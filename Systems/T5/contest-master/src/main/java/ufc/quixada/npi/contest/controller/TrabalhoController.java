package ufc.quixada.npi.contest.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.quixada.npi.contest.model.Avaliacao;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.model.Revisao;
import ufc.quixada.npi.contest.model.Trabalho;
import ufc.quixada.npi.contest.service.StorageService;
import ufc.quixada.npi.contest.service.TrabalhoService;
import ufc.quixada.npi.contest.util.GetEvento;
import ufc.quixada.npi.contest.util.PessoaLogadaUtil;

@Controller
@RequestMapping("/trabalho")
public class TrabalhoController {

	@Autowired
	private TrabalhoService trabalhoService;

	@Autowired
	private StorageService storageService;

	// OK
	@RequestMapping(value = "/{trabalho}")
	public String visualizarTrabalho(@PathVariable Trabalho trabalho, Authentication authentication, Model model, RedirectAttributes attributes) {
		Pessoa usuario = (Pessoa) authentication.getPrincipal();
		if (trabalho == null) {
			attributes.addFlashAttribute("error", "Trabalho não encontrado");
			return "redirect:/";
		}
		// Verifica se é autor, coautor, revisor, chefe da sessão ou organizador do evento
		if (trabalho.getAutor().equals(usuario) || trabalho.isCoautor(usuario) || trabalho.getTrabalhoProduct().isRevisor(usuario) ||
				trabalho.getEvento().isOrganizador(usuario) || (trabalho.getSessao() != null && usuario.equals(trabalho.getSessao().getResponsavel()))) {
			model.addAttribute("trabalho", trabalho);
			model.addAttribute("avaliacoes", Revisao.Classificacao.values());
			model.addAttribute("resultados", Arrays.asList(Avaliacao.APROVADO, Avaliacao.RESSALVAS, Avaliacao.REPROVADO));
			return "trabalho/detalhe-trabalho";
		}
		attributes.addFlashAttribute("error", "Você não tem permissão para acessar esse trabalho");
		return "redirect:/evento/" + GetEvento.getId(trabalho.getEvento());
	}



	// OK
	@RequestMapping("/{idEvento}/submeter")
	@PreAuthorize("#evento.isPeriodoSubmissao()")
	public String getSubmeterTrabalho(@PathVariable("idEvento") Evento evento, Model model) {
		model.addAttribute("evento", evento);
		model.addAttribute("trabalho", new Trabalho());
		return "trabalho/submissao-trabalho";
	}

	@RequestMapping(value = "/{idEvento}/submeter", method = RequestMethod.POST)
	@PreAuthorize("#evento.isPeriodoSubmissao()")
	public String submeterTrabalho(Trabalho trabalho,
			@RequestParam("file") MultipartFile file,
			@PathVariable("idEvento") Evento evento,
			@RequestParam(name = "orientadorId", required = false) Pessoa orientador,
			@RequestParam(name = "coautoresId", required = false) List<Pessoa> coautores,
			@RequestParam(name = "bolsistasId", required = false) List<Pessoa> bolsistas,
			Model model, RedirectAttributes redirectAttributes) {

		if (trabalhoInvalido(trabalho, orientador, file)) {
			model.addAttribute("error", "Por favor, prencha todos os campos obrigatórios.");
			model.addAttribute("evento", evento);
			model.addAttribute("trabalho", trabalho);
			return "trabalho/submissao-trabalho";
		}

		trabalho.setEvento(evento);
		trabalho.setAutor(PessoaLogadaUtil.pessoaLogada());
		trabalho.setArquivo(storageService.store(file));
		trabalho.setOrientador(orientador);
		trabalho.setCoautores(coautores);
		trabalho.setBolsistas(bolsistas);
		
		trabalho.setCriadaEm(new Date());
		trabalho.setAtualizadaEm(new Date());


		trabalhoService.adicionarTrabalho(trabalho);

		redirectAttributes.addFlashAttribute("info", "Trabalho submetido com sucesso!");
		return "redirect:/trabalho/" + trabalho.getId();

	}

	// OK
	@RequestMapping("/{idEvento}/editar/{idTrabalho}")
	@PreAuthorize("#evento.isPeriodoSubmissao()")
	public String getEdicaoTrabalho(@PathVariable("idEvento") Evento evento, @PathVariable("idTrabalho") Trabalho trabalho, Model model) {

		model.addAttribute("evento", evento);
		model.addAttribute("trabalho", trabalho);
		return "trabalho/editar-submissao-trabalho";
	}

