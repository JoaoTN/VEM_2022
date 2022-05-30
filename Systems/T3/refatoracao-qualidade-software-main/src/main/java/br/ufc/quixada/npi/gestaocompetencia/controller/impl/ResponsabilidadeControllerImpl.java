package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.repository.AvaliacaoRepository;
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

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.exception.NotAllowedException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.Etapa;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.SituacaoResponsabilidade;
import br.ufc.quixada.npi.gestaocompetencia.service.ComissaoService;
import br.ufc.quixada.npi.gestaocompetencia.service.MapeamentoService;
import br.ufc.quixada.npi.gestaocompetencia.service.ResponsabilidadeService;
import br.ufc.quixada.npi.gestaocompetencia.service.UsuarioService;

@RestController
@RequestMapping("responsabilidades")
public class ResponsabilidadeControllerImpl {

	@Autowired
	private ResponsabilidadeService responsabilidadeService;
	
	@Autowired
	private ComissaoService comissaoService;
	
	@Autowired
	private MapeamentoService mapeamentoService;
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	private static final String MAPEAMENTO = "Mapeamento";
	private static final String CODIGO = "Código";
	private static final String PATH_RESPONSABILIDADE = "/responsabilidades/";
	private static final String RESPONSABILIDADE = "Responsabilidade";
	private static final String PERMISSAO_MODIFICAR = "Sem permissão para ver ou modificar este mapeamento";
	
	@GetMapping("/{mapeamento}")
	public ResponseEntity<Collection<Responsabilidade>> findAllByUsuario(@PathVariable("mapeamento")Mapeamento mapeamento, @AuthenticationPrincipal Usuario usuario,
			HttpServletRequest request){

		if(mapeamento == null)
			throw new ResourceNotFoundException(MAPEAMENTO, CODIGO, request.getRequestURI().toString().replace(PATH_RESPONSABILIDADE, ""));

		if(!usuario.getUnidade().hasPermissionCRUD(usuario))
			throw new NotAllowedException(PERMISSAO_MODIFICAR);

		return ResponseEntity.ok(responsabilidadeService.findAll(usuario.getUnidade(), mapeamento));
	}

	@GetMapping("/diagnostico/{diagnostico}/autoavaliacao")
	public ResponseEntity<List<Responsabilidade>> findConsolidadasAutoavaliacao(@PathVariable Diagnostico diagnostico, @AuthenticationPrincipal Usuario usuario){
		List<Avaliacao> avaliacoes = avaliacaoRepository.findAvaliacaoByAvaliado(usuario, diagnostico, Avaliacao.TipoAvaliacao.SUBORDINADO, Avaliacao.Perspectiva.RESPONSABILIDADE);
		if(usuario.getUnidade().getChefe() == usuario || usuario.getUnidade().getViceChefe() == usuario) {
			return ResponseEntity.ok(responsabilidadeService.findConsolidadas(usuario.getUnidade(), diagnostico.getMapeamento()));
		} else {
			if (avaliacoes != null && !avaliacoes.isEmpty()) {
				List<Responsabilidade> responsabilidades = new ArrayList<>();
				List<ItemAvaliacao> itens = avaliacoes.get(0).getItens();
				for (ItemAvaliacao item : itens) {
					if (!item.isNaoAplica()) {
						responsabilidades.add(item.getResponsabilidade());
					}
				}
				return ResponseEntity.ok(responsabilidades);
			} else { return null; }
		}
	}

	@GetMapping("/diagnostico/{diagnostico}")
	public ResponseEntity<List<Responsabilidade>> findConsolidadas(@PathVariable Diagnostico diagnostico, @AuthenticationPrincipal Usuario usuario){
		return ResponseEntity.ok(responsabilidadeService.findConsolidadas(usuario.getUnidade(), diagnostico.getMapeamento()));
	}

