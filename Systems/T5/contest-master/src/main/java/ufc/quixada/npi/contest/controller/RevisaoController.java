package ufc.quixada.npi.contest.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.service.*;
import ufc.quixada.npi.contest.util.Constants;
import ufc.quixada.npi.contest.util.GetEvento;
import ufc.quixada.npi.contest.util.PessoaLogadaUtil;
import ufc.quixada.npi.contest.util.RevisaoJSON;

@Controller
@RequestMapping("/revisao")
public class RevisaoController {

	private static final String REVISOR_REVISOR_VER_REVISAO = "revisor/revisor_ver_revisao";
	private static final String[] igoneProperties = { "id", "revisor", "trabalho", "arquivo"};

	@Autowired
	private EventoService eventoService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private TrabalhoService trabalhoService;

	@Autowired
	private RevisaoService revisaoService;

	@Autowired
	private StorageService storageService;
	
	private static final String REVISOR_TRABALHOS_REVISAO = "revisor/revisor_trabalhos";
	private static final String REVISOR_SEM_PERMISSAO = "revisor/erro_permissao_de_revisor";
	private static final String TRABALHO_REVISAO_PELO_REVISOR = "revisor/erro_trabalho_ja_revisado";

	private static final String FORA_PERIODO_REVISAO = "FORA_PERIODO_REVISAO";
	private static final String NAO_HA_REVISAO = "NA_HA_REVISAO";


	// OK
	@RequestMapping(value = "/revisores", method = RequestMethod.POST)
	public String adicionarRevisores(@RequestParam("trabalho") Trabalho trabalho, @RequestParam("revisores") List<Pessoa> revisores, RedirectAttributes redirectAttributes) {
		if (trabalho == null) {
			redirectAttributes.addFlashAttribute("error", "Trabalho não encontrado");
			return "redirect:/";
		}
		trabalhoService.alocarRevisores(trabalho, revisores);
		redirectAttributes.addFlashAttribute("info", "Revisor(es) alocado(s) com sucesso");
		redirectAttributes.addFlashAttribute("tab", "revisor");
		return "redirect:/evento/" + GetEvento.getId(trabalho.getEvento()) + "/revisoes";
	}

	// OK
	@RequestMapping(value = "/{revisao}/excluir")
	public String excluirRevisao(@PathVariable Revisao revisao, RedirectAttributes redirectAttributes) {
		if (revisao == null) {
			redirectAttributes.addFlashAttribute("error", "Revisão não encontrada");
			return "redirect:/";
		}
		Evento evento = revisao.getTrabalho().getEvento();
		revisaoService.excluir(revisao);
		redirectAttributes.addFlashAttribute("info", "Revisor excluído com sucesso");
		redirectAttributes.addFlashAttribute("tab", "revisao");
		return "redirect:/evento/" + GetEvento.getId(evento) + "/revisoes";
	}

	// OK
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String avaliarTrabalho(Revisao revisaoAtualizada, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		
		Revisao revisao = revisaoService.find(revisaoAtualizada.getId());
		if (!file.isEmpty()) {
			Arquivo arquivo = updateFile(revisao.getArquivo(), file);
			revisao.setArquivo(arquivo);
		}
		
		BeanUtils.copyProperties(revisaoAtualizada, revisao, igoneProperties);
		revisaoService.addOrUpdate(revisao);
		redirectAttributes.addFlashAttribute("info", "Revisão realizada com sucesso");
		return "redirect:/trabalho/" + revisao.getTrabalho().getId();
	}
	
	private Arquivo updateFile(Arquivo arquivo, MultipartFile file) {
		if (arquivo == null) {
			return storeArquivo(file);
		} else {
			editArquivo(file, arquivo.getId());
		}
		return arquivo;
	}
	
	private Arquivo storeArquivo(MultipartFile file) {
		return storageService.store(file);
	}
	
	private void editArquivo(MultipartFile file, Integer id) {
		storageService.edit(file, id);
	}