	// OK
	@RequestMapping(value = "/{idEvento}/editar/{idTrabalho}", method = RequestMethod.POST)
	@PreAuthorize("#evento.isPeriodoSubmissao() && #trabalho.isAutorOuCoautor()")
	public String editarTrabalho(Trabalho trabalho, @PathVariable("idEvento") Evento evento,
			@RequestParam("file") MultipartFile file, @RequestParam(name = "orientadorId", required = false) Pessoa orientador,
			@RequestParam(name = "coautoresId", required = false) List<Pessoa> coautores, Model model,
			RedirectAttributes redirectAttributes) {

		if (trabalhoInvalido(trabalho, orientador, file)) {
			model.addAttribute("error", "Por favor, prencha todos os campos obrigatórios.");
			model.addAttribute("evento", evento);
			model.addAttribute("trabalho", trabalho);
			return "trabalho/editar-submissao-trabalho";
		}

		Trabalho trabalhoBD = trabalhoService.getTrabalhoById(trabalho.getId());

		storageService.edit(file, trabalhoBD.getArquivo().getId());

		trabalhoBD.setTitulo(trabalho.getTitulo());
		trabalhoBD.setModalidadeApresentacao(trabalho.getModalidadeApresentacao());
		trabalhoBD.setModalidadeSubmissao(trabalho.getModalidadeSubmissao());
		trabalhoBD.setResumo(trabalho.getResumo());
		trabalhoBD.setPalavrasChave(trabalho.getPalavrasChave());
		trabalhoBD.setCoautores(coautores);
		trabalhoBD.setAtualizadaEm(new Date());

		trabalhoService.adicionarTrabalho(trabalhoBD);

		redirectAttributes.addFlashAttribute("info", "Alterações realizadas com sucesso!");
		return "redirect:/trabalho/" + trabalho.getId();
	}

	@RequestMapping(value = "/{idEvento}/excluir/{idTrabalho}")
	@PreAuthorize("#evento.isPeriodoSubmissao() && #trabalho.isAutorOuCoautor()")
	public String removerTrabalho(@PathVariable("idEvento") Evento evento, @PathVariable("idTrabalho") Trabalho trabalho, RedirectAttributes redirectAttributes) {
		
		trabalhoService.remover(trabalho.getId());
		storageService.delete(trabalho.getArquivo().getId());

		redirectAttributes.addFlashAttribute("info", "Trabalho removido com sucesso!");
		return "redirect:/evento/" + GetEvento.getId(evento) + "/submissoes";
	}

	// OK
	@RequestMapping(value = "/{idEvento}/submissao-final/{idTrabalho}", method = RequestMethod.POST)
	@PreAuthorize("#evento.isPeriodoSubmissaoFinal() && #trabalho.isAutorOuCoautor()")
	public String submeterTrabalhoFinal(@PathVariable("idEvento") Evento evento, @PathVariable("idTrabalho") Trabalho trabalho,
			@RequestParam("file") MultipartFile file, Model model,
			RedirectAttributes redirectAttributes) {

		storageService.edit(file, trabalho.getArquivo().getId());

		trabalho.setAtualizadaEm(new Date());
		trabalhoService.adicionarTrabalho(trabalho);


		redirectAttributes.addFlashAttribute("info", "Submissão final realizada com sucesso!");
		return "redirect:/trabalho/" + trabalho.getId();
	}

	private boolean trabalhoInvalido(Trabalho trabalho, Pessoa orientador, MultipartFile file) {
		return (null == trabalho.getTitulo() || trabalho.getTitulo().isEmpty() || null == trabalho.getTrilha()
				|| null == trabalho.getTrilha().getId() || null == trabalho.getModalidadeApresentacao()
				|| null == trabalho.getModalidadeApresentacao().getId() || null == trabalho.getModalidadeSubmissao()
				|| null == trabalho.getModalidadeSubmissao().getId() || null == file
				|| file.isEmpty());
	}

	/*
	 * @RequestMapping(value = "/enviarTrabalhoForm/{id}", method =
	 * RequestMethod.GET) public String enviarTrabalhoForm(@PathVariable String id,
	 * Model model, RedirectAttributes redirect) { try { Long idEvento =
	 * Long.parseLong(id);
	 *
	 * if (eventoService.existeEvento(idEvento)) { List<Trilha> trilhas =
	 * trilhaService.buscarTrilhas(Long.parseLong(id)); Trabalho trabalho = new
	 * Trabalho();
	 *
	 * model.addAttribute("trabalho", trabalho); model.addAttribute("eventoId", id);
	 * model.addAttribute("trilhas", trilhas); model.addAttribute("pessoa",
	 * PessoaLogadaUtil.pessoaLogada()); return
	 * Constants.TEMPLATE_ENVIAR_TRABALHO_FORM_AUTOR; } return
	 * "redirect:/autor/meusTrabalhos"; } catch (NumberFormatException e) {
	 * redirect.addFlashAttribute("error",
	 * messageService.getMessage(ID_EVENTO_VAZIO_ERROR)); return
	 * "redirect:/autor/meusTrabalhos"; } }
	 */

