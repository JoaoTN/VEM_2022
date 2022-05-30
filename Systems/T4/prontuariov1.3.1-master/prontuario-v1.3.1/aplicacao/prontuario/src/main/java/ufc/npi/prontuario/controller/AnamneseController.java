package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_PERGUNTA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_FINALIZAR_ANAMNESE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_REMOVER_ANAMNESE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_SALVAR_ANAMNESE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_SALVAR_PERGUNTA;
import static ufc.npi.prontuario.util.PagesConstants.DETALHES_ANAMNESE;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_CADASTRAR_ANAMNESE;
import static ufc.npi.prontuario.util.PagesConstants.LISTAR_ANAMNESE;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_ANAMNESES;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_PAGINA_DETALHES_ANAMNESE;
import static ufc.npi.prontuario.util.FragmentsConstants.FRAGMENT_ANAMSESE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.Pergunta;
import ufc.npi.prontuario.model.Pergunta.TiposPerguntas;
import ufc.npi.prontuario.service.AnamneseService;

@Controller
@RequestMapping("/anamnese")
public class AnamneseController {

	@Autowired
	private AnamneseService anamneseService;

	@GetMapping
	public ModelAndView listarAnamneses() {
		ModelAndView modelAndView = new ModelAndView(LISTAR_ANAMNESE);
		modelAndView.addObject("anamneses", anamneseService.buscarTudo());
		return modelAndView;
	}
	
	@GetMapping("/listar-perguntas/{id}")
	public ModelAndView tabelaAnamneses(@PathVariable("id") Anamnese anamnese) {
		ModelAndView modelAndView = new ModelAndView(FRAGMENT_ANAMSESE);
		modelAndView.addObject("anamnese", anamnese);
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping("/cadastrar-anamnese")
	public ModelAndView formCadastrarAnamnese(Anamnese anamnese) {
		ModelAndView modelAndView = new ModelAndView(FORMULARIO_CADASTRAR_ANAMNESE);

		modelAndView.addObject("tiposPerguntas", TiposPerguntas.values());
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping("/cadastrar-anamnese")
	public ModelAndView cadastrarAnamnese(Anamnese anamnese, RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_ANAMNESES);
		try {
			anamneseService.salvar(anamnese);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_SALVAR_ANAMNESE);
		} catch (ProntuarioException e) {
			modelAndView.addObject(ERROR, e.getMessage());
			modelAndView.setViewName(FORMULARIO_CADASTRAR_ANAMNESE);
		}
		return modelAndView;
	}

	@GetMapping("/anamnese-detalhes/{id}")
	public ModelAndView detalhesAnamnese(@PathVariable("id") Anamnese anamnese) {
		ModelAndView modelAndView = new ModelAndView();
		if (anamnese == null) {
			modelAndView.setViewName(REDIRECT_LISTAGEM_ANAMNESES);
			return modelAndView;
		}
		modelAndView.setViewName(DETALHES_ANAMNESE);
		modelAndView.addObject("anamnese", anamnese);
		modelAndView.addObject("pergunta", new Pergunta());
		modelAndView.addObject("tiposPerguntas", TiposPerguntas.values());

		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping("/salvar-pergunta")
	public ModelAndView salvarPergunta(@ModelAttribute("pergunta") Pergunta pergunta, Integer idAnamnese,
			RedirectAttributes attributes) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_PAGINA_DETALHES_ANAMNESE + idAnamnese);

		if (idAnamnese != null && !pergunta.getTexto().trim().isEmpty()) {
			anamneseService.salvarPergunta(pergunta, idAnamnese);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_SALVAR_PERGUNTA);
		}

		return modelAndView;
	}
	
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping("/excluir-pergunta/{idAnamnese}/{idPergunta}")
	public ModelAndView excluirPergunta(@PathVariable("idAnamnese") Anamnese anamnese,
			@PathVariable("idPergunta") Pergunta pergunta, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(REDIRECT_PAGINA_DETALHES_ANAMNESE + anamnese.getId());
		
		anamneseService.excluirPergunta(pergunta, anamnese);
		attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_PERGUNTA);
		
		return mv;
	}
	

	@GetMapping("/altera-ordem-pergunta/{idAnamnese}")
	public ModelAndView alteraOrdemPerguntaDaAnamnese(@PathVariable("idAnamnese") Anamnese anamnese,
			@RequestParam("pergunta")Integer  pergunta, @RequestParam("index")Integer index){
		
		ModelAndView modelAndView = new ModelAndView(REDIRECT_PAGINA_DETALHES_ANAMNESE + anamnese.getId());
		
		Anamnese old = anamneseService.alterarOrdemAnamnese(anamnese, pergunta, index);
		
		modelAndView.addObject("anamnese", old);
		return modelAndView;
	}

	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@PostMapping("/finalizar-anamnese/{id}")
	public ModelAndView finalizarAnamnese(@PathVariable("id") Anamnese anamnese, RedirectAttributes attributes) {
		anamneseService.finalizar(anamnese);
		attributes.addFlashAttribute(SUCCESS, SUCCESS_FINALIZAR_ANAMNESE);
		return new ModelAndView(REDIRECT_PAGINA_DETALHES_ANAMNESE + anamnese.getId());
	}
	
	@PreAuthorize(PERMISSAO_ADMINISTRACAO)
	@GetMapping("/remover/{id}")
	public ModelAndView excluirAnamnese(@PathVariable("id") Anamnese anamnese, RedirectAttributes attributes){
		ModelAndView modelAndView = new ModelAndView(REDIRECT_LISTAGEM_ANAMNESES);
		try {
			anamneseService.remover(anamnese);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_REMOVER_ANAMNESE);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		return modelAndView;
	}
}
