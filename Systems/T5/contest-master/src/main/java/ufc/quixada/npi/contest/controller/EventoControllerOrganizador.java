package ufc.quixada.npi.contest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.service.*;
import ufc.quixada.npi.contest.util.Constants;
import ufc.quixada.npi.contest.util.PessoaLogadaUtil;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/eventoOrganizador")
public class EventoControllerOrganizador {

	private static final String ORGANIZADOR_SUCESS = "organizadorSucess";
	private static final String ORGANIZADOR_ERROR = "organizadorError";
	private static final String ERRO_ENVIO_EMAIL = "ERRO_ENVIO_EMAIL";
	private static final String EVENTOS_QUE_ORGANIZO = "eventosQueOrganizo";
	private static final String EXISTE_SUBMISSAO = "existeSubmissao";
	private static final String SUBMISSAO_REVISAO = "existeSubmissaoRevisao";
	private static final String SUBMISSAO_FINAL = "existeSubmissaoFinal";
	private static final String EVENTOS_INATIVOS = "eventosInativos";
	private static final String TRABALHOS_DO_EVENTO = "organizador/org_ver_trabalhos_evento";
	private static final String CONVIDAR_EVENTO_INATIVO = "CONVIDAR_EVENTO_INATIVO";
	private static final String EMAIL_ENVIADO_SUCESSO = "EMAIL_ENVIADO_SUCESSO";

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private EventoService eventoService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private TrilhaService trilhaService;

	@Autowired
	private TrabalhoService trabalhoService;

	@Autowired
	private RevisaoService revisaoService;

	@Autowired
	private SubmissaoService submissaoService;

	@Autowired
	private SessaoService sessaoService;

	@RequestMapping(value = "/evento/{id}/revisoes", method = RequestMethod.GET)
	public String consideracoesRevisores(@PathVariable String id, Model model, RedirectAttributes redirect) {
		Long eventoId = Long.parseLong(id);
		List<Trabalho> trabalhos = trabalhoService.getTrabalhosRevisadosComentadosByEvento(eventoId);
		Boolean participacaoComoOrganizador = isUsuarioLogadoOrganizadorEvento(eventoId);

		if (participacaoComoOrganizador) {
			if (!trabalhos.isEmpty()) {
				model.addAttribute("trabalhos", trabalhos);
				return Constants.TEMPLATE_CONSIDERACOES_REVISORES_ORG;
			}
			redirect.addFlashAttribute("revisao_inexistente", messageService.getMessage("REVISAO_INEXISTENTE"));
			return "redirect:/eventoOrganizador/evento/" + eventoId;
		} else {
			redirect.addFlashAttribute("nao_organizador", messageService.getMessage("NAO_ORGANIZADOR"));
			return "redirect:/eventoOrganizador/evento/" + eventoId;
		}
	}

	@RequestMapping(value = "/evento/trabalho/revisor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String atibuirRevisor(@RequestBody RevisaoJsonWrapper dadosRevisao) {
		throw new UnsupportedOperationException("Não implementado.");

		/**
		 * Definir nova estratégia para adicioanar o revisor do trabalho
		 * 
		 * 
		 * 
		 * Pessoa revisor = pessoaService.get(dadosRevisao.getRevisorId()); Trabalho
		 * trabalho = trabalhoService.getTrabalhoById(dadosRevisao.getTrabalhoId());
		 * 
		 * if (trabalho.isAutorInTrabalho(revisor)) { throw new
		 * IllegalArgumentException("Revisor selecionado é autor no trabalho"); }
		 * 
		 * ParticipacaoTrabalho participacaoTrabalho = participacaoTrabalhoService
		 * .getParticipacaoTrabalhoRevisor(revisor.getId(), trabalho.getId());
		 * 
		 * if (participacaoTrabalho == null) { ParticipacaoTrabalho
		 * participacaoTrabalhoTemp = new ParticipacaoTrabalho();
		 * participacaoTrabalhoTemp.setPapel(Tipo.REVISOR);
		 * participacaoTrabalhoTemp.setPessoa(revisor);
		 * participacaoTrabalhoTemp.setTrabalho(trabalho);
		 * 
		 * participacaoTrabalhoService.adicionarOuEditar(participacaoTrabalhoTemp); }
		 * 
		 * return "{\"result\":\"ok\"}";
		 * 
		 */

	}

