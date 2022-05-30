package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.controller.AvaliacaoController;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.service.DiagnosticoService;
import br.ufc.quixada.npi.gestaocompetencia.service.ItemAvaliacaoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.model.Avaliacao.TipoAvaliacao;
import br.ufc.quixada.npi.gestaocompetencia.service.impl.AvaliacaoComportamentalService;
import br.ufc.quixada.npi.gestaocompetencia.service.impl.AvaliacaoService;

import static java.net.URLDecoder.decode;

@RestController
@RequestMapping("avaliacoes")
public class AvaliacaoControllerImpl{

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private DiagnosticoService diagnosticoService;
	
	@Autowired
	private AvaliacaoComportamentalService comportamentalService;

	@Autowired
	private ItemAvaliacaoService itemAvaliacaoService;

	private static final String ITEM_AVALIADO = "Item já avaliado";

	static final Logger LOGGER = LogManager.getLogger(AvaliacaoController.class.getName());
	
	@GetMapping({"", "/"})
	public ResponseEntity<List<Avaliacao>> findAll() {
		return null;
	}

	
	@GetMapping("/{avaliacao}")
	public ResponseEntity<Avaliacao> find(@PathVariable Avaliacao avaliacao) {
		return ResponseEntity.ok(avaliacao);
	}

	@GetMapping("/itens/{avaliacao}")
	public ResponseEntity<List<ItemAvaliacao>> findItens(@PathVariable Avaliacao avaliacao) {
		List<ItemAvaliacao> itens = avaliacao.getItens();
		return ResponseEntity.ok(itens);
	}
	
	@GetMapping({"/diagnostico/{diagnostico}"})
	public ResponseEntity<List<Avaliacao>> find(@AuthenticationPrincipal Usuario usuario, @PathVariable Diagnostico diagnostico, @RequestParam(value="tipo") TipoAvaliacao tipo, @RequestParam Avaliacao.Perspectiva perspectiva) {
		List<Avaliacao> avaliacoes = avaliacaoService.find(usuario, diagnostico, tipo, perspectiva);
		List<Avaliacao> retorno = new ArrayList<>();

		if(diagnosticoService.verifyAccess(diagnostico, usuario.getUnidade())) {
			if (TipoAvaliacao.AUTOAVALIACAO.equals(tipo) || TipoAvaliacao.CHEFIA.equals(tipo)) {
				if (hasNoAvaliacoes(avaliacoes)) {
					retorno.add(avaliacaoService.createSimple(tipo, usuario, diagnostico, perspectiva));
				} else {
					retorno.add(avaliacoes.get(0));
				}
			} else if (TipoAvaliacao.SUBORDINADO.equals(tipo) || TipoAvaliacao.PARES.equals(tipo)) {
				if (hasNoAvaliacoes(avaliacoes)) {
					retorno.addAll(avaliacaoService.createComplex(tipo, usuario, diagnostico, perspectiva));
				} else {
					retorno.addAll(avaliacoes);
				}
			}
		} else if(diagnosticoService.verifySecondaryAccess(diagnostico, usuario.getUnidade())) {
			if (hasNoAvaliacoes(avaliacoes)) {
				if (TipoAvaliacao.SUBORDINADO.equals(tipo)) {
					retorno.addAll(avaliacaoService.createComplexSecondaryAccess(tipo, usuario, diagnostico, perspectiva));
				}
			} else {
				retorno.addAll(avaliacoes);
			}
		}

		return ResponseEntity.ok(retorno);
	}

	@GetMapping({"/diagnostico/{diagnostico}/justificativas"})
	public ResponseEntity<List<Avaliacao>> findWithJustificativas(
        @AuthenticationPrincipal Usuario usuario,
        @PathVariable Diagnostico diagnostico,
        @RequestParam(value="tipo") TipoAvaliacao tipo,
        @RequestParam(value="perspectiva") Avaliacao.Perspectiva perspectiva) {
		List<Avaliacao> retorno = new ArrayList<>();

	    if((usuario.getUnidade().getChefe() != null && usuario.getUnidade().getChefe().equals(usuario)) ||
		(usuario.getUnidade().getViceChefe() != null && usuario.getUnidade().getViceChefe().equals(usuario))) {
            retorno.addAll(avaliacaoService.findAvaliacoesJustificadasUnidade(diagnostico, usuario.getUnidade(), tipo, perspectiva));
        } else {
            retorno.addAll(avaliacaoService.findAvaliacoesJustificadasUsuario(diagnostico, usuario, tipo, perspectiva));
        }

		return ResponseEntity.ok(retorno);
	}

	// Justificar avaliação
	@PostMapping("/{avaliacao}/justificativa")
	public ResponseEntity<Avaliacao> justificarAvaliacao(
		@AuthenticationPrincipal Usuario avaliador,
		@PathVariable Avaliacao avaliacao,
		@RequestBody String justificativa
	) {
		if(!avaliacao.getAvaliador().equals(avaliador))
			throw new GestaoCompetenciaException("Você não tem permissão para justificar esta avaliação");

		try {
			justificativa = decode(justificativa, "UTF-8");
			justificativa = justificativa.replace("=", "");
		} catch (UnsupportedEncodingException e) {
			LOGGER.fatal("An exception occurred.", e);
			return ResponseEntity.status(500).build();
		}

		return ResponseEntity.ok(avaliacaoService.justificar(avaliador, avaliacao, justificativa));
	}

    @PutMapping("/{avaliacao}/justificativa")
    public ResponseEntity<Avaliacao> removerJustificativa(
            @AuthenticationPrincipal Usuario avaliador,
            @PathVariable Avaliacao avaliacao
    ) {
        if(avaliacao.getAvaliador().equals(avaliador)) {
            return ResponseEntity.ok(avaliacaoService.removerJustificativa(avaliacao));
        } else {
            throw new GestaoCompetenciaException("Você não tem permissão para justificar esta avaliação");
        }
    }
	