	/*
	 * @RequestMapping(value = "/enviarTrabalhoForm", method = RequestMethod.POST)
	 * public String enviarTrabalhoForm(@Valid Trabalho trabalho, BindingResult
	 * result, Model model,
	 *
	 * @RequestParam(value = "file", required = true) MultipartFile file,
	 *
	 * @RequestParam("eventoId") String eventoId, @RequestParam(required = false)
	 * String trilhaId, RedirectAttributes redirect, HttpServletRequest request) {
	 *
	 * Evento evento; Trilha trilha; Submissao submissao;
	 *
	 * try { String url = request.getScheme() + "://" + request.getServerName() +
	 * ":" + request.getServerPort() + request.getContextPath(); Long idEvento =
	 * Long.parseLong(eventoId); Long idTrilha = Long.parseLong(trilhaId);
	 *
	 * evento = eventoService.buscarEventoPorId(idEvento); trilha =
	 * trilhaService.get(idTrilha, idEvento);
	 *
	 * if (evento == null || trilha == null) {
	 * redirect.addFlashAttribute("erroAoCadastrar",
	 * messageService.getMessage(ERRO_CADASTRO_TRABALHO)); return
	 * "redirect:/autor/meusTrabalhos"; }
	 *
	 * trabalho.setEvento(evento); trabalho.setTrilha(trilha);
	 *
	 *//**
		 * Definir estratégia para adicioanar Autor e Coautores. Com as mudanças no
		 * modelo de dados será preciso adicionar o Orientador também.
		 *
		 * List<Pessoa> coautores = new ArrayList<Pessoa>(); if
		 * (trabalho.getParticipacoes() != null) { for (ParticipacaoTrabalho
		 * participacao : trabalho.getParticipacoes()) {
		 *
		 * Pessoa coautor =
		 * pessoaService.getByEmail(participacao.getPessoa().getEmail());
		 *
		 * if (coautor == null) {
		 *
		 * coautor = participacao.getPessoa();
		 * eventoService.adicionarCoAutor(coautor.getEmail(), evento, url);
		 *
		 * coautor = pessoaService.getByEmail(participacao.getPessoa().getEmail());
		 *
		 * coautor.setNome(participacao.getPessoa().getNome());
		 * pessoaService.addOrUpdate(coautor); }else{ coautores.add(coautor);
		 * if(!participacaoEventoService.isCoautorDoEvento(idEvento, coautor) &&
		 * PessoaLogadaUtil.pessoaLogada() != coautor){ ParticipacaoEvento
		 * participacaoEvento = new ParticipacaoEvento(Tipo.COAUTOR, coautor, evento);
		 * participacaoEventoService.adicionarOuEditarParticipacaoEvento(participacaoEvento);
		 * } } } } trabalho.setAutores(PessoaLogadaUtil.pessoaLogada(), coautores);
		 *//*
			 *
			 * submissao = new Submissao(); submissao.setTrabalho(trabalho);
			 *
			 * } catch (NumberFormatException e) {
			 * redirect.addFlashAttribute("erroAoCadastrar",
			 * messageService.getMessage(ERRO_CADASTRO_TRABALHO)); return
			 * "redirect:/autor/meusTrabalhos"; }
			 *
			 * trabalhoValidator.validate(trabalho, result); if (result.hasErrors()) {
			 * List<Trilha> trilhas = trilhaService.buscarTrilhas(Long.parseLong(eventoId));
			 * model.addAttribute("eventoId", eventoId); model.addAttribute("trilhas",
			 * trilhas); model.addAttribute("autor", PessoaLogadaUtil.pessoaLogada());
			 * return Constants.TEMPLATE_ENVIAR_TRABALHO_FORM_AUTOR; } else { if
			 * (validarArquivo(file)) { if (evento.isPeriodoInicial()) { if (saveFile(file,
			 * trabalho)) { submissaoService.adicionarOuEditar(submissao);
			 * redirect.addFlashAttribute("sucessoEnviarTrabalho",
			 * messageService.getMessage(TRABALHO_ENVIADO));
			 *
			 * trabalhoService.notificarAutoresEnvioTrabalho(evento, trabalho);
			 *
			 * return "redirect:/autor/meusTrabalhos"; } else { return "redirect:/erro/500";
			 * } } else { redirect.addFlashAttribute("foraDoPrazoDeSubmissao",
			 * messageService.getMessage(FORA_DA_DATA_DE_SUBMISSAO)); return
			 * "redirect:/autor/enviarTrabalhoForm/" + eventoId; } } else {
			 * redirect.addFlashAttribute("erro",
			 * messageService.getMessage(FORMATO_ARQUIVO_INVALIDO)); return
			 * "redirect:/autor/enviarTrabalhoForm/" + eventoId; } } }
			 */

}
