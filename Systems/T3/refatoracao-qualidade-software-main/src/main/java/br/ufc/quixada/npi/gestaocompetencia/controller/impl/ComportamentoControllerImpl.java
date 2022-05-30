package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.net.URI;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.service.MapeamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.ufc.quixada.npi.gestaocompetencia.controller.ComportamentoController;
import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.exception.NotAllowedException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.CategoriaComportamento;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.Etapa;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.SituacaoComportamento;
import br.ufc.quixada.npi.gestaocompetencia.service.ComissaoService;
import br.ufc.quixada.npi.gestaocompetencia.service.CompetenciaService;
import br.ufc.quixada.npi.gestaocompetencia.service.ComportamentoService;

@RestController
@RequestMapping("comportamentos")
public class ComportamentoControllerImpl implements ComportamentoController {

	@Autowired
	private MapeamentoService mapeamentoService;

	@Autowired
	private ComportamentoService comportamentoService;
	
	@Autowired
	private CompetenciaService competenciaService;
	
	@Autowired
	private ComissaoService comissaoService;

	private static final String MAPEAMENTO = "Mapeamento";
	private static final String CODIGO = "Código";
	private static final String PATH_COMPORTAMENTO = "/comportamentos/";
	private static final String COMPORTAMENTO = "Comportamento";
	private static final String PERMISSAO_NORMALIZACAO = "Sem permissão para normalizar os comportamentos desse mapeamento";
	
	@Override
	@GetMapping("{mapeamento}/status")
	public ResponseEntity<Map<String, Object>> getStatus(@PathVariable Mapeamento mapeamento, 
												@AuthenticationPrincipal Usuario usuario){	
		if(mapeamento == null)
			throw new ResourceNotFoundException(MAPEAMENTO,
					CODIGO, "");
		
		Map<String, Object> response = new TreeMap<>();

		response.put("ativo", mapeamento.isPeriodoCadastroComportamentos());
		response.put("total", comportamentoService.countByServidorAndMapeamento(usuario, mapeamento));
				
		return ResponseEntity.ok(response);
	}
	
	@Override
	@PostMapping("/{mapeamento}")
	public ResponseEntity<Comportamento> save(@PathVariable Mapeamento mapeamento, @RequestBody Comportamento comportamento, @AuthenticationPrincipal Usuario usuario,
										HttpServletRequest request) {
		if(mapeamento == null)
			throw new ResourceNotFoundException(MAPEAMENTO,
					CODIGO, request.getRequestURI().toString().replace(PATH_COMPORTAMENTO, ""));

		if(!pertenceMapeamento(mapeamento, usuario.getUnidade())) {
			throw new NotAllowedException("Sem permissão para salvar comportamentos nesse mapeamento!");
		}
		
		if(!mapeamento.isPeriodoCadastroComportamentos())
			throw new GestaoCompetenciaException("Não é possível cadastrar um comportamento fora do prazo estipulado para cadastros de comportamentos");
			
		comportamento.setServidor(usuario);
		comportamento.setMapeamento(mapeamento);
		
		Comportamento comportamentoSaved = comportamentoService.save(comportamento);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("{mapeamento}").buildAndExpand(comportamentoSaved.getId()).toUri();
		
		return ResponseEntity.created(location).body(comportamentoSaved);
	}
	
	@Override
	@GetMapping("/{comportamento}")
	public ResponseEntity<Comportamento> findById(@PathVariable Comportamento comportamento,
										@AuthenticationPrincipal Usuario usuario,
										HttpServletRequest request){
		if(comportamento == null)
			throw new ResourceNotFoundException(COMPORTAMENTO,
					CODIGO, request.getRequestURI().toString().replace(PATH_COMPORTAMENTO, ""));
		
		if(!comissaoService.isMembroComissao(usuario) && !comportamento.getServidor().equals(usuario))
			throw new NotAllowedException("Comportamento pertecente a outro usuário");
		
		return ResponseEntity.ok(comportamento);
	}

