package ufc.quixada.npi.contest.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.service.EventoService;
import ufc.quixada.npi.contest.service.PessoaService;
import ufc.quixada.npi.contest.service.SessaoService;
import ufc.quixada.npi.contest.service.TrabalhoService;
import ufc.quixada.npi.contest.util.Constants;
import ufc.quixada.npi.contest.util.PessoaLogadaUtil;

import java.util.Date;
import java.util.List;

@RequestMapping("/sessao")
@Controller
public class SessaoController {

	private SessaoControllerProduct sessaoControllerProduct = new SessaoControllerProduct();

	public static final String VER_SESSAO = "redirect:/sessao/ver/";
	
	@Autowired
	private PessoaService pessoaService;

	// OK
	@PreAuthorize("#sessao.evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{idSessao}/editar", method = RequestMethod.POST)
	public String editarSessao(Sessao sessaoEditada, @PathVariable("idSessao") Sessao sessao, RedirectAttributes redirectAttributes) {
		return sessaoControllerProduct.editarSessao(sessaoEditada, sessao, redirectAttributes);
	}


	// OK
	@PreAuthorize("#sessao.evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/alocar", method = RequestMethod.POST)
	public String adicionarTrabalhoNaSessao(@RequestParam("sessao") Sessao sessao, @RequestParam(value = "trabalhos", required = false) List<Trabalho> trabalhos, RedirectAttributes redirectAttributes) {
		return sessaoControllerProduct.adicionarTrabalhoNaSessao(sessao, trabalhos, redirectAttributes);
	}

	// OK
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String verTrabalhos(@PathVariable("id") Sessao sessao, Model model, RedirectAttributes redirectAttributes) {
		if (sessao == null) {
			redirectAttributes.addFlashAttribute("error", "Sessão não encontrada");
			return "redirect:/";
		}
		model.addAttribute("servidores", pessoaService.list());
		model.addAttribute("sessao", sessao);
		return "sessao/detalhe-sessao";
	}

	//OK
	@PreAuthorize("#trabalho.evento.isOrganizador(authentication.principal)")
	@RequestMapping("/excluir-trabalho/{idTrabalho}/{fromSessao}")
	public String excluirTrabalhoSessao(@PathVariable("idTrabalho") Trabalho trabalho, @PathVariable Boolean fromSessao, RedirectAttributes redirectAttributes) {
		return sessaoControllerProduct.excluirTrabalhoSessao(trabalho, fromSessao, redirectAttributes);
	}

	@RequestMapping("/sessao/notificar/{id}")
	public String enviarEmailParaAutoresPrincipaisDoArtigo(@PathVariable("id") Long id){
		return sessaoControllerProduct.enviarEmailParaAutoresPrincipaisDoArtigo(id);			
	}
	
}
