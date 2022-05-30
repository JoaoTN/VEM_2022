package ufc.quixada.npi.contest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import ufc.quixada.npi.contest.service.SessaoService;
import ufc.quixada.npi.contest.service.TrabalhoService;
import ufc.quixada.npi.contest.util.GetEvento;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ufc.quixada.npi.contest.model.Sessao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.BeanUtils;
import ufc.quixada.npi.contest.model.Trabalho;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

public class SessaoControllerProduct {
	private SessaoService sessaoService;
	private TrabalhoService trabalhoService;

	@RequestMapping("/sessao/notificar/{id}")
	public String enviarEmailParaAutoresPrincipaisDoArtigo(@PathVariable("id") Long id) {
		Sessao sessao = sessaoService.get(id);
		trabalhoService.notificarAutorPrincipalDoArtigo(sessao);
		return SessaoController.VER_SESSAO + id;
	}

	@PreAuthorize("#sessao.evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{idSessao}/editar", method = RequestMethod.POST)
	public String editarSessao(Sessao sessaoEditada, @PathVariable("idSessao") Sessao sessao,
			RedirectAttributes redirectAttributes) {
		String[] igoneProperties = { "id", "trabalhos", "evento" };
		BeanUtils.copyProperties(sessaoEditada, sessao, igoneProperties);
		sessaoService.addOrUpdate(sessao);
		redirectAttributes.addFlashAttribute("info", "Sessão atualizada com sucesso");
		return "redirect:/sessao/" + sessao.getId();
	}

	@PreAuthorize("#trabalho.evento.isOrganizador(authentication.principal)")
	@RequestMapping("/excluir-trabalho/{idTrabalho}/{fromSessao}")
	public String excluirTrabalhoSessao(@PathVariable("idTrabalho") Trabalho trabalho, @PathVariable Boolean fromSessao,
			RedirectAttributes redirectAttributes) {
		Sessao sessao = sessaoService.get(trabalho.getSessao().getId());
		trabalhoService.removerSessao(trabalho);
		redirectAttributes.addFlashAttribute("info", "Trabalho removido da sessão com sucesso");
		redirectAttributes.addFlashAttribute("tab", "alocados");
		if (fromSessao) {
			return "redirect:/sessao/" + sessao.getId();
		} else {
			return "redirect:/evento/" + GetEvento.getId(sessao.getEvento()) + "/sessoes";
		}
	}

	@PreAuthorize("#sessao.evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/alocar", method = RequestMethod.POST)
	public String adicionarTrabalhoNaSessao(@RequestParam("sessao") Sessao sessao,
			@RequestParam(value = "trabalhos", required = false) List<Trabalho> trabalhos,
			RedirectAttributes redirectAttributes) {
		sessaoService.adicionarTrabalhos(sessao, trabalhos);
		redirectAttributes.addFlashAttribute("tab", "nao-alocados");
		redirectAttributes.addFlashAttribute("info", "Trabalhos alocados com sucesso");
		return "redirect:/evento/" + GetEvento.getId(sessao.getEvento()) + "/sessoes";
	}
}