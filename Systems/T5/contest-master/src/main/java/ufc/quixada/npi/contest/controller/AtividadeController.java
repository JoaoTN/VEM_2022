package ufc.quixada.npi.contest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.contest.model.Atividade;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Participacao;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.service.AtividadeService;
import ufc.quixada.npi.contest.service.ParticipacaoService;
import ufc.quixada.npi.contest.util.GetEvento;
import ufc.quixada.npi.contest.validator.ContestException;

@RequestMapping("/atividade")
@Controller
public class AtividadeController {

    @Autowired
    private AtividadeService atividadeService;

    @Autowired
    private ParticipacaoService participacaoService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PreAuthorize("#atividade != null && #atividade.evento.isOrganizador(authentication.principal)")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String visualizarAtividade(@PathVariable("id") Atividade atividade, Model model) {
        model.addAttribute("atividade", atividade);
        model.addAttribute("participacoes", atividade.getParticipacoes());
        return "atividade/detalhe-atividade";
    }

    @PreAuthorize("#atividade != null && #atividade.evento.isOrganizador(authentication.principal)")
    @RequestMapping(value = "/participacao/{id}", method = RequestMethod.POST)
    public String registrarParticipacao(@PathVariable("id") Atividade atividade, @RequestParam("participantes") List<Pessoa> participantes,
                Authentication authentication, RedirectAttributes redirectAttributes) {
        List<String> warnings = new ArrayList<>();
        List<String> successes = new ArrayList<>();
    	for (Pessoa participante : participantes) {
			try {
				participacaoService.registrarParticipacao(atividade, participante, (Pessoa) authentication.getPrincipal());
				successes.add(participante.getNome());
			} catch (ContestException e) {
				warnings.add(participante.getNome() + " - " + e.getMessage());
			}
		}
        redirectAttributes.addFlashAttribute("warnings", warnings);
        redirectAttributes.addFlashAttribute("successes", successes);
        return "redirect:/atividade/" + atividade.getId();
    }

    @PreAuthorize("#atividade != null && #atividade.evento.isOrganizador(authentication.principal)")
    @RequestMapping(value = "/gerar-codigo/{id}", method = RequestMethod.GET)
    public String gerarCodigo(@PathVariable("id") Atividade atividade,  Authentication authentication, RedirectAttributes redirectAttributes) {
        atividadeService.gerarCodigo(atividade);
        redirectAttributes.addFlashAttribute("info", "Código gerado com sucesso");
        return "redirect:/atividade/" + atividade.getId();
    }

	@PreAuthorize("#atividade != null && #atividade.evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{idAtividade}/editar", method = RequestMethod.POST)
	public String editar(Atividade atividadeEditada, @PathVariable("idAtividade") Atividade atividade,
			RedirectAttributes redirectAttributes) {
		String[] igoneProperties = { "id", "evento", "participacoes"};
		BeanUtils.copyProperties(atividadeEditada, atividade, igoneProperties);
		atividadeService.adicionarOuAtualizar(atividade);
		redirectAttributes.addFlashAttribute("info", "Atividade atualizada com sucesso");
		return "redirect:/atividade/" + atividade.getId();
	}

	@PreAuthorize("#evento != null && #evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{idEvento}/adicionar", method = RequestMethod.POST)
	public String adicionar(Atividade atividade, @PathVariable("idEvento") Evento evento,
			RedirectAttributes redirectAttributes) {
		atividade.setEvento(evento);
		atividadeService.adicionarOuAtualizar(atividade);

		redirectAttributes.addFlashAttribute("info", "Atividade adicionada com sucesso");

		return "redirect:/atividade/" + atividade.getId();
	}

	@PreAuthorize("#atividade != null && #atividade.evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{idAtividade}/excluir")
	public String excluir(@PathVariable("idAtividade") Atividade atividade, RedirectAttributes redirectAttributes) {
		if (atividadeService.hasParticipacoes(atividade)) {
			redirectAttributes.addFlashAttribute("error",
					"Não é possível excluir a atividade pois possui participantes");
		} else {
			atividadeService.delete(atividade.getId());
			redirectAttributes.addFlashAttribute("info", "Atividade excluída com sucesso");
		}
		return "redirect:/evento/" + GetEvento.getId(atividade.getEvento()) + "/atividades";
	}

    @PreAuthorize("#atividade != null && #atividade.evento.isOrganizador(authentication.principal)")
    @RequestMapping(value = "/{idAtividade}/sorteio")
    public String sorteio(@PathVariable("idAtividade") Atividade atividade, RedirectAttributes redirectAttributes) {
        List<Participacao> participacoes = atividade.getParticipacoes();
        if (participacoes == null || participacoes.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Não há participantes nesta atividade");
        } else {
            redirectAttributes.addFlashAttribute("sorteado", participacoes.get(new Random().nextInt(participacoes.size())).getParticipante());
			redirectAttributes.addFlashAttribute("gif", new Random().nextInt(10));
        }
        return "redirect:/atividade/" + atividade.getId();
    }

}
