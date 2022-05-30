package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ATENDENTE;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_CADASTRO_PLANO_TRATAMENTO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHES_PACIENTE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PlanoTratamento;
import ufc.npi.prontuario.service.DisciplinaService;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

	@Autowired
	private DisciplinaService disciplinaService;
	
	@GetMapping("/{idPaciente}")
	public ModelAndView visualizarDetalhes(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_DETALHES_PACIENTE);
		modelAndView.addObject("paciente", paciente);
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ATENDENTE)
	@GetMapping("/plano-tratamento/{idPaciente}/cadastrar")
	public ModelAndView paginaCadastro(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView mv = new ModelAndView(PAGINA_CADASTRO_PLANO_TRATAMENTO);
		mv.addObject("paciente", paciente);
		mv.addObject("clinicas", disciplinaService.buscarTudo());
		mv.addObject("tratamento", new PlanoTratamento());
		return mv;
	}
	
}