	@RequestMapping(value = "/{idEvento}/trabalhosRevisao")
	public String trabalhosRevisao(Model model, @PathVariable("idEvento") Long idEvento, RedirectAttributes redirect) {
		Evento evento = eventoService.buscarEventoPorId(idEvento);
		Pessoa p = PessoaLogadaUtil.pessoaLogada();
		
		/**
		 * Veficar se esse trecho de código ainda será necessario com as mudanças na visualização
		 * Se sim, atualiza-lo de acordo com o novo modelo de dados.
		
		List<ParticipacaoEvento> participacoesComoRevisor = participacaoEventoService
				.getEventosDoRevisor(EstadoEvento.ATIVO, p.getId());
		List<Long> eventosComoRevisor = new ArrayList<>();
		
		for (ParticipacaoEvento participacaoEvento : participacoesComoRevisor) {
			eventosComoRevisor.add(participacaoEvento.getEvento().getId());
		}
		 */

		if (evento.isPeriodoSubmissao()) {
			redirect.addFlashAttribute("periodoRevisaoError", messageService.getMessage(NAO_HA_REVISAO));
			return "redirect:/eventoOrganizador";
		}

		Pessoa revisor = PessoaLogadaUtil.pessoaLogada();
		model.addAttribute("trabalhos", trabalhoService.getTrabalhosParaRevisar(revisor, evento));
		model.addAttribute("trabalhosRevisados",trabalhoService.getTrabalhosRevisadosDoRevisor(revisor.getId(), idEvento));

		model.addAttribute("evento", evento);

		return REVISOR_TRABALHOS_REVISAO;
	}

	@RequestMapping(value = "/{idTrabalho}/revisar", method = RequestMethod.GET)
	public String revisarTrabalho(HttpSession session, Model model, @PathVariable("idTrabalho") Long idTrabalho,
			RedirectAttributes redirect) {

		Trabalho trabalho = trabalhoService.getTrabalhoById(idTrabalho);
		Pessoa revisor = PessoaLogadaUtil.pessoaLogada();
		
		if (trabalho != null) {
			return revisionRevisor(trabalho.getEvento(), idTrabalho, redirect, revisor.getId());
		}
		
		return "redirect:/error";

		/**
		 * Alterar verificação de acordo com o novo modelo de dados

		
		if (participacaoTrabalhoService.getParticipacaoTrabalhoRevisor(revisor.getId(), trabalho.getId()) != null) {

			model.addAttribute("nomeEvento", evento.getNome());
			model.addAttribute("idEvento", evento.getId());
			model.addAttribute("trabalho", trabalho);

			session.setAttribute("ID_EVENTO_REVISOR", Long.valueOf(evento.getId()));
			session.setAttribute("ID_TRABALHO_REVISOR", Long.valueOf(idTrabalho));
			return REVISOR_AVALIAR_TRABALHO;
		}
		 */
	}
	
	private String revisionRevisor(Evento evento, Long idTrabalho,  RedirectAttributes redirect, Long idRevisor) {
		if (!evento.isPeriodoRevisao()) {
			redirect.addFlashAttribute("periodoRevisaoError", messageService.getMessage(FORA_PERIODO_REVISAO));				
			return "redirect:/eventoOrganizador";
		} else if (revisaoService.isTrabalhoRevisadoPeloRevisor(idTrabalho, idRevisor)) {
			return TRABALHO_REVISAO_PELO_REVISOR;
		}
		
		return REVISOR_SEM_PERMISSAO;
	}
	
	@RequestMapping(value = "/{idTrabalho}/revisao", method = RequestMethod.GET)
	public String verRevisaoTrabalho(Model model, @PathVariable("idTrabalho") Long idTrabalho) {

		Trabalho trabalho = trabalhoService.getTrabalhoById(Long.valueOf(idTrabalho));
		Pessoa revisor = PessoaLogadaUtil.pessoaLogada();
		
		if (trabalho != null &&
			revisaoService.isTrabalhoRevisadoPeloRevisor(trabalho.getId(), revisor.getId())) {
				Revisao revisao = revisaoService.getRevisaoTrabalhoFeitoPor(idTrabalho, revisor.getId());
				model.addAttribute("titulo", trabalho.getTitulo());
				model.addAttribute("revisao", RevisaoJSON.fromJson(revisao));
				model.addAttribute("comentarios_revisao", revisao.getObservacoes());
				return REVISOR_REVISOR_VER_REVISAO; 
		} else {
			return REVISOR_SEM_PERMISSAO;
		}

	}