	@GetMapping("/diagnostico/{diagnostico}/unidade/{unidade}")
	public ResponseEntity<List<Responsabilidade>> findConsolidadasByUnidade(@PathVariable Diagnostico diagnostico, @PathVariable Unidade unidade) {
		return ResponseEntity.ok(responsabilidadeService.findConsolidadas(unidade, diagnostico.getMapeamento()));
	}

	@PostMapping("/{mapeamento}")
	public ResponseEntity<Responsabilidade> save(@PathVariable("mapeamento")Mapeamento mapeamento, @AuthenticationPrincipal Usuario usuario,
			@RequestBody Responsabilidade responsabilidade,
			HttpServletRequest request){

		if(mapeamento == null )
			throw new ResourceNotFoundException(MAPEAMENTO, CODIGO, request.getRequestURI().toString().replace(PATH_RESPONSABILIDADE, ""));

		if(!usuario.getUnidade().hasPermissionCRUD(usuario))
			throw new NotAllowedException(PERMISSAO_MODIFICAR);
		
		if(!mapeamento.isPeriodoGestorEdicaoResponsabilidades())
			throw new GestaoCompetenciaException("Não é possível editar uma responsabilidade fora do prazo "
					+ "estipulado para o cadastros de responsabilidades");
		responsabilidade.setUnidade(usuario.getUnidade());
		responsabilidade.setMapeamento(mapeamento);
		return ResponseEntity.ok(responsabilidadeService.save(responsabilidade, usuario));
	}

	@PutMapping("/{responsabilidade}")
	public ResponseEntity<Responsabilidade> update(@AuthenticationPrincipal Usuario usuario, @PathVariable("responsabilidade") Responsabilidade responsabilidade,
			@RequestBody Responsabilidade responsabilidadeAtualizada,
			HttpServletRequest request){
		
		if(responsabilidade == null )
			throw new ResourceNotFoundException(RESPONSABILIDADE, CODIGO, request.getRequestURI().toString().replace(PATH_RESPONSABILIDADE, ""));

		if(
			!responsabilidade.getUnidade().getChefe().equals(usuario)
			&& !responsabilidade.getUnidade().getViceChefe().equals(usuario)
		)
			throw new NotAllowedException("Você não tem permissão para ver ou modificar este responsabilidade");

		
		responsabilidadeAtualizada.setUsuarioGestor(responsabilidade.getUsuarioGestor());
		responsabilidadeAtualizada.setId(responsabilidade.getId());
		responsabilidadeAtualizada.setMapeamento(responsabilidade.getMapeamento());
		return ResponseEntity.ok(responsabilidadeService.update(responsabilidadeAtualizada));
	}

