package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_ATENDIMENTOS;
import static ufc.npi.prontuario.util.PagesConstants.TABLE_ATENDIMENTOS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.prontuario.model.GetUsuarioId;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.service.AtendimentoService;

@Controller
@RequestMapping("/paciente")
public class PacienteAtendimentoController {
	
	@Autowired
	private AtendimentoService atendimentoService;
	
	public Integer mostrarIdUsuario(Usuario usuario) {
		return GetUsuarioId.mostrarIdUsuario(usuario);
	}
	
	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idPaciente}/atendimentos")
	public ModelAndView listarAtendimentos(@PathVariable("idPaciente") Paciente paciente, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_ATENDIMENTOS);
		Usuario usuario = (Usuario) auth.getPrincipal();
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("atendimentos",
				atendimentoService.buscarAtendimentosPorUsuario(mostrarIdUsuario(usuario), paciente));
		return modelAndView;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idPaciente}/tableAtendimentos")
	public ModelAndView getTableAtendimentos(@PathVariable("idPaciente") Paciente paciente, Authentication auth) {
		ModelAndView modelAndView = new ModelAndView(TABLE_ATENDIMENTOS);
		Usuario usuario = (Usuario) auth.getPrincipal();
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("atendimentos",
				atendimentoService.buscarAtendimentosPorUsuario(mostrarIdUsuario(usuario), paciente));
		return modelAndView;
	}
}