	/*@RequestMapping(value = "/trabalho/{trabalhoID}", method = RequestMethod.GET)
	public String validaTrabalho(HttpSession session, @PathVariable("trabalhoID") String idTrabalho,
			HttpServletResponse response, RedirectAttributes redirect) {
		
		Pessoa usuarioLogado = PessoaLogadaUtil.pessoaLogada();
		Trabalho trabalho = trabalhoService.getTrabalhoById(Long.valueOf(idTrabalho));

		if (trabalho != null && trabalho.getRevisores().contains(usuarioLogado)) {
			try {
				baixarTrabalho(response, trabalho);
			} catch (IOException e) {
				logger.error("O trabalho[id={}] nao foi encontrado no path {}",
						trabalho.getId(), trabalho.getPath(), e);
				return Constants.ERROR_404;
			}

			session.setAttribute("ID_TRABALHO_REVISOR", idTrabalho);
			return "redirect:/revisor/" + session.getAttribute("ID_EVENTO_REVISOR") + "/" + idTrabalho + "/revisar";
		}

		redirect.addFlashAttribute("trabalhoNaoExisteError", messageService.getMessage(TRABALHO_NAO_EXISTE));
		return Constants.ERROR_403;
	}

	@ResponseBody
	public void baixarTrabalho(HttpServletResponse response, Trabalho trabalho) throws IOException {
		String titulo = trabalho.getTitulo();
		titulo = titulo.replaceAll("\\s", "_");
		String src = trabalho.getPath();
		InputStream is = new FileInputStream(src);
		IOUtils.copy(is, response.getOutputStream());
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename = " + titulo + ".pdf");
		response.flushBuffer();
	}

	@RequestMapping(value = "/participarevento", method = RequestMethod.POST)
	public String professorParticipa(@RequestParam String idEvento, Model model, RedirectAttributes redirect) {
		
		throw new UnsupportedOperationException("Não implementado.");
		
		*//**
		 * Definir se o método ainda será necessario dado o modelo de dados
		 * 
		
		if (!eventoService.existeEvento(Long.parseLong(idEvento))) {
			redirect.addFlashAttribute(EVENTO_VAZIO_ERROR, messageService.getMessage(ID_EVENTO_VAZIO_ERROR));
			return "redirect:/revisor";
		}

		Pessoa professorLogado = PessoaLogadaUtil.pessoaLogada();
		Evento evento = eventoService.buscarEventoPorId(Long.parseLong(idEvento));

		if (evento != null) {
			if (evento.getEstado() == EstadoEvento.ATIVO) {
				ParticipacaoEvento participacaoEvento = new ParticipacaoEvento();
				participacaoEvento.setEvento(evento);
				participacaoEvento.setPessoa(professorLogado);
				participacaoEvento.setPapel(Tipo.REVISOR);

				// TEM QUE ATUALIZAR O USUARIO DA SESSÂO (getPrincipal())

				participacaoEventoService.adicionarOuEditarParticipacaoEvento(participacaoEvento);
				redirect.addFlashAttribute(PARTICAPACAO_EVENTO_SUCESSO,
						messageService.getMessage(PARTICAPAR_EVENTO_SUCESSO));
			} else {
				redirect.addFlashAttribute(PARTICIPAR_EVENTO_INATIVO_ERROR,
						messageService.getMessage(PARTICIPAR_EVENTO_INATIVO));
			}
		} else {
			redirect.addFlashAttribute(EVENTO_NAO_EXISTE, messageService.getMessage(EVENTO_NAO_EXISTE));
			return "redirect:/revisor/";
		}

		return "redirect:/revisor/";
		
		*//*

	}*/


	
	/*@RequestMapping(value = "/evento/{id}")
	public String paginaRevisor(@PathVariable Long id, Model model) {
		
		String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
		Pessoa pessoaAux = pessoaService.getByCpf(cpf);		
		
		List<Evento> eventos;
		
		if(id != null) {
			eventos = new ArrayList<Evento>();
			eventos.add(eventoService.buscarEventoPorId(id));
		} else {
			eventos = eventoService.getMeusEventosAtivosComoRevisor(pessoaAux.getId());		
		}
		
		model.addAttribute("pessoa", pessoaAux);
		
		model.addAttribute("eventos", eventos);
				
		return "revisor/revisor_meus_eventos";
	}*/

	/*@RequestMapping(value = "/")
	public String paginaRevisor(Model model) {
		return paginaRevisor(null, model);
	}*/
	
