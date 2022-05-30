package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.PERMISSAO_ADMINISTRACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_AVALIACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_CADASTRAR_CRITERIO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_AVALIACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_ITEM_AVALIACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_FINALIZAR_AVALIACAO;
import static ufc.npi.prontuario.util.PagesConstants.FORMULARIO_CADASTRAR_VALIDACAO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_DETALHE_AVALIACAO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LISTAGEM_AVALIACAO;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_DETALHE_AVALIACAO;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_AVALIACAO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_ALTERAR_STATUS;

import java.util.List;

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
import ufc.npi.prontuario.model.Avaliacao;
import ufc.npi.prontuario.model.ItemAvaliacao;
import ufc.npi.prontuario.service.AvaliacaoService;

@Controller
@PreAuthorize(PERMISSAO_ADMINISTRACAO)
@RequestMapping("/avaliacao")
public class AvaliacaoController {

	@Autowired
	private AvaliacaoService avaliacaoService;
	
	@GetMapping("/cadastrar")
	public ModelAndView formularioCadastrarValidacao(@ModelAttribute("avaliacao") Avaliacao avaliacao) {
		return new ModelAndView(FORMULARIO_CADASTRAR_VALIDACAO).addObject("avaliacao", avaliacao);
	}
	
	@PostMapping("/cadastrar")
	public ModelAndView cadastrarAvaliacao(Avaliacao avaliacao, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(REDIRECT_LISTAGEM_AVALIACAO);
		avaliacaoService.salvar(avaliacao);
		attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_AVALIACAO);
		return mv;
	}
	
	@GetMapping("detalhes/{avaliacao}")
	public ModelAndView visualizarDetalhes(Avaliacao avaliacao) {
		ModelAndView modelAndView = new ModelAndView();
		if (avaliacao == null) {
			modelAndView.setViewName(REDIRECT_LISTAGEM_AVALIACAO);
			return modelAndView;
		}
		modelAndView.setViewName(PAGINA_DETALHE_AVALIACAO);
		modelAndView.addObject("avaliacao", avaliacao);
		modelAndView.addObject("item", new ItemAvaliacao());
		return modelAndView;
	}
	
	@PostMapping("/adicionar-criterio")
	public ModelAndView adicionarCriterio(ItemAvaliacao item, @RequestParam("avaliacao") Avaliacao avaliacao, RedirectAttributes attributes) {
		try {
			avaliacao = avaliacaoService.addItem(avaliacao, item);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_CADASTRAR_CRITERIO);
			attributes.addFlashAttribute("avaliacao", avaliacao);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		return new ModelAndView(REDIRECT_DETALHE_AVALIACAO + avaliacao.getId());
	}
	
	@GetMapping
	public ModelAndView listarAvaliacoes() {
		ModelAndView mv = new ModelAndView(PAGINA_LISTAGEM_AVALIACAO);
		List<Avaliacao> avaliacoes = avaliacaoService.getTodasAvaliacoes();
		mv.addObject("avaliacoes", avaliacoes);
		return mv;
	}
	
	@GetMapping("excluir/{id}")
	public ModelAndView excluirAvaliacao(@PathVariable("id") Avaliacao avaliacao, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(REDIRECT_LISTAGEM_AVALIACAO);
		try {
			avaliacaoService.deleteAvaliacao(avaliacao);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_AVALIACAO);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		return mv;
	}

	@GetMapping("/excluir-item/{id}")
	public ModelAndView deleteItemAvaliacao(@PathVariable("id") ItemAvaliacao item, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(REDIRECT_DETALHE_AVALIACAO + item.getAvaliacao().getId());
		avaliacaoService.deleteItem(item);
		attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_ITEM_AVALIACAO);
		return mv;
	}
	
	@GetMapping("/finalizar/{id}")
	public ModelAndView finalizarAvaliacao(@PathVariable("id") Avaliacao avaliacao, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(REDIRECT_LISTAGEM_AVALIACAO);
		avaliacaoService.finalizar(avaliacao);
		attributes.addFlashAttribute(SUCCESS, SUCCESS_FINALIZAR_AVALIACAO);
		return mv;
	}
	
	@GetMapping("/ativar-desativar/{id}")
	public ModelAndView ativarDesativarAvaliacao(@PathVariable("id") Avaliacao avaliacao, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(REDIRECT_LISTAGEM_AVALIACAO);
		try {
			avaliacaoService.ativarDesativar(avaliacao);
			attributes.addFlashAttribute(SUCCESS, SUCCESS_ALTERAR_STATUS);
		} catch (ProntuarioException e) {
			attributes.addFlashAttribute(ERROR, e.getMessage());
		}
		return mv;
	}
}
