package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_PROFESSOR_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_DISCIPLINA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EDITAR_DISCIPLINA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_DISCIPLINA;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_ADICIONAR_DISCIPLINA;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHE_DISCIPLINA;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_DISCIPLINA;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_DISCIPLINAS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.service.DisciplinaService;

@Controller
@RequestMapping("/disciplina")
public class DisciplinaController {

	@Autowired
	private DisciplinaService disciplinaService;

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/adicionar")
	public ModelAndView formularioAdicionarDisciplina(Disciplina disciplina) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_DISCIPLINA);
		modelAndView.addObject("disciplina", disciplina);
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping(value = "/adicionar")
	public ModelAndView adicionarDisciplina(Disciplina disciplina, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_DISCIPLINAS);
		try {
			if(disciplina.getId() == null) {
				disciplinaService.salvar(disciplina);
				attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_DISCIPLINA);
			} else {
				disciplinaService.atualizar(disciplina);
				attributes.addFlashAttribute(SUCCESS, SUCCESS_EDITAR_DISCIPLINA);
			}
		} catch (ProntuarioException e) {
			modelAndView.setViewName(FORMULARIO_ADICIONAR_DISCIPLINA);
			modelAndView.addObject(ERROR, e.getMessage());
		}

		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/editar/{idDisciplina}")
	public ModelAndView formularioEditarDisciplina(@PathVariable("idDisciplina") Disciplina disciplina){
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_DISCIPLINA);
		modelAndView.addObject("disciplina", disciplina);
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/remover/{idDisciplina}")
	public ModelAndView excluirDisciplina(@PathVariable("idDisciplina") Disciplina disciplina, 
			RedirectAttributes attributes) {
		
		try {
			disciplinaService.removerDisciplina(disciplina.getId());
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_DISCIPLINA);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		
		return new ModelAndView(REDIRECT_LISTAGEM_DISCIPLINAS);
	}
	

	@PreAuthorize(PERMISSOES_PROFESSOR_ADMINISTRACAO)
	@GetMapping(value = "/detalhes/{id}")
	public ModelAndView detalhesDisciplina(@PathVariable("id") Disciplina disciplina) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHE_DISCIPLINA);
		modelAndView.addObject("disciplina", disciplina);
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSOES_PROFESSOR_ADMINISTRACAO)
	@GetMapping(value = "/listar")
	public ModelAndView listagemDisciplina() {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_DISCIPLINA);
		modelAndView.addObject("disciplinas", disciplinaService.buscarTudo());
		return modelAndView;
	}
}