	@DeleteMapping("/{mapeamento}")
	public ResponseEntity<Void> delete(@PathVariable("mapeamento")Mapeamento mapeamento, @RequestParam(value="responsabilidade", required = true)Responsabilidade responsabilidade, @AuthenticationPrincipal Usuario usuario,
			HttpServletRequest request){

		if(mapeamento == null)
			throw new ResourceNotFoundException(MAPEAMENTO, CODIGO, request.getRequestURI().toString().replace(PATH_RESPONSABILIDADE, ""));
			
		if(responsabilidade == null && request.getParameter("responsabilidade") != null)
			throw new ResourceNotFoundException(RESPONSABILIDADE, CODIGO, request.getParameter("responsabilidade"));

		if(responsabilidade != null && responsabilidade.getMapeamento() != null && !responsabilidade.getMapeamento().equals(mapeamento))
			throw new NotAllowedException("Esta responsabilidade não pertence a este mapeamento");

		if(!usuario.getUnidade().hasPermissionCRUD(usuario))
			throw new NotAllowedException(PERMISSAO_MODIFICAR);

		if(!mapeamento.isPeriodoGestorEdicaoResponsabilidades())
			throw new GestaoCompetenciaException("Não é possível editar uma responsabilidade fora do prazo "
					+ "estipulado para o cadastros de responsabilidades");

		try {
			responsabilidadeService.delete(responsabilidade);
			return ResponseEntity.noContent().build();
		}catch (ResourceNotFoundException e) {
		  return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/normalizacao/{mapeamento}")
	public ResponseEntity<Collection<Responsabilidade>> getResponsabilidadeNormalizacao(
			@PathVariable Mapeamento mapeamento,
			@RequestParam(name="unidade", required=false) Unidade[] unidades,
			@RequestParam(required=false) SituacaoResponsabilidade situacao,
			@AuthenticationPrincipal Usuario usuario, HttpServletRequest request){

		if(!comissaoService.isMembroComissao(usuario) && !usuarioService.isChefia(usuario) && !usuarioService.isViceChefia(usuario))
				throw new NotAllowedException(PERMISSAO_MODIFICAR);
		
		if(unidades == null) {
			return ResponseEntity.ok(new ArrayList<>());
		}
		
		ResponsabilidadeSearch search = new ResponsabilidadeSearch( unidades, situacao, mapeamento);
		ResponsabilidadeSpecification responsabilidade = new ResponsabilidadeSpecification(search);
		return ResponseEntity.ok(responsabilidadeService.search(responsabilidade));
	}

	@DeleteMapping("/{mapeamento}/{responsabilidade}")
	public ResponseEntity<Responsabilidade> deleteResponsabilidadeNormalizacao(@PathVariable("mapeamento") Mapeamento mapeamento,
			@PathVariable("responsabilidade") Responsabilidade responsabilidade, @AuthenticationPrincipal Usuario usuario, HttpServletRequest request){

		responsabilidade.setExcluida(true);
		
		try {
			responsabilidadeService.update(responsabilidade);
			return ResponseEntity.noContent().build();
		}catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/normalizacao")
	public ResponseEntity<Responsabilidade> updateResponsabilidadeNormalizacao(@RequestParam Responsabilidade responsabilidade,
	@RequestParam Mapeamento mapeamento, @RequestParam Etapa etapa, @RequestBody Responsabilidade responsabilidadeAtualizada,
	@AuthenticationPrincipal Usuario usuario, HttpServletRequest request){
		boolean cadastro = etapa.equals(Etapa.CADASTRO_RESPONSABILIDADES) &&
			usuario.getUnidade().hasPermissionCRUD(usuario) &&
			mapeamentoService.verifyAccess(mapeamento, usuario.getUnidade()) &&
			mapeamento.isPeriodoGestorEdicaoResponsabilidades();

		boolean normalizacao = etapa.equals(Etapa.NORMALIZACAO_RESPONSABILIDADES) &&
			comissaoService.isMembroComissao(usuario) &&
			mapeamento.isPeriodoEdicaoComissaoResponsabilidades();

		boolean validacao = etapa.equals(Etapa.VALIDACAO_RESPONSABILIDADES) &&
			usuarioService.hasUnidadesFilhas(usuario) &&
			(
				mapeamentoService.verifyAccess(mapeamento, usuario.getUnidade()) ||
				mapeamentoService.verifySecondaryAccess(mapeamento, usuario.getUnidade())
			) &&
			mapeamento.isPeriodoEdicaoChefiaResponsabilidades();

		boolean consolidacao = etapa.equals(Etapa.CONSOLIDACAO_RESPONSABILIDADES) &&
			comissaoService.isMembroComissao(usuario) &&
			mapeamento.isPeriodoEdicaoConsolidacaoResponsabilidades();

		responsabilidade.setEditada(true);

		if(cadastro) {
			cadastroResponsabilidadeUpdate(responsabilidade, responsabilidadeAtualizada, usuario);
		} else if(normalizacao) {
			normalizacaoResponsabilidadeUpdate(responsabilidade, responsabilidadeAtualizada, usuario);
		} else if(validacao) {
			validacaoResponsabilidadeUpdate(responsabilidade, responsabilidadeAtualizada, usuario);
		} else if(consolidacao) {
			consolidacaoResponsabilidadeUpdate(responsabilidade, responsabilidadeAtualizada, usuario);
		} else {
			throw new GestaoCompetenciaException("Não é possível editar a responsabilidade ou por estar fora do prazo "
					+ " ou por não ter privilégios sucientes para realizar a operação");
		}

		return ResponseEntity.ok(responsabilidadeService.update(responsabilidade));
	}

	@PutMapping("/normalizacao/validar")
	public ResponseEntity<List<Responsabilidade>> validarResponsabilidades(@RequestBody Responsabilidade[] responsabilidades, @AuthenticationPrincipal Usuario usuario){
		List<Responsabilidade> responsabilidadesAux = new ArrayList<>();
		Responsabilidade responsabilidadeOriginal = null;
		for(Responsabilidade responsabilidade : responsabilidades) {
			Optional<Responsabilidade> responsabilidadeOpt = responsabilidadeService.findById(responsabilidade.getId());
			if(responsabilidadeOpt.isPresent()) {
				responsabilidadeOriginal = responsabilidadeOpt.get();
			responsabilidadeOriginal.setValidada(true);
			for(Competencia comp : responsabilidadeOriginal.getCompetenciasComissao()) 	if(!comp.isValidada()) comp.setValidada(true);
			responsabilidadeOriginal.atualizarValidacao(usuario,responsabilidade);
			responsabilidadesAux.add(responsabilidadeService.update(responsabilidadeOriginal));

			}
		}
		return ResponseEntity.ok(responsabilidadesAux);	
	}
	
	@PutMapping("/normalizacao/consolidar")
	public ResponseEntity<List<Responsabilidade>> consolidarResponsabilidades(@RequestBody Responsabilidade[] responsabilidades, @AuthenticationPrincipal Usuario usuario){
		List<Responsabilidade> responsabilidadesAux = new ArrayList<>();
		Responsabilidade responsabilidadeOriginal = null;
		for(Responsabilidade responsabilidade : responsabilidades) {
			Optional<Responsabilidade> responsabilidadeOpt = responsabilidadeService.findById(responsabilidade.getId());
			if(responsabilidadeOpt.isPresent()) {
				responsabilidadeOriginal = responsabilidadeOpt.get();
			responsabilidadeOriginal.setConsolidada(true);
			responsabilidadeOriginal.atualizarConsolidacao(usuario,responsabilidade);
			responsabilidadesAux.add(responsabilidadeService.update(responsabilidadeOriginal));
			}
		}
		return ResponseEntity.ok(responsabilidadesAux);	
	}

	@PutMapping("/normalizacao/desvalidar/{responsabilidade}")
	public ResponseEntity<Responsabilidade> desvalidarResponsabilidade(@AuthenticationPrincipal Usuario usuario, @PathVariable("responsabilidade") Responsabilidade responsabilidade){
		if(!usuario.getUnidade().hasPermissionCRUD(usuario))
			throw new GestaoCompetenciaException("Você não tem permissão para modificar esta responsabilidade");

		responsabilidade.setUsuarioChefia(null);
		responsabilidade.setTituloChefia(null);
		responsabilidade.setDificuldadeChefia(null);
		responsabilidade.setImpactoChefia(null);
		responsabilidade.setCompetenciasChefia(new ArrayList<>());
		responsabilidade.setValidada(false);
		responsabilidade.setEditada(false);

		return ResponseEntity.ok(responsabilidadeService.update(responsabilidade));
	}
	
	@PutMapping("/normalizacao/desconsolidar/{responsabilidade}")
	public ResponseEntity<Responsabilidade> desconsolidarResponsabilidade(@PathVariable Responsabilidade responsabilidade) {
		responsabilidade.setUsuarioComissao(null);
		responsabilidade.setTituloComissao(null);
		responsabilidade.setDificuldadeComissao(null);
		responsabilidade.setImpactoComissao(null);
		responsabilidade.setCompetenciasComissao(new ArrayList<>());

		responsabilidade.setUsuarioConsolidacao(null);
		responsabilidade.setTituloConsolidado(null);
		responsabilidade.setDificuldadeConsolidado(null);
		responsabilidade.setImpactoConsolidado(null);
		responsabilidade.setCompetenciasConsolidado(new ArrayList<>());
		responsabilidade.setConsolidada(false);

		if(!responsabilidade.isValidada()) {
			responsabilidade.setEditada(false);
		}

		return ResponseEntity.ok(responsabilidadeService.update(responsabilidade));
	}

	@PutMapping("/restaurar/{mapeamento}/{responsabilidade}")
	public ResponseEntity<Responsabilidade> restaurarResponsabilidadeNormalizacao(@PathVariable("responsabilidade")Responsabilidade responsabilidade){
		responsabilidade.setExcluida(false);

		return ResponseEntity.ok(responsabilidadeService.update(responsabilidade));
	}

	private void cadastroResponsabilidadeUpdate(Responsabilidade responsabilidade,
													Responsabilidade responsabilidadeAtualizada, Usuario usuario) {
		responsabilidade.setUsuarioGestor(usuario);
		responsabilidade.setTitulo(responsabilidadeAtualizada.getTitulo());
		responsabilidade.setDificuldade(responsabilidadeAtualizada.getDificuldade());
		responsabilidade.setImpacto(responsabilidadeAtualizada.getImpacto());
		responsabilidade.setCompetencias(responsabilidadeAtualizada.getCompetencias());
	}

	private void normalizacaoResponsabilidadeUpdate(Responsabilidade responsabilidade,
													Responsabilidade responsabilidadeAtualizada, Usuario usuario) {
		for(Competencia comp : responsabilidade.getCompetencias()) {
			if(!comp.isValidada())
				comp.setValidada(true);
		}
		responsabilidade.setUsuarioComissao(usuario);
		responsabilidade.setTituloComissao(responsabilidadeAtualizada.getTitulo());
		responsabilidade.setTituloChefia(null);
		responsabilidade.setTituloConsolidado(null);
		responsabilidade.setDificuldadeComissao(responsabilidadeAtualizada.getDificuldade());
		responsabilidade.setImpactoComissao(responsabilidadeAtualizada.getImpacto());
		responsabilidade.setCompetenciasComissao(responsabilidadeAtualizada.getCompetencias());
	}
	
	private void consolidacaoResponsabilidadeUpdate(Responsabilidade responsabilidade,
			Responsabilidade responsabilidadeAtualizada, Usuario usuario) {
		responsabilidade.setUsuarioConsolidacao(usuario);
		responsabilidade.setTituloConsolidado(responsabilidadeAtualizada.getTitulo());
		responsabilidade.setDificuldadeConsolidado(responsabilidadeAtualizada.getDificuldade());
		responsabilidade.setImpactoConsolidado(responsabilidadeAtualizada.getImpacto());
		responsabilidade.setCompetenciasConsolidado(responsabilidadeAtualizada.getCompetencias());
		for(Competencia comp : responsabilidade.getCompetenciasChefia()) if(!comp.isValidada()) comp.setValidada(true);
		for(Competencia comp : responsabilidade.getCompetenciasConsolidado()) if(!comp.isValidada())comp.setValidada(true);
	}

	private void validacaoResponsabilidadeUpdate(Responsabilidade responsabilidade,
			Responsabilidade responsabilidadeAtualizada, Usuario usuario) {

		responsabilidade.setUsuarioChefia(usuario);
		responsabilidade.setTituloChefia(responsabilidadeAtualizada.getTitulo());
        responsabilidade.setTituloConsolidado(null);
		responsabilidade.setDificuldadeChefia(responsabilidadeAtualizada.getDificuldade());
		responsabilidade.setImpactoChefia(responsabilidadeAtualizada.getImpacto());
		responsabilidade.setCompetenciasChefia(responsabilidadeAtualizada.getCompetencias());
		
	}

}