	@RequestMapping(value = "/evento/trabalho/removerRevisor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String removerRevisor(@RequestBody RevisaoJsonWrapper dadosRevisao) {
		throw new UnsupportedOperationException("Não implementado.");

		/**
		 * Definir nova estratégia para remover o revisor do trabalho
		 * 
		 * 
		 * 
		 * ParticipacaoTrabalho participacao = participacaoTrabalhoService
		 * .getParticipacaoTrabalhoRevisor(dadosRevisao.getRevisorId(),
		 * dadosRevisao.getTrabalhoId());
		 * participacaoTrabalhoService.remover(participacao); return
		 * "{\"result\":\"ok\"}";
		 */
	}

	@RequestMapping(value = "/convidar/{id}", method = RequestMethod.GET)
	public String convidarPessoasPorEmail(@PathVariable String id, Model model, RedirectAttributes redirect) {
		Long eventoId = Long.parseLong(id);
		Evento evento = eventoService.buscarEventoPorId(eventoId);
		Pessoa professorLogado = PessoaLogadaUtil.pessoaLogada();

		if (EstadoEvento.ATIVO.equals(evento.getEstado())) {
			model.addAttribute("eventoId", eventoId);
			return Constants.TEMPLATE_CONVIDAR_PESSOAS_EMAIL_ORG;
		} else {
			redirect.addFlashAttribute(ORGANIZADOR_ERROR, messageService.getMessage(CONVIDAR_EVENTO_INATIVO));
			return "redirect:/eventoOrganizador/evento/" + eventoId;
		}
	}

	@RequestMapping(value = "/trilha/editar", method = RequestMethod.POST)
	public String atualizarTrilha(@RequestParam(required = false) String eventoId, @Valid Trilha trilha, Model model,
			BindingResult result, RedirectAttributes redirect) {
		long idEvento = Long.parseLong(eventoId);
		model.addAttribute("trilha", trilhaService.get(trilha.getId(), idEvento));
		if (trilha.getNome().isEmpty()) {
			model.addAttribute(ORGANIZADOR_ERROR, messageService.getMessage("TRILHA_NOME_VAZIO"));
		} else {
			if (eventoService.existeEvento(idEvento)) {
				if (trilhaService.exists(trilha.getNome(), idEvento)) {
					model.addAttribute(ORGANIZADOR_ERROR, messageService.getMessage("TRILHA_NOME_JA_EXISTE"));
				} else if (trilhaService.existeTrabalho(trilha.getId())) {
					model.addAttribute(ORGANIZADOR_ERROR, messageService.getMessage("TRILHA_POSSUI_TRABALHO"));
				} else {
					trilha.setEvento(eventoService.buscarEventoPorId(idEvento));
//					trilhaService.adicionarOuAtualizarTrilha(trilha);
					model.addAttribute("trilha", trilhaService.get(trilha.getId(), idEvento));
				}
			} else {
				model.addAttribute(ORGANIZADOR_ERROR, messageService.getMessage("EVENTO_NAO_EXISTE"));
			}
		}
		return Constants.TEMPLATE_DETALHES_TRILHA_ORG;
	}

	public Pessoa getUsuarioLogado() {
		return PessoaLogadaUtil.pessoaLogada();
	}

	public boolean isUsuarioLogadoOrganizadorEvento(Long eventoId) {

		throw new UnsupportedOperationException("Não implementado.");

		/**
		 * Verificar necessidade/Refazer
		 * 
		 * 
		 * Pessoa usuarioLogado =
		 * pessoaService.get(PessoaLogadaUtil.pessoaLogada().getId()); return
		 * participacaoEventoService.isOrganizadorDoEvento(usuarioLogado, eventoId);
		 * 
		 */

	}

	/*@RequestMapping(value = "/avaliar/", method = RequestMethod.POST)
	public String avaliarTrabalhoModerado(@RequestParam Long idEvento, @RequestParam String avaliacao,
			@RequestParam Long idTrabalho, Model model) {

		if (isUsuarioLogadoOrganizadorEvento(idEvento)) {
			Trabalho trabalho = trabalhoService.getTrabalhoById(idTrabalho);
			trabalho.setStatus(Avaliacao.valueOf(avaliacao));
			trabalhoService.adicionarTrabalho(trabalho);
			List<Trabalho> trabalhos = trabalhoService.getTrabalhosEvento(eventoService.buscarEventoPorId(idEvento));

			Evento evento = eventoService.buscarEventoPorId(idEvento);
			if (evento == null) {
				return "redirect:/error";
			}

			model.addAttribute("evento", evento);
			model.addAttribute("opcoesFiltro", Avaliacao.values());
			model.addAttribute("trabalhos", trabalhos);

			// return verTrabalhosDoEvento(idEvento, model);
		}
		return Constants.ERROR_403;
	}*/
}