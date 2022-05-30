package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE_VERIFICACAO_ESTUDANTE;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_PROFESSOR_VERIFICACAO_PROFESSOR;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ESTUDANTE_PROFESSOR;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_AVALIAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EDITAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_PATOLOGIA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_PROCEDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_FINALIZAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_VALIDAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_ADICIONAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHES_ATENDIMENTO;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_DETALHES_ATENDIMENTO;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_INDEX;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_ODONTOGRAMA;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Atendimento.Status;
import ufc.npi.prontuario.model.AvaliacaoAtendimento;
import ufc.npi.prontuario.model.GetAtendimentoId;
import ufc.npi.prontuario.model.GetPacienteId;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Papel;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.service.AlunoService;
import ufc.npi.prontuario.service.AtendimentoService;
import ufc.npi.prontuario.service.PatologiaService;
import ufc.npi.prontuario.service.ProcedimentoService;
import ufc.npi.prontuario.service.TurmaService;

@Controller
@RequestMapping("/atendimento")
public class AtendimentoController {

	@Autowired
	private AtendimentoService atendimentoService;

//	@Autowired
//	private AlunoService alunoService;
//
//	@Autowired
//	private TurmaService turmaService;

//	@Autowired
//	private PatologiaService patologiaService;

//	@Autowired
//	private ProcedimentoService procedimentoService;

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idAtendimento}")
	public ModelAndView getDetalhesAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
			Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHES_ATENDIMENTO);
		atendimentoService.adicionarAvaliacaoAtendimento(atendimento);
		if (atendimento.isVisivel(auth.getName()) || auth.getAuthorities().contains(Papel.ADMINISTRACAO)) {
			modelAndView.addObject("atendimento", atendimento);
			modelAndView.addObject("paciente", atendimento.getPaciente());
			modelAndView.addObject("avaliacaoAtendimento", atendimento.getAvaliacao());
		} else {
			modelAndView.setViewName(REDIRECT_INDEX);
		}

		return modelAndView;
	}
	