	@RequestMapping(value = "/ativos", method = RequestMethod.GET)
	public String listarEventosAtivos(Model model) {
		throw new UnsupportedOperationException("Não implementado.");
		
		/**
		 * Veficar se esse código ainda será necessario com as mudanças na visualização
		 * Se sim, atualiza-lo de acordo com o novo modelo de dados.
		
		Pessoa pessoa = PessoaLogadaUtil.pessoaLogada();
		List<Evento> eventosAtivos = eventoService.buscarEventoPorEstado(EstadoEvento.ATIVO);
		List<ParticipacaoEvento> participacoesComoRevisor = participacaoEventoService
				.getEventosDoRevisor(EstadoEvento.ATIVO, pessoa.getId());
		List<ParticipacaoEvento> participacoesComoOrganizador = participacaoEventoService
				.getEventosDoOrganizador(EstadoEvento.ATIVO, pessoa.getId());
		boolean existeEventos = true;

		if (eventosAtivos.isEmpty())
			existeEventos = false;

		List<Long> eventosComoRevisor = new ArrayList<>();
		List<Long> eventosComoOrganizador = new ArrayList<>();

		for (ParticipacaoEvento participacaoEvento : participacoesComoRevisor) {
			eventosComoRevisor.add(participacaoEvento.getEvento().getId());
		}

		for (ParticipacaoEvento participacaoEvento : participacoesComoOrganizador) {
			eventosComoOrganizador.add(participacaoEvento.getEvento().getId());
		}

		model.addAttribute("existeEventos", existeEventos);
		model.addAttribute("eventosAtivos", eventosAtivos);
		model.addAttribute("eventosComoOrganizador", eventosComoOrganizador);
		model.addAttribute("eventosComoRevisor", eventosComoRevisor);
		return Constants.TEMPLATE_LISTAR_EVENTOS_ATIVOS_REV;
		
		*/

	}

	
	@RequestMapping(value = "/evento/{id}/detalhes", method = RequestMethod.GET)
	public String detalhesEvento(@PathVariable String id, Model model) {
		throw new UnsupportedOperationException("Não implementado.");
		
		/**
		 * Veficar/Alterar se esse código ainda será necessario com as mudanças na visualização
		
		
		Long eventoId = Long.parseLong(id);
		Pessoa pessoa = PessoaLogadaUtil.pessoaLogada();
		List<ParticipacaoEvento> participacoesComoRevisor = participacaoEventoService
				.getEventosDoRevisor(EstadoEvento.ATIVO, pessoa.getId());
		List<Long> eventosComoRevisor = new ArrayList<>();
		Evento evento = eventoService.buscarEventoPorId(eventoId);
		Boolean eventoPrivado = false;

		if (evento.getVisibilidade() == VisibilidadeEvento.PRIVADO) {
			eventoPrivado = true;
		}
		for (ParticipacaoEvento participacaoEvento : participacoesComoRevisor) {
			eventosComoRevisor.add(participacaoEvento.getEvento().getId());
		}
		boolean organizaEvento = evento.getOrganizadores().contains(pessoa);

		model.addAttribute("organizaEvento", organizaEvento);
		model.addAttribute("evento", evento);
		model.addAttribute("eventoPrivado", eventoPrivado);

		int trabalhosSubmetidos = trabalhoService.buscarQuantidadeTrabalhosPorEvento(evento);
		int trabalhosNaoRevisados = trabalhoService.buscarQuantidadeTrabalhosNaoRevisadosPorEvento(evento);
		int trabalhosRevisados = trabalhosSubmetidos - trabalhosNaoRevisados;

		List<Pessoa> organizadores = pessoaService.getOrganizadoresEvento(eventoId);

		for (Pessoa p : organizadores) {
			if (p.getId() == pessoa.getId()) {
				model.addAttribute("gerarCertificado", true);
				break;
			}
		}

		model.addAttribute("numeroTrabalhos", trabalhosSubmetidos);
		model.addAttribute("numeroTrabalhosNaoRevisados", trabalhosNaoRevisados);
		model.addAttribute("numeroTrabalhosRevisados", trabalhosRevisados);
		model.addAttribute("comentarios",
				trabalhoService.buscarQuantidadeTrabalhosRevisadosEComentadosPorEvento(evento));
		model.addAttribute("eventosComoRevisor", eventosComoRevisor);

		return Constants.TEMPLATE_DETALHES_EVENTO_REV;
		
		*/
	}
	
}