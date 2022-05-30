package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_PACIENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EDITAR_PACIENTE;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_CADASTRO_PACIENTE;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_PACIENTES;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_PACIENTES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.service.PacienteFormularioService;
import ufc.npi.prontuario.service.PacienteService;

@Controller
@RequestMapping("/paciente")
public class PacienteListController {
	
	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private PacienteFormularioService formularioService;
	
	@GetMapping
	public ModelAndView listagemPacientes() {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_PACIENTES);
		modelAndView.addObject("pacientes", pacienteService.buscarTudo());
		return modelAndView;
	}
	
	@PostMapping("/editar")
	public ModelAndView editarPaciente(Paciente paciente, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(REDIRECT_LISTAGEM_PACIENTES);
		
		try {
			pacienteService.salvar(paciente);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EDITAR_PACIENTE);
		} catch (ProntuarioException e) {
			mv.addAllObjects(formularioService.formularioEdicao());
			addErrorModel(mv, e);;
		}
		
		return mv;
	}

	@PostMapping("/cadastrar")
	public ModelAndView cadastrarPaciente(Paciente paciente, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(REDIRECT_LISTAGEM_PACIENTES);
		
		try {
			pacienteService.salvar(paciente);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_PACIENTE);
		} catch (ProntuarioException e) {
			mv.addAllObjects(formularioService.formularioCadastro());
			addErrorModel(mv, e);;
		}
		
		return mv;
	}

	private void addErrorModel(ModelAndView mv, ProntuarioException e) {
		mv.addObject(ERROR, e.getMessage());
		mv.setViewName(FORMULARIO_CADASTRO_PACIENTE);
	}	
}