//	@PreAuthorize(PERMISSAO_ESTUDANTE)
//	@GetMapping("/{id}/cadastrar")
//	public ModelAndView formularioCadastrarAtendimento(@PathVariable("id") Paciente paciente, Authentication auth) {
//		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_ATENDIMENTO);
//		Aluno aluno = (Aluno) auth.getPrincipal();
//		modelAndView.addObject("atendimento", new Atendimento());
//		modelAndView.addObject("turmas", turmaService.buscarAtivasPorAluno(aluno));
//		modelAndView.addObject("paciente", paciente);
//		modelAndView.addObject("action", "cadastrar");
//		return modelAndView;
//	}
	
	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/cadastrar")
	public ModelAndView cadastrarAtendimento(Atendimento atendimento, @RequestParam("paciente") Paciente paciente,
			RedirectAttributes attributes) {
		atendimento.setPaciente(paciente);
		ModelAndView modelAndView = new ModelAndView("redirect:/paciente/" + GetPacienteId.mostrarIdPaciente(paciente) + "/atendimentos");
		try {
			atendimentoService.salvar(atendimento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_ATENDIMENTO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		return modelAndView;
	}
	
//	@PreAuthorize(PERMISSAO_ESTUDANTE)
//	@GetMapping("/editar/{idAtendimento}")
//	public ModelAndView formularioEditarAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
//			Authentication auth) {
//		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_ATENDIMENTO);
//		Aluno aluno = (Aluno) auth.getPrincipal();
//		modelAndView.addObject("atendimento", atendimento);
//		modelAndView.addObject("turmas", turmaService.buscarAtivasPorAluno(aluno));
//		modelAndView.addObject("paciente", atendimento.getPaciente());
//		modelAndView.addObject("professor", atendimento.getProfessor());
//		modelAndView.addObject("professores", atendimento.getTurma().getProfessores());
//		modelAndView.addObject("auxiliar", atendimento.getAjudante());
//		modelAndView.addObject("ajudantes", alunoService.buscarAjudantes(atendimento.getTurma().getId(), aluno));
//		modelAndView.addObject("action", "editar");
//		return modelAndView;
//	}

	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/editar")
	public ModelAndView editarAtendimento(Atendimento atendimento, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView("redirect:/atendimento/" + GetAtendimentoId.getIdAtendimento(atendimento));
		try {
			atendimentoService.atualizar(atendimento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EDITAR_ATENDIMENTO);
		} catch (ProntuarioException e) {
			modelAndView.addObject("atendimento", atendimento);
			modelAndView.addObject("paciente", atendimento.getPaciente());
			modelAndView.addObject("action", "editar");
			modelAndView.setViewName(FORMULARIO_ADICIONAR_ATENDIMENTO);
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ESTUDANTE_VERIFICACAO_ESTUDANTE)
	@GetMapping("/excluir/{id}")
	public ModelAndView removerAtendimento(@PathVariable("id") @Param("atendimento") Atendimento atendimento, 
			Authentication auth, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView("redirect:/paciente/" + atendimento.getPaciente().getId() + "/atendimentos");
		if (!atendimento.getStatus().equals(Status.EM_ANDAMENTO)) {
			attributes.addFlashAttribute(ERROR, ERRO_EXCLUIR_ATENDIMENTO);
		} else {
			atendimentoService.remover(atendimento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_ATENDIMENTO);
		}
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR)
	@PostMapping("/avaliar/{idAtendimento}")
	public ModelAndView avaliarAtendimento(@PathVariable("idAtendimento") Atendimento atendimento, AvaliacaoAtendimento avaliacaoAtendimento, Authentication auth, RedirectAttributes attributes) {
		atendimento.getAvaliacao().setObservacao(avaliacaoAtendimento.getObservacao());
		atendimento.getAvaliacao().setItens(avaliacaoAtendimento.getItens());
		try {
			atendimentoService.atualizar(atendimento);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		ModelAndView modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + GetAtendimentoId.getIdAtendimento(atendimento));
		attributes.addFlashAttribute(SUCCESS, SUCCESS_AVALIAR_ATENDIMENTO);
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO)
	@GetMapping({ "/finalizar/{id}", "/{id}/finalizar/odontograma" })
	public ModelAndView finalizarAtendimento(@PathVariable("id") Atendimento atendimento, RedirectAttributes attributes,
			HttpServletRequest request) {
		ModelAndView modelAndView = null;
		atendimentoService.finalizarAtendimento(atendimento);
		if (request.getRequestURI().contains("odontograma")) {
			modelAndView = new ModelAndView(REDIRECT_ODONTOGRAMA + atendimento.getPaciente().getId());
		} else {
			modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + GetAtendimentoId.getIdAtendimento(atendimento));
			modelAndView.addObject("atendimento", atendimento);
		}
		attributes.addFlashAttribute(SUCCESS, SUCCESS_FINALIZAR_ATENDIMENTO);
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_PROFESSOR_VERIFICACAO_PROFESSOR)
	@GetMapping("/validar/{id}")
	public ModelAndView validarAtendimento(@PathVariable("id") Atendimento atendimento, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + GetAtendimentoId.getIdAtendimento(atendimento));
		atendimentoService.validarAtendimento(atendimento);
		attributes.addFlashAttribute(SUCCESS, SUCCESS_VALIDAR_ATENDIMENTO);
		return modelAndView;
	}
	
//	@PreAuthorize(PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO)
//	@GetMapping("/{idAtendimento}/excluir-procedimento/{idProcedimento}")
//	public ModelAndView removerProcedimento(@PathVariable("idProcedimento") Procedimento procedimento,
//			@PathVariable("idAtendimento") @Param("atendimento") Atendimento atendimento, Authentication auth,
//			RedirectAttributes attributes) {
//		ModelAndView modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + GetAtendimentoId.getIdAtendimento(atendimento));
//		procedimentoService.deletar(procedimento);
//		attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_PROCEDIMENTO);
//		return modelAndView;
//	}

//	@PreAuthorize(PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO)
//	@GetMapping("/{idAtendimento}/excluir-patologia/{idPatologia}")
//	public ModelAndView removerPatologia(@PathVariable("idPatologia") Patologia patologia,
//			@PathVariable("idAtendimento") @Param("atendimento") Atendimento atendimento, Authentication auth,
//			RedirectAttributes attributes) {
//		ModelAndView modelAndView = new ModelAndView(REDIRECT_DETALHES_ATENDIMENTO + GetAtendimentoId.getIdAtendimento(atendimento));
//		patologiaService.deletar(patologia);
//		attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_PATOLOGIA);
//		return modelAndView;
//	}

	/*@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/tabela-itens/{idAtendimento}")
	public ModelAndView getTableItensAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
			Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(FRAGMENT_TABELA_ITENS_AVALIACAO);

		Atendimento old = atendimentoService.adicionarAvaliacaoAtendimento(atendimento);

		Avaliacao avaliacao = avaliacaoService.getAvaliacaoAtiva();

		if (atendimento.isVisivel(auth.getName())  || auth.getAuthorities().contains(Papel.ADMINISTRACAO)) {
			modelAndView.addObject("atendimento", old);
			modelAndView.addObject("paciente", atendimento.getPaciente());
			modelAndView.addObject("avaliacao", avaliacaoService.getAvaliacaoAtiva());
			modelAndView.addObject("itens", avaliacao.itensNaoAvaliados(atendimento));
			modelAndView.addObject("itensAvaliados", old.getAvaliacao().getItens());
		} else {
			modelAndView.setViewName(REDIRECT_INDEX);
		}

		return modelAndView;
	}*/
	
	

	/*@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR)
	@GetMapping("/avaliar-item/{idAtendimento}")
	public ModelAndView avaliarItemAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
			Authentication auth, @Param(value = "item") Integer item, @Param(value = "nota") String nota,
			@Param(value = "avaliacao") Integer avaliacao) {

		ModelAndView modelAndView = new ModelAndView(FRAGMENT_TABELA_ITENS_AVALIACAO);

		Atendimento old = atendimentoService.adicionarItemAvaliacaoAtendimento(item, atendimento, nota, avaliacao);

		Avaliacao avaliacaoOld = avaliacaoService.getAvaliacaoAtiva();

		if (atendimento.isVisivel(auth.getName())) {
			modelAndView.addObject("atendimento", old);
			modelAndView.addObject("paciente", atendimento.getPaciente());
			modelAndView.addObject("avaliacao", avaliacaoService.getAvaliacaoAtiva());
			modelAndView.addObject("itens", avaliacaoOld.itensNaoAvaliados(atendimento));
			modelAndView.addObject("itensAvaliados", old.getAvaliacao().getItens());
		} else {
			modelAndView.setViewName(REDIRECT_INDEX);
		}

		return modelAndView;
	}*/

	/*@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR)
	@GetMapping("/reavaliar-item/{idAtendimento}")
	public ModelAndView reavaliarItemAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
			Authentication auth, @Param(value = "nota") String nota,
			@Param(value = "itemAvaliacao") Integer itemAvaliacao) {

		ModelAndView modelAndView = new ModelAndView(FRAGMENT_TABELA_ITENS_AVALIACAO);

		Atendimento old = atendimentoService.reavaliarItem(atendimento, nota, itemAvaliacao);

		Avaliacao avaliacaoOld = avaliacaoService.getAvaliacaoAtiva();

		if (atendimento.isVisivel(auth.getName())) {
			modelAndView.addObject("reavaliado", "reavaliado");
			modelAndView.addObject("atendimento", old);
			modelAndView.addObject("paciente", atendimento.getPaciente());
			modelAndView.addObject("avaliacao", avaliacaoService.getAvaliacaoAtiva());
			modelAndView.addObject("itens", avaliacaoOld.itensNaoAvaliados(atendimento));
			modelAndView.addObject("itensAvaliados", old.getAvaliacao().getItens());
		} else {
			modelAndView.setViewName(REDIRECT_INDEX);
		}

		return modelAndView;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR)
	@GetMapping("/adicionar-observacao/{idAtendimento}")
	public ModelAndView adicionarObservacaoAvaliacaoAtendimento(@PathVariable("idAtendimento") Atendimento atendimento,
			Authentication auth, @Param(value = "observacao") String observacao) {

		ModelAndView modelAndView = new ModelAndView(FRAGMENT_TABELA_ITENS_AVALIACAO);

		Atendimento old = atendimentoService.adicionarObservacao(atendimento, observacao);

		Avaliacao avaliacaoOld = avaliacaoService.getAvaliacaoAtiva();

		if (atendimento.isVisivel(auth.getName())) {
			modelAndView.addObject("atendimento", old);
			modelAndView.addObject("paciente", atendimento.getPaciente());
			modelAndView.addObject("avaliacao", avaliacaoService.getAvaliacaoAtiva());
			modelAndView.addObject("itens", avaliacaoOld.itensNaoAvaliados(atendimento));
			modelAndView.addObject("itensAvaliados", old.getAvaliacao().getItens());
		} else {
			modelAndView.setViewName(REDIRECT_INDEX);
		}

		return modelAndView;
	}*/
}
