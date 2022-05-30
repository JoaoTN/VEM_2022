package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_TIPO_PROCEDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_TIPO_PROCEDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_SALVAR_TIPO_PROCEDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EDITAR_TIPO_PROCEDIMENTO;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_ADICIONAR_TIPOS_PROCEDIMENTO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_TIPOS_PROCEDIMENTO;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_TIPOS_PROCEDIMENTO;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.TipoProcedimento;
import ufc.npi.prontuario.service.TipoProcedimentoService;

@Controller
@RequestMapping("/tipo-procedimento")
public class TipoProcedimentoController {

	@Autowired
	private TipoProcedimentoService tipoProcedimentoService;


	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/adicionar")
	public ModelAndView formularioAdicionarTipoProcedimento(TipoProcedimento procedimento) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_TIPOS_PROCEDIMENTO);
		modelAndView.addObject("procedimento", procedimento);
		return modelAndView;
	}
/////////////////////////////////////////////////////////////////////////////////////////////
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping(value = "/adicionar")
	public ModelAndView adicionarTipoProcedimento(
			@Valid @ModelAttribute("tipoProcedimento") TipoProcedimento tipoProcedimento,
			BindingResult result, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_TIPOS_PROCEDIMENTO);
		if(!result.hasErrors()){
			try {
				if(ProcedimentoIdNull(tipoProcedimento)) {
					tipoProcedimentoService.salvar(tipoProcedimento);
					addFlashAttribute(attributes, SUCCESS, SUCCESS_SALVAR_TIPO_PROCEDIMENTO);
				} else {
					tipoProcedimentoService.atualizar(tipoProcedimento);
					addFlashAttribute(attributes ,SUCCESS, SUCCESS_EDITAR_TIPO_PROCEDIMENTO);
				}
			} catch (ProntuarioException e) {
				modelAndView.addObject("tiposProcedimento", tipoProcedimentoService.buscarTudo());
				modelAndView.addObject(ERROR, e.getMessage());
				modelAndView.setViewName(PAGINA_LISTAGEM_TIPOS_PROCEDIMENTO);
			}
		}
		return modelAndView;
	}
	
	public boolean ProcedimentoIdNull(TipoProcedimento tipoProcedimento) {
		return tipoProcedimento.getId() == null;
	}
	
	public void addFlashAttribute(RedirectAttributes attributes, String option1, String option2) {
		attributes.addFlashAttribute(option1, option2);
	}
////////////////////////////////////////////////////////////////////////////////////////////////
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/remover/{id}")
	public ModelAndView excluirTipoProcedimento(@PathVariable("id") TipoProcedimento tipoProcedimento, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_TIPOS_PROCEDIMENTO);
		try {
			tipoProcedimentoService.remover(tipoProcedimento.getId());
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_TIPO_PROCEDIMENTO);
		} catch (Exception e) {
			attributes.addFlashAttribute(ERROR, ERRO_EXCLUIR_TIPO_PROCEDIMENTO);
		}
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/editar/{id}")
	public ModelAndView editarTipoProcedimento(
			@PathVariable("id") TipoProcedimento tipoProcedimento
	) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_TIPOS_PROCEDIMENTO);
		modelAndView.addObject("tipoProcedimento", tipoProcedimento);
		return modelAndView;
	}

	@GetMapping(value = "/listar")
	public ModelAndView listarTiposProcedimento() {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_TIPOS_PROCEDIMENTO);
		modelAndView.addObject("tipoProcedimento", new TipoProcedimento());
		modelAndView.addObject("tiposProcedimento", tipoProcedimentoService.buscarTudo());
		return modelAndView;
	}
}
