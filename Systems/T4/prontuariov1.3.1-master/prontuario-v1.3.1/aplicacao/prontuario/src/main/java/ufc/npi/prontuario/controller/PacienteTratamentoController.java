package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ATENDENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_TRATAMENTO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_TRATAMENTOS;
import static ufc.npi.prontuario.util.PagesConstants.TABLE_LISTAGEM_TRATAMENTOS;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_INDEX;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.GetPacienteId;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PlanoTratamento;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.service.PlanoTratamentoService;

@Controller
@RequestMapping("/paciente")
public class PacienteTratamentoController {
	
	@Autowired
	private PlanoTratamentoService tratamentoService;
	
	@GetMapping("/{idPaciente}/tratamentos")
	public ModelAndView listarTratamentos(@PathVariable("idPaciente") Paciente paciente) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_TRATAMENTOS);
		modelAndView.addObject("paciente", paciente);
		modelAndView.addObject("tratamentos", paciente.getTratamentos());
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ATENDENTE)
	@PostMapping("/plano-tratamento/cadastrar")
	public ModelAndView cadastrar(PlanoTratamento planoTratamento, @RequestParam("paciente") Paciente paciente,
			@RequestParam("responsavel") Servidor servidorResponsavel, RedirectAttributes attributes) {
		String tratamentosViewName = REDIRECT_INDEX + "paciente/" + GetPacienteId.mostrarIdPaciente(paciente) + "/tratamentos";
		ModelAndView mv = new ModelAndView(tratamentosViewName);
		salvarPlanoTratamento(planoTratamento, paciente, servidorResponsavel, attributes);
		
		return mv;
	}
	
	@PreAuthorize(PERMISSAO_ATENDENTE)
	@PostMapping("/plano-tratamento/{idPaciente}/excluir/{tratamento}")
	public ModelAndView excluir(@PathVariable("tratamento") PlanoTratamento planoTratamento,
			@PathVariable("idPaciente") Paciente paciente, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(TABLE_LISTAGEM_TRATAMENTOS);

		try {
			tratamentoService.excluirPlanoTratamento(planoTratamento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_TRATAMENTO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}

		mv.addObject("paciente", paciente);
		mv.addObject("tratamentos", paciente.getTratamentos());

		return mv;
	}
	
	private void salvarPlanoTratamento(PlanoTratamento planoTratamento, Paciente paciente, Servidor servidorResponsavel, RedirectAttributes attributes) {
		try {
			tratamentoService.salvar(planoTratamento, servidorResponsavel, paciente);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_TRATAMENTO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
	}
}
