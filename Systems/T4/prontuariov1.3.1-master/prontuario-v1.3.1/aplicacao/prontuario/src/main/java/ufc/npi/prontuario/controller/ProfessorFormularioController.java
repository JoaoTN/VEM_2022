package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO_VERIFICACAO_ID_PROFESSOR;
import static ufc.npi.prontuario.util.PagesConstants.DETALHES_PROFESSOR;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_CADASTRAR_PROFESSOR;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.model.Papel;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.service.AtendimentoService;
import ufc.npi.prontuario.service.TurmaService;

@Controller
@RequestMapping("/professor")
public class ProfessorFormularioController {
	
	@Autowired
	private TurmaService turmaService;

	@Autowired
	private AtendimentoService atendimentoService;
	
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping("/cadastrar")
	public ModelAndView formAdicionarProfessor(Servidor servidor) {
		servidor.addPapel(Papel.PROFESSOR);
		return new ModelAndView(FORMULARIO_CADASTRAR_PROFESSOR).addObject("servidor", servidor)
				.addObject("papeis", Arrays.asList(Papel.PROFESSOR, Papel.ADMINISTRACAO, Papel.ATENDENTE));
	}
	
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping("/editar/{id}")
	public ModelAndView formEditarProfessor(@PathVariable("id") Servidor professor, RedirectAttributes attributes) {
		return new ModelAndView(FORMULARIO_CADASTRAR_PROFESSOR).addObject("servidor", professor)
				.addObject("papeis", Arrays.asList(Papel.PROFESSOR, Papel.ADMINISTRACAO, Papel.ATENDENTE));
	}
	
	@PreAuthorize(PERMISSAO_ADMINISTRACAO_VERIFICACAO_ID_PROFESSOR)
	@GetMapping("/detalhes/{id}")
	public ModelAndView detalhes(@PathVariable("id") Servidor professor, Authentication authentication) {
		ModelAndView mv = new ModelAndView(DETALHES_PROFESSOR);
		mv.addObject("professor", professor);
		mv.addObject("turmas", turmaService.buscarTurmasProfessor(professor));

		if (professor.equals((Servidor) authentication.getPrincipal())) {
			mv.addObject("atendimentos", atendimentoService.buscarAtendimentosNaoFinalizadosPorProfessor(professor));
		}

		return mv;
	}
}
