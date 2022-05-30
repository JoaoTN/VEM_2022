package ufc.quixada.npi.contest.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.service.*;
import ufc.quixada.npi.contest.util.GetEvento;
import ufc.quixada.npi.contest.util.PessoaLogadaUtil;
import ufc.quixada.npi.contest.validator.ContestException;

@Controller
@RequestMapping("/evento")
public class EventoController {

	private static final String EVENTO = "evento";

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private EventoService eventoService;

	@Autowired
	private TrabalhoService trabalhoService;

	@Autowired
	private SessaoService sessaoService;

	@Autowired
	private TrilhaService trilhaService;

	@Autowired
	private AtividadeService atividadeService;

	@Autowired
    private ParticipacaoService participacaoService;

	@ModelAttribute("pessoas")
	public List<Pessoa> listaPossiveisOrganizadores() {
		return pessoaService.list();
	}

	// OK
	@RequestMapping(value = "/{eventoId}", method = RequestMethod.GET)
	public String dashboard(@PathVariable("eventoId") Evento evento, Model model, RedirectAttributes redirectAttributes) {
		if (evento == null) {
			redirectAttributes.addFlashAttribute("error", "Evento não encontrado");
			return "redirect:/";
		}
		model.addAttribute("evento", evento);
		return "evento/dashboard-evento";
	}

