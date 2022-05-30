package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ATENDENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EDITAR_TRATAMENTO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_EDITAR_PLANO_TRATAMENTO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_PACIENTES_AGUARDANDO_TRATAMENTO;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_INDEX;

import java.util.Arrays;
import java.util.List;

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
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.GetPacienteId;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PlanoTratamento;
import ufc.npi.prontuario.model.PlanoTratamento.Status;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.service.DisciplinaService;
import ufc.npi.prontuario.service.PlanoTratamentoService;

@Controller
@RequestMapping("/plano-tratamento")
public class PlanoTratamentoController {

	@Autowired
	private DisciplinaService disciplinaService;

	@Autowired
	private PlanoTratamentoService tratamentoService;

	@GetMapping("/aguardando-tratamento")
	public ModelAndView parcientesAguardandoTratamento() {
		ModelAndView modelAndView = new ModelAndView(PAGINA_PACIENTES_AGUARDANDO_TRATAMENTO);
		modelAndView.addObject("clinicas", disciplinaService.buscarTudo());
		modelAndView.addObject("status", Status.values());
		modelAndView.addObject("busca", false);
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ATENDENTE)
	@PostMapping("/cadastrar")
	public ModelAndView cadastrar(PlanoTratamento planoTratamento, @RequestParam("paciente") Paciente paciente,
			@RequestParam("responsavel") Servidor responsavel, RedirectAttributes attributes) {
		final String redirectTratamentos = REDIRECT_INDEX + "paciente/" + GetPacienteId.mostrarIdPaciente(paciente) + "/tratamentos";
		ModelAndView mv = new ModelAndView(redirectTratamentos);
		
		try {
			tratamentoService.salvar(planoTratamento, responsavel, paciente);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_TRATAMENTO);
		} catch(ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		
		return mv;
	}

	@PostMapping("/aguardando-tratamento")
	public ModelAndView visualizarParcientesAguardandoTratamento(@RequestParam("clinica") Disciplina disciplina,
			@RequestParam(value = "status", required = false) String status) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_PACIENTES_AGUARDANDO_TRATAMENTO);
		modelAndView.addObject("tratamentos",
				tratamentoService.buscarPlanoTratamentoPorClinicaEStatus(disciplina, status));
		modelAndView.addObject("clinicas", disciplinaService.buscarTudo());
		modelAndView.addObject("status", Status.values());
		modelAndView.addObject("busca", true);
		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ATENDENTE)
	@GetMapping("/pagina-editar/{tratamento}")
	public ModelAndView finalizar(@PathVariable("tratamento") PlanoTratamento planoTratamento,
			RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(PAGINA_EDITAR_PLANO_TRATAMENTO);

		List<Status> statuses = Arrays.asList(Status.EM_ESPERA, Status.EM_ANDAMENTO, 
				Status.CONCLUIDO, Status.INTERROMPIDO);
		mv.addObject("statuses", statuses);
		mv.addObject("paciente", planoTratamento.getPaciente());
		mv.addObject("tratamento", planoTratamento);

		return mv;
	}
	
	@PreAuthorize(PERMISSAO_ATENDENTE)
	@PostMapping("/editar")
	public ModelAndView editar(PlanoTratamento planoTratamento, RedirectAttributes attributes) {
		final String redirectTratamentos = REDIRECT_INDEX + "paciente/" 
					+ planoTratamento.getPaciente().getId() + "/tratamentos";
		ModelAndView mv = new ModelAndView(redirectTratamentos);
		
		try{
			tratamentoService.editar(planoTratamento);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EDITAR_TRATAMENTO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		
		return mv;
	}

}
