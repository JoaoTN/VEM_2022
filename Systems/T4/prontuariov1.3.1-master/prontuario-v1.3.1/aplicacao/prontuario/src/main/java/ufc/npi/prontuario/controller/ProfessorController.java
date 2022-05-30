package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_SERVIDOR_PAPEL;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_SERVIDOR_VAZIO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_ATUALIZAR_SERVIDOR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_SERVIDOR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_SERVIDOR;
import static ufc.npi.prontuario.util.PagesConstants.LISTAGEM_PROFESSOR;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_FORMULARIO_CADASTRAR_PROFESSOR;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_PROFESSOR;

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
import ufc.npi.prontuario.model.GetUsuarioId;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.service.ServidorService;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

	@Autowired
	private ServidorService servidorService;

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping("/cadastrar")
	public ModelAndView adicionarProfessor(Servidor professor, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_PROFESSOR);
		try {
			if(GetUsuarioId.mostrarIdUsuario(professor)!= null && professor.getPapeis().size() == 0){
				
				modelAndView.setViewName(REDIRECT_FORMULARIO_CADASTRAR_PROFESSOR);
				attributes.addFlashAttribute("servidor", professor);
				attributes.addFlashAttribute(ERROR, ERRO_ADICIONAR_SERVIDOR_PAPEL);
			}
			
			if(GetUsuarioId.mostrarIdUsuario(professor)!= null && professor.getNome().replaceAll(" ", "").length() == 0){
				modelAndView.setViewName(REDIRECT_FORMULARIO_CADASTRAR_PROFESSOR);
				attributes.addFlashAttribute("servidor", servidorService.buscarPorId(GetUsuarioId.mostrarIdUsuario(professor)));
				attributes.addFlashAttribute(ERROR, ERRO_ADICIONAR_SERVIDOR_VAZIO);
			}
			
			if(GetUsuarioId.mostrarIdUsuario(professor) == null) {
				servidorService.salvar(professor);
				attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_SERVIDOR);
			} else {
				servidorService.atualizar(professor);
				attributes.addFlashAttribute(SUCCESS, SUCCESS_ATUALIZAR_SERVIDOR);
			}
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR,e.getMessage());
			modelAndView.setViewName(REDIRECT_FORMULARIO_CADASTRAR_PROFESSOR);

		}
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/listar")
	public ModelAndView listarProfessores() {
		ModelAndView modelAndView = new ModelAndView(LISTAGEM_PROFESSOR);
		modelAndView.addObject("professores", servidorService.buscarProfessores());
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/remover/{id}")
	public ModelAndView excluirServidor(@PathVariable("id") Servidor servidor, RedirectAttributes attributes) {
		
		try {
			servidorService.removerServidor(GetUsuarioId.mostrarIdUsuario(servidor));
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_SERVIDOR);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		
		return new ModelAndView(REDIRECT_LISTAGEM_PROFESSOR);
	}

}
