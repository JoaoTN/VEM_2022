package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufc.quixada.npi.gestaocompetencia.controller.DesempenhoController;
import br.ufc.quixada.npi.gestaocompetencia.exception.NotAllowedException;
import br.ufc.quixada.npi.gestaocompetencia.model.Avaliacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Avaliacao.Perspectiva;
import br.ufc.quixada.npi.gestaocompetencia.model.Diagnostico;
import br.ufc.quixada.npi.gestaocompetencia.model.ItemAvaliacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Responsabilidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.DiagnosticoService;
import br.ufc.quixada.npi.gestaocompetencia.service.ResponsabilidadeService;
import br.ufc.quixada.npi.gestaocompetencia.service.UnidadeService;
import br.ufc.quixada.npi.gestaocompetencia.service.UsuarioService;
import br.ufc.quixada.npi.gestaocompetencia.service.impl.AvaliacaoService;

@RestController
@RequestMapping("/desempenho")
public class DesempenhoControllerImpl implements DesempenhoController {
	@Autowired
	private ResponsabilidadeService responsabilidadeService;

	@Autowired
	private DiagnosticoService diagnosticoService;

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UnidadeService unidadeService;

	@Override
	@GetMapping({"/avaliacoes", "/diagnostico/{diagnosticoId}/avaliacoes"})
	public ResponseEntity<List<Map<String, Object>>> getAvaliacoes(@AuthenticationPrincipal Usuario usuario,
	@PathVariable(required = false) Diagnostico diagnosticoId, @RequestParam Avaliacao.TipoAvaliacao tipo,
	@RequestParam Avaliacao.Perspectiva perspectiva, @RequestParam boolean recebidas) {
		Diagnostico diagnostico = null;
		List<Avaliacao> resultado;
		List<Map<String, Object>> avaliacoes = new ArrayList<>();

		if(diagnosticoId != null) {
			if(diagnosticoService.verifyAccess(diagnosticoId, usuario.getUnidade())
				|| diagnosticoService.verifySecondaryAccess(diagnosticoId, usuario.getUnidade())
			) {
				diagnostico = diagnosticoId;
			}
		} else {
			diagnostico = diagnosticoService.findLast(usuario.getUnidade());
		}

		if(diagnostico != null) {
			if(recebidas) {
				resultado = avaliacaoService.findByAvaliado(usuario, diagnostico, tipo, perspectiva);
			} else {
				resultado = avaliacaoService.find(usuario, diagnostico, tipo, perspectiva);
			}
			avaliacoes = setDados(resultado, usuario, perspectiva, avaliacoes);
		}

		return ResponseEntity.ok(avaliacoes);
	}

	@Override
	@GetMapping({"/avaliacoes/servidores", "/diagnostico/{diagnosticoId}/avaliacoes/servidores"})
	public ResponseEntity<List<Map<String, Object>>> getAvaliacoesServidores(@AuthenticationPrincipal Usuario usuario,
	@PathVariable(required = false) Diagnostico diagnosticoId, @RequestParam Avaliacao.TipoAvaliacao tipo,
	@RequestParam Avaliacao.Perspectiva perspectiva, @RequestParam(required = false) Unidade unidade) {
		Diagnostico diagnostico = null;
		List<Avaliacao> resultado;
		List<Map<String, Object>> avaliacoes = new ArrayList<>();
		List<Usuario> servidores;

        if(unidade == null) {
            if(!usuario.getUnidade().hasPermissionCRUD(usuario)) {
                throw new NotAllowedException("Sem permissão para visualizar as avaliações");
            } else {
                unidade = usuario.getUnidade();
            }
        } else {
            if (!unidadeService.hasPermissionRead(unidade, usuario)) {
                throw new NotAllowedException("Sem permissão para visualizar as avaliações");
            }
        }

		servidores = usuarioService.findByUnidade(unidade);
		servidores.remove(usuario);

        if(unidade.getViceChefe() != null && unidade.getViceChefe().equals(usuario)) {
            servidores.remove(unidade.getChefe());
        }

		List<Unidade> subunidades = unidadeService.findByUnidadePai(unidade);
		for (Unidade subunidade : subunidades) {
			servidores.add(subunidade.getChefe());
		}

		if(diagnosticoId != null) {
			if(diagnosticoService.verifyAccess(diagnosticoId, usuario.getUnidade())
				|| diagnosticoService.verifySecondaryAccess(diagnosticoId, usuario.getUnidade())
			) {
				diagnostico = diagnosticoId;
			}
		} else {
			diagnostico = diagnosticoService.findLast(unidade);
		}

		if(!servidores.isEmpty()) {
			for(Usuario servidor : servidores) {
				resultado = avaliacaoService.findByAvaliado(servidor, diagnostico, tipo, perspectiva);
				avaliacoes = setDados(resultado, servidor, perspectiva, avaliacoes);
			}

			return ResponseEntity.ok(avaliacoes);
		}

		return null;
	}

	public List<Map<String, Object>> setDados(List<Avaliacao> resultado, Usuario servidor, Perspectiva perspectiva, List<Map<String, Object>> avaliacoes) {
		for(Avaliacao avaliacao : resultado) {
			avaliacao.getItens().removeIf(ItemAvaliacao::isNaoAplica);

			if (!avaliacao.getItens().isEmpty()) {
				Map<String, Object> dados = new LinkedHashMap<>();
				dados.put("ID", avaliacao.getId());
				dados.put("AVALIACAO", avaliacao);
				dados.put("ITENS", avaliacao.getItens());

				if (Perspectiva.RESPONSABILIDADE.equals(perspectiva)) {
					List<Responsabilidade> responsabilidades = new ArrayList<>();
					if(servidor.getUnidade().getChefe().equals(servidor)) {
						responsabilidades = responsabilidadeService.findConsolidadas(servidor.getUnidade(), avaliacao.getDiagnostico().getMapeamento());
					} else {
						for(ItemAvaliacao item : avaliacao.getItens()) {
							responsabilidades.add(item.getResponsabilidade());
						}
					}

					dados.put("RESPONSABILIDADES", responsabilidades);
				}

				avaliacoes.add(dados);
			}
		}

		return avaliacoes;
	}
}
