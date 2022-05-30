 package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import br.ufc.quixada.npi.gestaocompetencia.exception.NotAllowedException;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.service.MapeamentoService;
import br.ufc.quixada.npi.gestaocompetencia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.ufc.quixada.npi.gestaocompetencia.service.UnidadeService;
import br.ufc.quixada.npi.gestaocompetencia.service.impl.UnidadeMapeadaService;

@RestController
@RequestMapping("unidades")
public class UnidadeControllerImpl {

	@Autowired
	private UnidadeService unidadeService;
	
	@Autowired
	private UnidadeMapeadaService unidadeMapeadaService;

	@Autowired
	private MapeamentoService mapeamentoService;

	@Autowired
	private UsuarioService usuarioService;

	private static final String ID = "ID";
	private static final String UNIDADE = "UNIDADE";
	private static final String SERVIDOR = "SERVIDOR";
	private static final String GESTOR = "GESTOR";
	private static final String TIPO = "TIPO";
	private static final String PERMISSAO_ACESSAR = "Sem permissão para acessar os servidores";

	@GetMapping("/{unidade}")
	public ResponseEntity<Unidade> find(@PathVariable Unidade unidade) {
		return ResponseEntity.ok(unidade);
	}

	@GetMapping({"", "/"})
	public ResponseEntity<List<Unidade>> findAll() {
		return ResponseEntity.ok(unidadeService.findAll());
	}

	@GetMapping({"/validacao"})
	public ResponseEntity<List<Unidade>> findAllByGestor(@RequestParam(required = false) Mapeamento mapeamento, @AuthenticationPrincipal Usuario usuario) {
		List<Unidade> resultado = new ArrayList<>();
		UnidadeMapeada unidadeMapeada;
		List<Unidade> subunidades = unidadeService.findByUnidadePai(usuario.getUnidade());

		if(mapeamento == null) {
			unidadeMapeada = unidadeMapeadaService.findByMapeamento(mapeamentoService.findLastMapeamento(usuario));
		} else {
			if(mapeamentoService.verifySecondaryAccess(mapeamento, usuario.getUnidade())) {
				unidadeMapeada = unidadeMapeadaService.findByMapeamento(mapeamento);
			} else {
				unidadeMapeada = null;
			}
		}

		if(unidadeMapeada != null) {
			List<Unidade> unidades = unidadeMapeada.getUnidades();
			for(Unidade u : subunidades){
				if(unidades.contains(u) || (unidadeMapeada.getUnidade().equals(u) && mapeamento != null)) {
					resultado.add(u);
				}
			}

			return ResponseEntity.ok(resultado);
		} else {
			return null;
		}
	}
	
	@GetMapping("/usuario")
	public ResponseEntity<Unidade> findByUsuario(@AuthenticationPrincipal Usuario usuario){
		return ResponseEntity.ok(usuario.getUnidade());
	}

	@GetMapping("/subunidades")
	public ResponseEntity<List<Unidade>> findSubunidades(@AuthenticationPrincipal Usuario usuario,
	@RequestParam(required = false) Mapeamento mapeamento, @RequestParam(required = false) Diagnostico diagnostico) {
		List<Unidade> unidadeAndSubunidades = getUnidadeAndSubunidades(usuario.getUnidade());
		List<Unidade> unidadesMapeamento;
		List<Unidade> unidades = new ArrayList<>();

		if(diagnostico != null) {
			mapeamento = diagnostico.getMapeamento();
		}

		if(mapeamento != null) {
			unidadesMapeamento = mapeamentoService.getUnidadesByMapeamento(mapeamento);

			for (Unidade unidade : unidadeAndSubunidades) {
				if(unidadesMapeamento != null && unidadesMapeamento.contains(unidade)) {
					unidades.add(unidade);
				}
			}
		} else {
			unidades.addAll(unidadeAndSubunidades);
		}

		return ResponseEntity.ok(unidades);
	}

	@PostMapping("/subunidades")
	public ResponseEntity<List<Unidade>> findAllUnidadesByUnidade(@RequestBody List<Unidade> unidades) {
		List<Unidade> aux = new ArrayList<>();

		for (Unidade u : unidades) {
			if(!aux.contains(u))
				aux.add(u);
			List<Unidade> aux2 = unidadeService.findAllSubunidadesByUnidade(u);
			for (Unidade u2 : aux2) {
				if(!aux.contains(u2)) {
					aux.add(u2);
				}
			}
		}
		return ResponseEntity.ok(aux);

	}

	@GetMapping("/servidores")
	public  ResponseEntity<List<Map<String, Object>>> findServidores(@AuthenticationPrincipal Usuario usuario,
	@RequestParam(required = false) Unidade unidade) {
		List<Map<String, Object>> dados = new ArrayList<>();
		List<Usuario> servidores;

		if(unidade == null) {
			if(!usuario.getUnidade().hasPermissionCRUD(usuario)) {
				throw new NotAllowedException(PERMISSAO_ACESSAR);
			} else {
				unidade = usuario.getUnidade();
			}
		} else {
			if (!unidadeService.hasPermissionRead(unidade, usuario)) {
				throw new NotAllowedException(PERMISSAO_ACESSAR);
			}
		}

		// SERVIDORES
		servidores = usuarioService.findByUnidade(unidade);
		servidores.remove(usuario);

		if(unidade.getViceChefe() != null && unidade.getViceChefe().equals(usuario)) {
			servidores.remove(unidade.getChefe());
		}

		for (Usuario servidor : servidores) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put(ID, servidor.getId());
			map.put(SERVIDOR, servidor);
			map.put(UNIDADE, servidor.getUnidade());
			if(servidor.getUnidade().hasPermissionCRUD(servidor)) {
				map.put(TIPO, GESTOR);
			} else {
				map.put(TIPO, SERVIDOR);
			}

			dados.add(map);
		}

		// GESTORES
		List<Unidade> subunidades = unidadeService.findByUnidadePai(unidade);
		for (Unidade subunidade : subunidades) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put(ID, subunidade.getChefe().getId());
			map.put(SERVIDOR, subunidade.getChefe());
			map.put(UNIDADE, subunidade);
			map.put(TIPO, GESTOR);

			dados.add(map);
		}

		return ResponseEntity.ok(dados);
	}

	@GetMapping("/mapeamento")
	public ResponseEntity<List<Unidade>> findAllUnidadesWithMapeamento() {
		return ResponseEntity.ok(unidadeService.findAllWithMapeamento());
	}
        
	@PostMapping("/mapeamento")
	public ResponseEntity<UnidadeMapeada> relateUnidadeToMapeamento(@RequestBody UnidadeMapeada unidadeMapeada) {
		return ResponseEntity.ok(unidadeMapeadaService.create(unidadeMapeada));
	}

	public List<Unidade> getUnidadeAndSubunidades(Unidade unidade) {
		List<Unidade> unidades = new ArrayList<>();
		List<Unidade> unidadesAux = unidadeService.findByUnidadePai(unidade);

		unidades.add(unidade);
		if(unidadesAux != null && !unidadesAux.isEmpty()) {
			for (Unidade u : unidadesAux) {
				unidades.addAll(getUnidadeAndSubunidades(u));
			}
		}

		return unidades;
	}
}