	// OK
	@RequestMapping(value = "/{eventoId}/detalhe", method = RequestMethod.GET)
	public String detalhesEvento(@PathVariable("eventoId") Evento evento, Model model, RedirectAttributes redirectAttributes) {
		if (evento == null) {
			redirectAttributes.addFlashAttribute("error", "Evento não encontrado");
			return "redirect:/";
		}
		model.addAttribute("evento", evento);
		return "evento/detalhe-evento";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/editar", method = RequestMethod.POST)
	public String editarEvento(Evento eventoComAlteracoes, @PathVariable("eventoId") Evento evento,
			RedirectAttributes redirectAttributes) {

		if (evento == null) {
			redirectAttributes.addFlashAttribute("error", "Evento não encontrado");
			return "redirect:/";
		}
		String[] igoneProperties = { "id", "visibilidade", "estado", "organizadores", "revisores", "trilhas",
				"modalidadesSubmissao", "modalidadesApresentacao", "sessoes" };
		BeanUtils.copyProperties(eventoComAlteracoes, evento, igoneProperties);
		try {
			eventoService.adicionarOuAtualizarEvento(evento);
			redirectAttributes.addFlashAttribute("info", "Evento alterado com sucesso");
		} catch (ContestException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/evento/" + GetEvento.getId(evento) + "/detalhe";
	}

    // OK
    @RequestMapping(value = "/{eventoId}/sessoes")
    public String indexSessao(Model model, @PathVariable("eventoId") Evento evento, Authentication authentication, RedirectAttributes redirectAttributes) {
		if (evento == null) {
			redirectAttributes.addFlashAttribute("error", "Evento não encontrado");
			return "redirect:/";
		}
		model.addAttribute(EVENTO, evento);
        model.addAttribute("trabalhos", trabalhoService.getTrabalhosEvento(evento));
        model.addAttribute("sessoes", sessaoService.listByEvento(evento));
		model.addAttribute("minhasSessoes", sessaoService.findByChefe(evento, (Pessoa) authentication.getPrincipal()));
		model.addAttribute("servidores", pessoaService.list());
        return "sessao/listagem-sessao";
    }

    // OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
    @RequestMapping(value = "/{idEvento}/sessao", method = RequestMethod.POST)
    public String cadastrarSessao(Sessao sessao, @PathVariable("idEvento") Evento evento, RedirectAttributes redirectAttributes) {
        sessao.setEvento(evento);
        sessaoService.addOrUpdate(sessao);
        redirectAttributes.addFlashAttribute("info", "Sessão cadastrada com sucesso");
        return "redirect:/evento/" + GetEvento.getId(sessao.getEvento()) + "/sessoes";
    }

    // OK
    @RequestMapping(value = "/{id}/submissoes", method = RequestMethod.GET)
    public String visualizarSubmissoes(@PathVariable("id") Evento evento, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
		if (evento == null) {
			redirectAttributes.addFlashAttribute("error", "Evento não encontrado");
			return "redirect:/";
		}
		Pessoa usuario = (Pessoa) authentication.getPrincipal();
		model.addAttribute("evento", evento);
        if (evento.isOrganizador(usuario)) {
			model.addAttribute("trabalhos", trabalhoService.getTrabalhosEvento(evento));
		}
		model.addAttribute("meusTrabalhos", trabalhoService.getTrabalhos(evento, usuario));
        return "evento/submissoes-evento";
    }

	// OK
	@RequestMapping(value = "/{id}/revisoes", method = RequestMethod.GET)
	public String visualizarRevisoes(@PathVariable("id") Evento evento, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
		if (evento == null) {
			redirectAttributes.addFlashAttribute("error", "Evento não encontrado");
			return "redirect:/";
		}
		Pessoa usuario = (Pessoa) authentication.getPrincipal();
		model.addAttribute("evento", evento);
		if (evento.isRevisor(usuario)) {
			model.addAttribute("minhasRevisoes", trabalhoService.getTrabalhosParaRevisar(usuario, evento));
		}
		if (evento.isOrganizador(usuario)) {
			model.addAttribute("aguardandoRevisor", trabalhoService.getTrabalhosAguardandoRevisor(evento));
			model.addAttribute("aguardandoRevisao", trabalhoService.getTrabalhosAguardandoRevisao(evento));
			model.addAttribute("revisados", trabalhoService.getTrabalhosRevisados(evento));
		}
		return "evento/revisoes-evento";
	}

	@RequestMapping(value = "/adicionar", method = RequestMethod.GET)
	public String adicionarEvento(Model model) {
		Pessoa organizador = new Pessoa();
		
		model.addAttribute(EVENTO, new Evento());
		model.addAttribute("organizador", organizador);
		
		return ""; //Constants.TEMPLATE_ADICIONAR_OU_EDITAR_EVENTO_ADMIN;gin

	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/trilha", method = RequestMethod.POST)
	public String adicionarTrilha(Trilha trilha, @PathVariable("eventoId") Evento evento, RedirectAttributes redirectAttributes) {
		trilhaService.adicionarOuAtualizarTrilha(evento, trilha);
		redirectAttributes.addFlashAttribute("tab", "trilha");
		redirectAttributes.addFlashAttribute("info", "Trilha adicionada com sucesso");
		return "redirect:/evento/" + GetEvento.getId(evento) + "/detalhe";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/excluir-trilha/{trilhaId}")
	public String excluirTrilha(@PathVariable("eventoId") Evento evento,
			@PathVariable("trilhaId") Trilha trilha, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("tab", "trilhas");
		redirectAttributes.addFlashAttribute("info", "Trilha excluída com sucesso");
		trilhaService.excluir(evento, trilha);
		return "redirect:/evento/" + GetEvento.getId(evento) + "/detalhe";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/modalidade-submissao", method = RequestMethod.POST)
	public String adicionarModalidadeSubmissao(Model model, @PathVariable("eventoId") Evento evento,
			@RequestParam("nome") String nomeModalidade, RedirectAttributes redirectAttributes) {
		List<Modalidade> modalidadeEvento = evento.getModalidadesSubmissao();
		modalidadeEvento.add(new Modalidade(nomeModalidade));
		evento.setModalidadesSubmissao(modalidadeEvento);
		// evento.addModalidadeSubmissao(new Modalidade(nomeModalidade));
		try {
			eventoService.adicionarOuAtualizarEvento(evento);
            redirectAttributes.addFlashAttribute("info", "Modalidade de submissão adicionada com sucesso");
		} catch (ContestException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
        redirectAttributes.addFlashAttribute("tab", "modalidades-submissao");
		return "redirect:/evento/" + GetEvento.getId(evento) + "/detalhe";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/excluir-modalidade-submissao/{modalidadeId}")
	public String excluirModalidadeSubmissao(Model model, @PathVariable("eventoId") Evento evento,
			@PathVariable("modalidadeId") Modalidade modalidade, RedirectAttributes redirectAttributes) {
		List<Modalidade> modalidadeEvento = evento.getModalidadesSubmissao();
		modalidadeEvento.remove(modalidade);
		evento.setModalidadesSubmissao(modalidadeEvento);
		//evento.removeModalidadeSubmissao(modalidade);
		try {
			eventoService.adicionarOuAtualizarEvento(evento);
			redirectAttributes.addFlashAttribute("info", "Modalidade de submissão excluída com sucesso");
		} catch (ContestException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		redirectAttributes.addFlashAttribute("tab", "modalidades-submissao");
		return "redirect:/evento/" + GetEvento.getId(evento) + "/detalhe";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/modalidade-apresentacao", method = RequestMethod.POST)
	public String adicionarModalidadeApresentacao(Model model, @PathVariable("eventoId") Evento evento,
											   @RequestParam("nome") String nomeModalidade, RedirectAttributes redirectAttributes) {
		List<Modalidade> modalidadesApresentacao = evento.getModalidadesApresentacao();
		modalidadesApresentacao.add(new Modalidade(nomeModalidade));
		evento.setModalidadesApresentacao(modalidadesApresentacao);
		//evento.addModalidadeApresentacao(new Modalidade(nomeModalidade));
		try {
			eventoService.adicionarOuAtualizarEvento(evento);
			redirectAttributes.addFlashAttribute("info", "Modalidade de apresentação adicionada com sucesso");
		} catch (ContestException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		redirectAttributes.addFlashAttribute("tab", "modalidades-apresentacao");
		return "redirect:/evento/" + GetEvento.getId(evento) + "/detalhe";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/excluir-modalidade-apresentacao/{modalidadeId}")
	public String excluirModalidadeApresentacao(Model model, @PathVariable("eventoId") Evento evento,
											 @PathVariable("modalidadeId") Modalidade modalidade, RedirectAttributes redirectAttributes) {
		List<Modalidade> modalidadesApresentacao = evento.getModalidadesApresentacao();
		modalidadesApresentacao.remove(modalidade);
		evento.setModalidadesApresentacao(modalidadesApresentacao);
		// evento.removeModalidadeApresentacao(modalidade);
		try {
			eventoService.adicionarOuAtualizarEvento(evento);
			redirectAttributes.addFlashAttribute("info", "Modalidade de apresentação excluída com sucesso");
		} catch (ContestException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		redirectAttributes.addFlashAttribute("tab", "modalidades-apresentacao");
		return "redirect:/evento/" + GetEvento.getId(evento) + "/detalhe";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/equipe")
	public String indexEquipe(Model model, @PathVariable("eventoId") Evento evento) {
		model.addAttribute(EVENTO, evento);
		return "equipe/listagem-equipe";

	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/organizador", method = RequestMethod.POST)
	public String adicionarOrganizador(Model model, @PathVariable("eventoId") Evento evento,
			@RequestParam("pessoaId") Pessoa pessoa, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("tab", "organizadores");
		redirectAttributes.addFlashAttribute("info", "Organizador adicionado com sucesso");

		eventoService.adicionarOrganizador(evento, pessoa);

		return "redirect:/evento/" + GetEvento.getId(evento) + "/equipe";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/organizador/{pessoaId}/excluir")
	public String excluirOrganizador(Model model, @PathVariable("eventoId") Evento evento,
			@PathVariable("pessoaId") Pessoa pessoa, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("tab", "organizadores");
		redirectAttributes.addFlashAttribute("info", "Organizador excluído com sucesso");
		eventoService.excluirOrganizador(evento, pessoa);

		return "redirect:/evento/" + GetEvento.getId(evento) + "/equipe";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/revisor", method = RequestMethod.POST)
	public String adicionarRevisor(Model model, @PathVariable("eventoId") Evento evento,
			@RequestParam("pessoaId") Pessoa pessoa, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("tab", "revisores");
		redirectAttributes.addFlashAttribute("info", "Revisor adicionado com sucesso");

		eventoService.adicionarRevisor(evento, pessoa);

		return "redirect:/evento/" + GetEvento.getId(evento) + "/equipe";
	}

	// OK
	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{eventoId}/revisor/{pessoaId}/excluir")
	public String excluirRevisor(Model model, @PathVariable("eventoId") Evento evento,
			@PathVariable("pessoaId") Pessoa pessoa, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("tab", "revisores");
		redirectAttributes.addFlashAttribute("info", "Revisor excluído com sucesso");

		eventoService.excluirRevisor(evento, pessoa);

		return "redirect:/evento/" + GetEvento.getId(evento) + "/equipe";
	}

	// OK
	@RequestMapping(value = "/{eventoId}/certificados")
	public String indexCertificados(Model model, @PathVariable("eventoId") Evento evento) {
		model.addAttribute(EVENTO, evento);
		model.addAttribute("trabalhos", trabalhoService.getTrabalhosEvento(evento));
		return "certificados/listagem-certificados";

	}

	// OK
	@RequestMapping(value = "/{eventoId}/atividades")
	public String indexAtividades(Model model, @PathVariable("eventoId") Evento evento) {
		model.addAttribute(EVENTO, evento);
		List<Atividade> atividades = atividadeService.getAtividadePorEvento(GetEvento.getId(evento));
		
		model = construcaoModel(model, atividades, evento);
		
		return "atividade/listagem-atividades";

	}
	
	private Model construcaoModel(Model model, List<Atividade> atividades, Evento evento) {
		model.addAttribute("atividades", atividades);
		model.addAttribute("participacoes", atividadeService.getAtividadePorParticipacao(PessoaLogadaUtil.pessoaLogada()));
		model.addAttribute("participantes",  atividadeService.getParticipantes(evento));
		
		return model;
	}

    // OK
    @PreAuthorize("#evento.isOrganizador(authentication.principal)")
    @RequestMapping(value = "/{eventoId}/participante/{participanteId}")
    public String getParticipante(@PathVariable("eventoId") Evento evento, @PathVariable("participanteId") Pessoa participante,
              Model model, RedirectAttributes redirectAttributes) {
        if (evento == null || participante == null) {
            redirectAttributes.addFlashAttribute("erro", "Não foi encontrada nenhuma participação nesse evento");
            return "redirect:/";
        }
	    model.addAttribute(EVENTO, evento);
        model.addAttribute("participante", participante);
        model.addAttribute("participacoes", participacaoService.findByParticipante(participante, evento));
        return "atividade/detalhe-participante";
    }

	@PreAuthorize("#evento.isOrganizador(authentication.principal)")
    @RequestMapping(value = "/{eventoId}/sorteio")
	public String sortear(Model model, @PathVariable("eventoId") Evento evento, RedirectAttributes redirectAttributes) {
		List<Pessoa> participantes = atividadeService.getParticipantes(evento);
		if (participantes == null || participantes.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Não há participantes neste evento");
		} else {
			redirectAttributes.addFlashAttribute("sorteado", participantes.get(new Random().nextInt(participantes.size())));
			redirectAttributes.addFlashAttribute("gif", new Random().nextInt(10));
		}
		return "redirect:/evento/" + GetEvento.getId(evento) + "/atividades";

	}
	
}
