package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_CADASTRO_PACIENTE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.service.PacienteFormularioService;

@Controller
@RequestMapping("/paciente")
public class PacienteFormularioController {
	
	@Autowired
	private PacienteFormularioService formularioService;
	
	@GetMapping("/cadastrar")
	public ModelAndView paginaCadastroPaciente(Paciente paciente) {
		ModelAndView mv = new ModelAndView(FORMULARIO_CADASTRO_PACIENTE);
		mv.addAllObjects(formularioService.formularioCadastro());
		return mv;
	}
	
	@GetMapping("/editar/{idPaciente}")
	public ModelAndView editarPaciente(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView mv = new ModelAndView(FORMULARIO_CADASTRO_PACIENTE);
		mv.addObject("paciente", paciente);
		mv.addAllObjects(formularioService.formularioEdicao());
		return mv;
	}
	
}