	@GetMapping("/unidade/{unidade}")
	public ResponseEntity<Collection<Avaliacao>> getAllAvaliacaoByUserAndUnidade(@AuthenticationPrincipal Usuario avaliador, @PathVariable Unidade unidade) {
		return ResponseEntity.ok(avaliacaoService.findByAvaliadorAndUnidade(avaliador,unidade));
	}
	
	
//	@GetMapping("/{tipo}")
	public ResponseEntity<Collection<Avaliacao>> getAvaliacaoByTipoAvaliacaoAndUsuario(@AuthenticationPrincipal Usuario usuario, @PathVariable TipoAvaliacao tipo) {
		return ResponseEntity.ok(avaliacaoService.findAvaliacoes(usuario,tipo));
	}

	@PostMapping("/avaliar")
	public ResponseEntity<List<ItemAvaliacao>> avaliar(@RequestBody List<ItemAvaliacao> avaliacoes) {
		List<ItemAvaliacao> itens = new ArrayList<>();
		for(ItemAvaliacao item : avaliacoes) {
			if (Avaliacao.Perspectiva.COMPORTAMENTAL.equals(item.getAvaliacao().getPerspectiva()) && itemAvaliacaoService.findByFator(item) != null) {
				throw new GestaoCompetenciaException(ITEM_AVALIADO);
			}
			if (Avaliacao.Perspectiva.RESPONSABILIDADE.equals(item.getAvaliacao().getPerspectiva()) && itemAvaliacaoService.findByResponsabilidade(item) != null) {
				throw new GestaoCompetenciaException(ITEM_AVALIADO);
			}
			item.setAvaliacao(avaliacaoService.findById(item.getAvaliacao().getId()));
			itens.add(itemAvaliacaoService.save(item));
		}
		return ResponseEntity.ok().build();
	}
	

	@PostMapping("/{avaliacao}")
	public ResponseEntity<ItemAvaliacao> addAvaliacaoComportamental(@PathVariable Avaliacao avaliacao, @RequestBody ItemAvaliacao itemAvaliacao,
		@AuthenticationPrincipal Usuario usuario) {
		if(!avaliacao.getAvaliador().equals(usuario)) {
			throw new GestaoCompetenciaException("Você não tem permissão para realizar esta avaliação");
		}
		if (Avaliacao.Perspectiva.COMPORTAMENTAL.equals(avaliacao.getPerspectiva()) && itemAvaliacaoService.findByFator(itemAvaliacao) != null) {
			throw new GestaoCompetenciaException(ITEM_AVALIADO);
		}
		if (Avaliacao.Perspectiva.RESPONSABILIDADE.equals(avaliacao.getPerspectiva()) && itemAvaliacaoService.findByResponsabilidade(itemAvaliacao) != null) {
			throw new GestaoCompetenciaException(ITEM_AVALIADO);
		}
		itemAvaliacao.setAvaliacao(avaliacao);
		return ResponseEntity.ok(itemAvaliacaoService.save(itemAvaliacao));
	}
	
	
	@DeleteMapping("/{avaliacao}")
	public void deleteAvaliacao(@AuthenticationPrincipal Usuario usuario, @PathVariable Avaliacao avaliacao) {
		if(!avaliacao.getAvaliador().equals(usuario))
			throw new GestaoCompetenciaException("Você não tem permissão para alterar esta avaliação");
		avaliacaoService.delete(avaliacao);
	}
	
	
	@PutMapping("/comportamental/{avaliacao}")
	public ResponseEntity<ItemAvaliacao> putAtualizacaoComportamental(@PathVariable ItemAvaliacao avaliacao, @RequestBody ItemAvaliacao atualizada){
			atualizada.setAvaliacao(avaliacao.getAvaliacao());
			atualizada.setId(avaliacao.getId());
			
			if(atualizada.getFator() == null)
				atualizada.setFator(avaliacao.getFator());
						
		return ResponseEntity.ok(comportamentalService.put(atualizada));
	}
	
	
	@PostMapping("")
	public ResponseEntity<Avaliacao> addAvaliacao(@AuthenticationPrincipal Usuario avaliador, @RequestBody Avaliacao avaliacao){
		if(avaliacao.getTipo().equals(TipoAvaliacao.SUBORDINADO)) {
			if(!avaliador.isSubordinado(avaliacao.getAvaliado())) {
				throw new GestaoCompetenciaException("O usuário avaliado não é subordinado");				
			}
			else if(avaliador.getId().equals(avaliacao.getAvaliado().getId())) {
				throw new GestaoCompetenciaException("Você não pode avaliar a si mesmo como subordinado");							
			}
		}
		
		if(avaliacao.getTipo().equals(TipoAvaliacao.AUTOAVALIACAO)) 
			avaliacao.setAvaliado(avaliador);
		
		if(avaliacaoService.findAvaliacoesByAvaliadorAndAvaliadoAndTipo(avaliador, avaliacao.getAvaliado(), avaliacao.getTipo())!= null)
			throw new GestaoCompetenciaException("Já existe uma avaliação com o mesmo avaliador, avaliado e tipo");

		avaliacao.setAvaliador(avaliador);
		avaliacao.setUnidade(avaliador.getUnidade());
		return ResponseEntity.ok(avaliacaoService.postAvaliacao(avaliacao));
	}

	private boolean hasNoAvaliacoes(List<Avaliacao> lista) {
		return lista == null || lista.isEmpty();
	}
}
