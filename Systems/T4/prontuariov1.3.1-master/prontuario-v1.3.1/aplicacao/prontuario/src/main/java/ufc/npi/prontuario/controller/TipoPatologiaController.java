package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_TIPO_PATOLOGIA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EDITAR_TIPO_PATOLOGIA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_TIPO_PATOLOGIA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_SALVAR_TIPO_PATOLOGIA;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_ADICIONAR_TIPO_PATOLOGIA;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_TIPOS_PATOLOGIA;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_TIPOS_PATOLOGIA;

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
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.service.TipoPatologiaService;

@Controller
@RequestMapping("/tipo-patologia")
public class TipoPatologiaController {

	@Autowired
	private TipoPatologiaService tipoPatologiaService;

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/adicionar")
	public ModelAndView adicionarTipoPatologiaFormulario(TipoPatologia patologia){
		ModelAndView model = new ModelAndView(FORMULARIO_ADICIONAR_TIPO_PATOLOGIA);
		model.addObject("tipoPatologia", patologia);
		return model;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////
    @PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping(value = "/adicionar")
	public ModelAndView adicionarTipoPatologia(@Valid @ModelAttribute("tipoPatologia") TipoPatologia tipoPatologia, BindingResult result,
											   RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_TIPOS_PATOLOGIA);

		if(!result.hasErrors()){
			try {
				if(PatologiaIdNull(tipoPatologia)) {
					tipoPatologiaService.salvar(tipoPatologia);
					addFlashAttribute(attributes, SUCCESS, SUCCESS_SALVAR_TIPO_PATOLOGIA);
				} else {
					tipoPatologiaService.atualizar(tipoPatologia);
					addFlashAttribute(attributes, SUCCESS, SUCCESS_EDITAR_TIPO_PATOLOGIA);
				}
			} catch (ProntuarioException e) {
				modelAndView.addObject("tiposPatologia", tipoPatologiaService.buscarTudo());
				modelAndView.addObject(ERROR, e.getMessage());
				modelAndView.setViewName(PAGINA_LISTAGEM_TIPOS_PATOLOGIA);
			}
		}
		return modelAndView;
	}
	
	public boolean PatologiaIdNull(TipoPatologia tipoPatologia) {
		return tipoPatologia.getId() == null;
	}
	
	public void addFlashAttribute(RedirectAttributes attributes, String option1, String option2) {
		attributes.addFlashAttribute(option1, option2);
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/editar/{idTipoPatologia}")
	public ModelAndView formularioEditarTipoPatologia(@PathVariable("idTipoPatologia") TipoPatologia tipoPatologia){
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_ADICIONAR_TIPO_PATOLOGIA);
		modelAndView.addObject("tipoPatologia", tipoPatologia);
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping(value = "/remover/{id}")
	public ModelAndView excluirTipoPatologia(@PathVariable("id") TipoPatologia tipoPatologia, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_TIPOS_PATOLOGIA);
		try {
			tipoPatologiaService.remover(tipoPatologia.getId());
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_TIPO_PATOLOGIA);
		} catch (Exception e) {
			attributes.addFlashAttribute(ERROR, ERRO_EXCLUIR_TIPO_PATOLOGIA);
		}
		return modelAndView;
	}

	@GetMapping(value = "/listar")
	public ModelAndView listarTiposPatologia() {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LISTAGEM_TIPOS_PATOLOGIA);
		modelAndView.addObject("tiposPatologia", tipoPatologiaService.buscarTudo());
		return modelAndView;
	}
}