	@GetMapping("/diagnostico/{diagnostico}")
	public ResponseEntity<List<Comportamento>> findByDiagnostico(@PathVariable Diagnostico diagnostico, @AuthenticationPrincipal Usuario usuario,
												  HttpServletRequest request){
		if(diagnostico == null)
			throw new ResourceNotFoundException(COMPORTAMENTO,
					CODIGO, request.getRequestURI().toString().replace(PATH_COMPORTAMENTO, ""));

		return ResponseEntity.ok(comportamentoService.findConsolidadosByMapeamento(diagnostico.getMapeamento()));
	}
	
	@GetMapping("/{mapeamento}/categorias")
	public ResponseEntity<Map<CategoriaComportamento, List<Comportamento>>> findByCategoria(@PathVariable Mapeamento mapeamento,
												@AuthenticationPrincipal Usuario usuario,
												HttpServletRequest request){
		if(mapeamento == null)
			throw new ResourceNotFoundException(MAPEAMENTO,
					CODIGO, request.getRequestURI().toString().replace(PATH_COMPORTAMENTO, ""));

		if(!pertenceMapeamento(mapeamento, usuario.getUnidade())) {
			throw new NotAllowedException("Sem permissão para visualizar os comportamentos nesse mapeamento!");
		}

		Map<CategoriaComportamento, List<Comportamento>> response = new TreeMap<>();
		
		response.put(CategoriaComportamento.GOSTO, comportamentoService.findByCategoria
				(usuario, mapeamento, CategoriaComportamento.GOSTO));
		
		response.put(CategoriaComportamento.NAO_GOSTO, comportamentoService.findByCategoria
				(usuario, mapeamento, CategoriaComportamento.NAO_GOSTO));
		
		response.put(CategoriaComportamento.IDEAL, comportamentoService.findByCategoria
				(usuario, mapeamento, CategoriaComportamento.IDEAL));
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{etapa}/{comportamentoOriginal}")
	public ResponseEntity<Comportamento> update(@PathVariable Comportamento comportamentoOriginal,
												@PathVariable Etapa etapa,
												@RequestBody Comportamento comportamento,
												@AuthenticationPrincipal Usuario usuario,
												HttpServletRequest request){
	
		if(comportamentoOriginal == null)
			throw new ResourceNotFoundException(COMPORTAMENTO, CODIGO,
					request.getRequestURI().toString().replace(PATH_COMPORTAMENTO, ""));
		
		if(comportamentoOriginal.isExcluido())
			throw new GestaoCompetenciaException("Comportamento já se encontra excluído");
		
		if(!comissaoService.isMembroComissao(usuario) && !comportamentoOriginal.getServidor().equals(usuario))
			throw new NotAllowedException("Sem permissão para editar o comportamento desejado");
	
		if(etapa == Etapa.CADASTRO_COMPORTAMENTOS) {
			//Atualização a partir do cadastro de comportamentos
			
			if(!comportamentoOriginal.getMapeamento().isPeriodoCadastroComportamentos())
				throw new GestaoCompetenciaException("Não é possível atualizar um comportamento fora do prazo estipulado para cadastros de comportamentos");
			
			comportamentoOriginal.setDescricao(comportamento.getDescricao());
		} 
		else {
			//Atualização a partir da normalização comportamental
			if(!comportamentoOriginal.getMapeamento().isPeriodoNormalizacaoComportamentos())
				throw new GestaoCompetenciaException("Não é possível normalizar um comportamento fora do prazo "
						+ "estipulado para normalização de comportamentos");
			
			comportamentoOriginal.setDescricaoAtualizada(comportamento.getDescricaoAtualizada());
			
			if(comportamento.getCompetencia().getId() == -1)
				comportamentoOriginal.setCompetencia(null);
			
			comportamentoOriginal.setImportancia(comportamento.getImportancia());
			comportamentoOriginal.setResponsavelComissao(usuario);
			
			if(comportamento.getCompetencia() != null && comportamento.getCompetencia().getId() != -1) {
				Competencia competencia = competenciaService.findById(comportamento.getCompetencia().getId())
						.orElseThrow(() -> new ResourceNotFoundException("Competencia", CODIGO, comportamento.getCompetencia().getId()));
				comportamentoOriginal.setCompetencia(competencia);
			}
			
		}
		
		Comportamento comportamentoAtualizado = comportamentoService.update(comportamentoOriginal)
				.orElseThrow(() -> new GestaoCompetenciaException("Erro ao tentar atualizar o comportamento desejado"));
		
		return ResponseEntity.ok(comportamentoAtualizado);
	}
	
	@DeleteMapping("/{comportamento}")
	public ResponseEntity<Void> delete(@PathVariable Comportamento comportamento,
									   @AuthenticationPrincipal Usuario usuario,
									   HttpServletRequest request){
		
		if(comportamento == null) {
			throw new ResourceNotFoundException(COMPORTAMENTO, CODIGO,
					request.getRequestURI().toString().replace(PATH_COMPORTAMENTO, ""));
		}

		if(!comportamento.getMapeamento().isPeriodoCadastroComportamentos()) {
			throw new GestaoCompetenciaException("Não é possível remover um comportamento fora do prazo estipulado para cadastros de comportamentos");
		}
		
		if(!comissaoService.isMembroComissao(usuario) && !comportamento.getServidor().equals(usuario))
			throw new NotAllowedException("Sem permissão para remover o comportamento desejado");

		if(comissaoService.isMembroComissao(usuario) && !comportamento.getServidor().equals(usuario)) {
			comportamentoService.deleteByComissao(comportamento)
					.orElseThrow(() -> new GestaoCompetenciaException("Erro ao tentar remover o comportamento desejado"));
		}

		if(comportamento.getServidor().equals(usuario)) {
			comportamentoService.delete(comportamento);
		}
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("")
	public ResponseEntity<List<Comportamento>> findByMapeamento(
        @RequestParam(name="unidade", required=false) Unidade[] unidades,
		@RequestParam(name="mapeamento", required=false) Mapeamento mapeamento,
        @RequestParam(required=false) SituacaoComportamento situacao,
        @RequestParam(value = "competencia", required=false) Integer competenciaId,
        @RequestParam(required=false) Boolean subunidades,
        @AuthenticationPrincipal Usuario usuario){
            Optional<Competencia> competencia = competenciaService.findById(competenciaId);
            ComportamentoSearch search = new ComportamentoSearch(unidades, mapeamento, situacao, competencia.isPresent() ? competencia.get() : null, subunidades);
            ComportamentoSpecification comportamentoSpecification = new ComportamentoSpecification(search);

            List<Comportamento> comportamentos = comportamentoService.search(comportamentoSpecification);

            return ResponseEntity.ok(comportamentos);
	}

	@Override
	@PutMapping("/normalizacao/consolidar")
	public ResponseEntity<List<Comportamento>> consolidarComportamentos(@RequestBody List<Comportamento> comportamentos, @AuthenticationPrincipal Usuario usuario){
		
		if(!comissaoService.isMembroComissao(usuario))
			throw new NotAllowedException(PERMISSAO_NORMALIZACAO);
		
		List<Comportamento> comportamentosAux = new ArrayList<>();
		Comportamento comportamentoOriginal = null;
		
		for(Comportamento comportamento : comportamentos) {
			if(comportamento.getDescricaoAtualizada() == null)
				comportamento.setDescricaoAtualizada(comportamento.getDescricao());
			
			if(comportamento.isExcluido())
				throw new GestaoCompetenciaException("Comportamento já encontra excluído");
			
			comportamentoOriginal = comportamentoService.findById(comportamento.getId())
					.orElseThrow(() -> new ResourceNotFoundException(COMPORTAMENTO, CODIGO, comportamento.getId()));
			
			comportamentoOriginal.setCompetencia(comportamento.getCompetencia());
			comportamentoOriginal.setDescricaoAtualizada(comportamento.getDescricaoAtualizada());
			comportamentoOriginal.setConsolidado(true);
			
			comportamentosAux.add(comportamentoService.update(comportamentoOriginal)
									.orElseThrow(() -> new GestaoCompetenciaException(
											"Erro ao atualizar o comportamento: "+comportamento.getId())));
		}
		
		return ResponseEntity.ok(comportamentosAux);
	}

	@PutMapping("/normalizacao/vincular")
	public ResponseEntity<List<Comportamento>> vincularComportamentos(@RequestBody List<Comportamento> comportamentos, @AuthenticationPrincipal Usuario usuario){

		if(!comissaoService.isMembroComissao(usuario))
			throw new NotAllowedException(PERMISSAO_NORMALIZACAO);

		List<Comportamento> comportamentosAux = new ArrayList<>();
		Comportamento comportamentoOriginal = null;

		for(Comportamento comportamento : comportamentos) {
			if(comportamento.isExcluido())
				throw new GestaoCompetenciaException("Comportamento já encontra excluído");

			comportamentoOriginal = comportamentoService.findById(comportamento.getId())
					.orElseThrow(() -> new ResourceNotFoundException(COMPORTAMENTO, CODIGO, comportamento.getId()));
			if(comportamento.getCompetencia().getId() == -1)
				comportamentoOriginal.setCompetencia(null);
			else
				comportamentoOriginal.setCompetencia(comportamento.getCompetencia());

			comportamentoOriginal.setDescricaoAtualizada(comportamento.getDescricaoAtualizada());

			comportamentosAux.add(comportamentoService.update(comportamentoOriginal)
									.orElseThrow(() -> new GestaoCompetenciaException(
											"Erro ao atualizar o comportamento: "+comportamento.getId())));
		}
		return ResponseEntity.ok(comportamentosAux);
	}
	
	@PutMapping("/normalizacao/restaurar/{comportamento}")
	public ResponseEntity<Comportamento> restaurarComportamento(@PathVariable Comportamento comportamento,
															@AuthenticationPrincipal Usuario usuario,
															HttpServletRequest request){
		if(comportamento == null)
			throw new ResourceNotFoundException(COMPORTAMENTO,
					CODIGO, request.getRequestURI().toString().replace("/comportamentos/normalizacao/restaurar", ""));
		
		if(!comissaoService.isMembroComissao(usuario))
			throw new NotAllowedException("Sem permissão para restaurar esse comportamento");
		
		comportamento.setExcluido(false);
		
		Comportamento comportamentoAtualizado = comportamentoService.update(comportamento)
				.orElseThrow(() -> new GestaoCompetenciaException(
						"Erro ao restaurar o comportamento: "+comportamento.getId())); 
		
		return ResponseEntity.ok(comportamentoAtualizado);
	}
	
	@PutMapping("/normalizacao/desconsolidar/{comportamento}")
	public ResponseEntity<Comportamento> desconsolidarComportamento(@PathVariable Comportamento comportamento,
															@AuthenticationPrincipal Usuario usuario,
															HttpServletRequest request){
		
		if(comportamento == null)
			throw new ResourceNotFoundException(COMPORTAMENTO,
					CODIGO, request.getRequestURI().toString().replace("/comportamentos/normalizacao/restaurar", ""));
		
		if(!comissaoService.isMembroComissao(usuario))
			throw new NotAllowedException("Sem permissão para restaurar esse comportamento");
		
		if(!comportamento.getMapeamento().isPeriodoNormalizacaoComportamentos())
			throw new GestaoCompetenciaException("Não é possível normalizar os comportamentos fora do prazo "
					+ "estipulado para essa ação");
		
		comportamento.setConsolidado(false);
		
		Comportamento comportamentoAtualizado = comportamentoService.update(comportamento)
				.orElseThrow(() -> new GestaoCompetenciaException(
						"Erro ao desconsolidar o comportamento: "+comportamento.getId())); 
		
		return ResponseEntity.ok(comportamentoAtualizado);
	}
	
	@PutMapping("/normalizacao/excluir/{comportamento}")
	public ResponseEntity<Comportamento> excluirComportamentos(@PathVariable Comportamento comportamento, @AuthenticationPrincipal Usuario usuario){
		
		if(!comissaoService.isMembroComissao(usuario))
			throw new NotAllowedException(PERMISSAO_NORMALIZACAO);
			
			comportamento.setExcluido(true);
			Comportamento aux = comportamentoService.update(comportamento).orElseThrow(() -> new GestaoCompetenciaException(
											"Erro ao excluir o comportamento: "+comportamento.getId()));
			
		return ResponseEntity.ok(aux);
	}

	private boolean pertenceMapeamento(Mapeamento mapeamento, Unidade unidade) {
		return mapeamentoService.verifyAccess(mapeamento, unidade) || mapeamentoService.verifySecondaryAccess(mapeamento, unidade);
	}

}
