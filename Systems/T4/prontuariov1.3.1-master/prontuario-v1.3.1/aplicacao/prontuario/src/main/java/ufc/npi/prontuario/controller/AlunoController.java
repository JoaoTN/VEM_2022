package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_PROFESSOR_ADMINISTRACAO_VERIFICACAO_ESTUDANTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_ATUALIZAR_ALUNO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_ALUNO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_ALUNO;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_ADICIONAR_ALUNO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHES_ALUNO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_ALUNOS;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_ALUNOS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.GetUsuarioId;
import ufc.npi.prontuario.service.AlunoService;
import ufc.npi.prontuario.service.AtendimentoService;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

	@Autowired
	private AlunoService alunoService;

	@Autowired
	private AtendimentoService atendimentoService;

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/adicionar")
	public ModelAndView formularioAdicionarAluno(Aluno aluno) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_ALUNO);
		modelAndView.addObject("aluno", aluno);
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping(value = "/adicionar")
	public ModelAndView adicionarAluno(Aluno aluno, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_ALUNOS);

		try {
			salvarOuAtualizarAluno(aluno, attributes);
		} catch (ProntuarioException e) {
			modelAndView.setViewName(FORMULARIO_ADICIONAR_ALUNO);
			modelAndView.addObject(ERROR, e.getMessage());
		}

		return modelAndView;
	}

	private void salvarOuAtualizarAluno(Aluno aluno, RedirectAttributes attributes) throws ProntuarioException {
		if (GetUsuarioId.mostrarIdUsuario(aluno) == null) {
			alunoService.salvar(aluno);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_ALUNO);
		} else {
			alunoService.atualizar(aluno);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_ATUALIZAR_ALUNO);
		}
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/remover/{idAluno}")
	public ModelAndView excluirAluno(@PathVariable("idAluno") Aluno aluno, RedirectAttributes attributes) {
		try {
			alunoService.removerAluno(GetUsuarioId.mostrarIdUsuario(aluno));
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_ALUNO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}

		return new ModelAndView(REDIRECT_LISTAGEM_ALUNOS);
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping("/editar/{idAluno}")
	public ModelAndView editarAluno(@PathVariable("idAluno") Aluno aluno) {
		return formularioAdicionarAluno(aluno);
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/listar")
	public ModelAndView listarAlunos() {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_ALUNOS);
		modelAndView.addObject("alunos", alunoService.buscarTudo());
		return modelAndView;
	}

	@PostAuthorize(PERMISSOES_PROFESSOR_ADMINISTRACAO_VERIFICACAO_ESTUDANTE)
	@GetMapping(value = "/detalhes/{alunoId}")
	public ModelAndView visualizarDetalhesAluno(@PathVariable("alunoId") Aluno aluno) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHES_ALUNO);
		modelAndView.addObject("atendimentos", atendimentoService.buscarTudoPorAluno(aluno));
		modelAndView.addObject("aluno", aluno);
		return modelAndView;
	}
}