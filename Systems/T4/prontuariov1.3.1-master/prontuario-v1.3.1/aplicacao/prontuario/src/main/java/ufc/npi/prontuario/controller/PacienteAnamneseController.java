package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ESTUDANTE;
import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_REALIZAR_ANAMNESE;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_REALIZAR_ANAMNESE;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHES_ANAMNESE_PACIENTE;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_ANAMNESES_PACIENTE;

import org.springframework.beans.factory.annotation.Autowired;
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

import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.GetMatriculaUsuario;
import ufc.npi.prontuario.model.GetPacienteId;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PacienteAnamnese;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.service.AlunoService;
import ufc.npi.prontuario.service.AnamneseService;
import ufc.npi.prontuario.service.PacienteService;

@Controller
@RequestMapping("/paciente")
public class PacienteAnamneseController {
	
	@Autowired
	private AlunoService alunoService;
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private AnamneseService anamneseService;
	
	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idPaciente}/anamneses")
	public ModelAndView listarAnamneses(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_ANAMNESES_PACIENTE);
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("anamneses", anamneseService.buscarTodasFinalizadas());
		modelAndView.addObject("pacienteAnamneses", paciente.getPacienteAnamneses());
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@GetMapping("/{idPaciente}/anamnese")
	public ModelAndView realizarAnamneseForm(@PathVariable("idPaciente") Paciente paciente,
			@RequestParam("idAnamnese") Anamnese anamnese, PacienteAnamnese pacienteAnamnese) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_REALIZAR_ANAMNESE);

		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("anamnese", anamnese);
		modelAndView.addObject("pacienteAnamnese", pacienteAnamnese);

		return modelAndView;
	}

	@PreAuthorize(PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO)
	@GetMapping("/{idPaciente}/anamnese/{idAnamnese}")
	public ModelAndView visualizarDetalhesAnamnese(@PathVariable("idPaciente") Paciente paciente,
			@PathVariable("idAnamnese") PacienteAnamnese anamnese) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHES_ANAMNESE_PACIENTE);
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("anamnese", anamnese);
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ESTUDANTE)
	@PostMapping("/{idPaciente}/anamnese")
	public ModelAndView realizarAnamnese(@PathVariable("idPaciente") Paciente paciente,
			PacienteAnamnese pacienteAnamnese, Authentication auth, RedirectAttributes attributes) {

		Usuario usuario = (Usuario) auth.getPrincipal();
		
		pacienteAnamnese.setResponsavel(alunoService.buscarPorMatricula(GetMatriculaUsuario.getUsuarioMatricula(usuario)));
		pacienteService.adicionarAnamnese(paciente, pacienteAnamnese);

		attributes.addFlashAttribute(SUCCESS, SUCCESS_REALIZAR_ANAMNESE);

		return new ModelAndView("redirect:/paciente/" + GetPacienteId.mostrarIdPaciente(paciente) + "/anamneses");
	}
}
