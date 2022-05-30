package ufc.quixada.npi.contest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import ufc.quixada.npi.contest.service.EventoService;
import ufc.quixada.npi.contest.service.MessageService;
import ufc.quixada.npi.contest.service.RevisaoService;
import ufc.quixada.npi.contest.service.SubmissaoService;
import ufc.quixada.npi.contest.service.TrabalhoService;
import ufc.quixada.npi.contest.service.StorageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.util.PessoaLogadaUtil;
import ufc.quixada.npi.contest.util.Constants;
import ufc.quixada.npi.contest.util.GetEvento;
import ufc.quixada.npi.contest.model.Trabalho;
import java.util.List;
import ufc.quixada.npi.contest.model.Revisao;
import java.util.Map;
import java.util.ArrayList;
import ufc.quixada.npi.contest.util.RevisaoJSON;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ufc.quixada.npi.contest.model.Submissao;
import java.util.Date;
import ufc.quixada.npi.contest.model.TipoSubmissao;

public class AutorControllerProduct {
	private EventoService eventoService;
	private MessageService messageService;
	private RevisaoService revisaoService;
	private SubmissaoService submissaoService;
	private TrabalhoService trabalhoService;
	private StorageService storageService;

	@RequestMapping(value = "/listarTrabalhos/{id}", method = RequestMethod.GET)
	public String listarTrabalhos(@PathVariable String id, Model model, RedirectAttributes redirect) {
		try {
			Evento evento = eventoService.buscarEventoPorId(Long.parseLong(id));
			Pessoa pessoa = PessoaLogadaUtil.pessoaLogada();
			if (evento != null) {
				return Constants.TEMPLATE_LISTAR_TRABALHO_AUTOR;
			}
			return "redirect:/autor/meusTrabalhos";
		} catch (NumberFormatException e) {
			redirect.addFlashAttribute("erroAoCadastrar",
					messageService.getMessage(AutorController.ERRO_CADASTRO_TRABALHO));
			return "redirect:/autor/meusTrabalhos";
		}
	}

	@RequestMapping(value = "/revisao/trabalho/{trabalhoId}", method = RequestMethod.GET)
	public String verRevisao(@PathVariable String trabalhoId, Model model, RedirectAttributes redirect) {
		Long idTrabalho = Long.parseLong(trabalhoId);
		Trabalho trabalho = trabalhoService.getTrabalhoById(idTrabalho);
		Pessoa autorLogado = PessoaLogadaUtil.pessoaLogada();
		Evento evento = trabalho.getEvento();
		if ((trabalho.getAutor().equals(autorLogado) || trabalho.getCoautores().contains(autorLogado))
				&& !evento.isPeriodoRevisao()) {
			List<Revisao> revisoes = revisaoService.getRevisaoByTrabalho(trabalho);
			if (!revisoes.isEmpty()) {
				model.addAttribute("titulo", trabalho.getTitulo());
				List<Map<String, String>> revisoesWrappers = new ArrayList<>();
				for (Revisao revisao : revisoes) {
					revisoesWrappers.add(RevisaoJSON.fromJson(revisao));
				}
				model.addAttribute("revisoes", revisoesWrappers);
				return Constants.TEMPLATE_REVISAO_AUTOR;
			}
			redirect.addFlashAttribute("revisao_inexistente", messageService.getMessage("REVISAO_INEXISTENTE"));
			return "redirect:/autor/listarTrabalhos/" + GetEvento.getId(evento);
		}
		return AutorController.AUTOR_SEM_PERMISSAO_REVISAO;
	}

	@RequestMapping(value = "/reenviarTrabalho", method = RequestMethod.POST)
	public String reenviarTrabalhoForm(@RequestParam("trabalhoId") String trabalhoId,
			@RequestParam("eventoId") String eventoId,
			@RequestParam(value = "file", required = true) MultipartFile file, RedirectAttributes redirect) {
		Long idEvento = Long.parseLong(eventoId);
		Long idTrabalho = Long.parseLong(trabalhoId);
		try {
			if (trabalhoService.existeTrabalho(idTrabalho) && eventoService.existeEvento(idEvento)) {
				Evento evento = eventoService.buscarEventoPorId(Long.parseLong(eventoId));
				Trabalho trabalho = trabalhoService.getTrabalhoById(idTrabalho);
				Submissao submissao = new Submissao();
				submissao.setTrabalho(trabalho);
				if (validarArquivo(file)) {
					if (evento.isPeriodoSubmissao() || evento.isPeriodoFinal()) {
						if (saveFile(file, trabalho)) {
							submissaoService.adicionarOuEditar(submissao);
							redirect.addFlashAttribute("sucessoEnviarTrabalho",
									messageService.getMessage(AutorController.TRABALHO_ENVIADO));
							trabalhoService.notificarAutoresReenvioTrabalho(evento, trabalho);
							return "redirect:/autor/meusTrabalhos";
						}
					} else {
						redirect.addFlashAttribute("FORA_DA_DATA_DE_SUBMISSAO",
								messageService.getMessage(AutorController.FORA_DA_DATA_DE_SUBMISSAO));
						return "redirect:/autor/listarTrabalhos/" + idEvento;
					}
				} else {
					redirect.addFlashAttribute("arquivoInvalido",
							messageService.getMessage(AutorController.FORMATO_ARQUIVO_INVALIDO));
					return "redirect:/autor/listarTrabalhos/" + idEvento;
				}
			}
			redirect.addAttribute("ERRO_TRABALHO_EVENTO",
					messageService.getMessage(AutorController.ERRO_TRABALHO_EVENTO));
			return "redirect:/autor/listarTrabalhos/" + idEvento;
		} catch (NumberFormatException e) {
			redirect.addAttribute("ERRO_REENVIAR", messageService.getMessage(AutorController.ERRO_REENVIAR));
			return "redirect:/autor/listarTrabalhos/" + idEvento;
		}
	}

	public Submissao configuraSubmissao(Submissao submissao, Evento evento) {
		submissao.setDataSubmissao(new Date(System.currentTimeMillis()));
		if (evento.isPeriodoSubmissao()) {
			submissao = setTipoSubmissao(submissao, TipoSubmissao.PARCIAL);
		} else if (evento.isPeriodoFinal()) {
			submissao = setTipoSubmissao(submissao, TipoSubmissao.FINAL);
		}
		return submissao;
	}
	
	private Submissao setTipoSubmissao(Submissao submissao, TipoSubmissao tipoSubmissao) {
		submissao.setTipoSubmissao(tipoSubmissao);
		return submissao;
	}

	public boolean validarArquivo(MultipartFile file) {
		return file.getOriginalFilename().endsWith(AutorController.EXTENSAO_PDF) && !file.isEmpty();
	}

	public boolean saveFile(MultipartFile file, Trabalho trabalho) {
		try {
			trabalho.setArquivo(storageService.store(file));
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}
}