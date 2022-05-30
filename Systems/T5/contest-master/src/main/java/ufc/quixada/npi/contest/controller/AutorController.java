package ufc.quixada.npi.contest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.service.*;
import ufc.quixada.npi.contest.util.Constants;
import ufc.quixada.npi.contest.util.PessoaLogadaUtil;
import ufc.quixada.npi.contest.util.RevisaoJSON;
import ufc.quixada.npi.contest.validator.TrabalhoValidator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/autor")
public class AutorController {

	private AutorControllerProduct autorControllerProduct = new AutorControllerProduct();
	public static final String EXTENSAO_PDF = ".pdf";
	private static final String FORA_DO_PRAZO_SUBMISSAO = "FORA_DO_PRAZO_SUBMISSAO";
	private static final String ERRO_EXCLUIR_TRABALHO = "ERRO_EXCLUIR_TRABALHO";
	private static final String TRABALHO_EXCLUIDO_COM_SUCESSO = "TRABALHO_EXCLUIDO_COM_SUCESSO";
	public static final String FORA_DA_DATA_DE_SUBMISSAO = "FORA_DA_DATA_DE_SUBMISSAO";
	public static final String ERRO_CADASTRO_TRABALHO = "ERRO_CADASTRO_TRABALHO";
	public static final String TRABALHO_ENVIADO = "TRABALHO_ENVIADO";
	public static final String FORMATO_ARQUIVO_INVALIDO = "FORMATO_ARQUIVO_INVALIDO";
	private static final String PARTICAPAR_EVENTO_SUCESSO = "PARTICAPAR_EVENTO_SUCESSO";
	private static final String PARTICAPACAO_EVENTO_SUCESSO = "particapacaoEventoSucesso";
	private static final String EVENTO_NAO_EXISTE = "EVENTO_NAO_EXISTE";
	private static final String EVENTO_INEXISTENTE_ERROR = "eventoInexistenteError";
	private static final String PARTICIPAR_EVENTO_INATIVO = "PARTICIPAR_EVENTO_INATIVO";
	private static final String ID_EVENTO_VAZIO_ERROR = "ID_EVENTO_VAZIO_ERROR";
	private static final String EVENTO_VAZIO_ERROR = "eventoVazioError";
	private static final String PARTICIPAR_EVENTO_INATIVO_ERROR = "participarEventoInativoError";
	public static final String ERRO_TRABALHO_EVENTO = "ERRO_TRABALHO_EVENTO";
	public static final String ERRO_REENVIAR = "ERRO_REENVIAR";
	private static final String AUTOR_SEM_PERMISSAO = "AUTOR_SEM_PERMISSAO";
	public static final String AUTOR_SEM_PERMISSAO_REVISAO = "autor/erro_permissao_de_autor";

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private TrabalhoValidator trabalhoValidator;

	@Autowired
	private TrilhaService trilhaService;

	@RequestMapping
	public String index(Model model) {
		Pessoa autorLogado = PessoaLogadaUtil.pessoaLogada();
		/*model.addAttribute("eventosParaParticipar", eventoService.eventosParaParticipar(autorLogado.getId()));
		model.addAttribute("eventoParticipando", eventoService.getMeusEventosComoAutor(autorLogado.getId()));*/
		return Constants.TEMPLATE_INDEX_AUTOR;
	}

	@RequestMapping(value = "/revisao/trabalho/{trabalhoId}", method = RequestMethod.GET)
	public String verRevisao(@PathVariable String trabalhoId, Model model, RedirectAttributes redirect) {
		return autorControllerProduct.verRevisao(trabalhoId, model, redirect);
	}

	@RequestMapping(value = "/participarEvento", method = RequestMethod.GET)
	public String eventosAtivos(Model model) {
		throw new UnsupportedOperationException("Não implementado.");
		/**
		 * Com as mudanças no modelo será preciso verificar a necessidade desse método 
		 * 
		Pessoa autorLogado = PessoaLogadaUtil.pessoaLogada();
		model.addAttribute("eventosParaParticipar", eventoService.eventosParaParticipar(autorLogado.getId()));
		model.addAttribute("eventoParticipando", eventoService.getMeusEventosComoAutor(autorLogado.getId()));
		return Constants.TEMPLATE_INDEX_AUTOR;
		*/
	}

	@RequestMapping(value = "/participarEvento", method = RequestMethod.POST)
	public String participarEvento(@RequestParam String idEvento, Model model, RedirectAttributes redirect) {
		throw new UnsupportedOperationException("Não implementado.");
		
		/**
		 * Com as mudanças no modelo será preciso verificar a necessidade desse método 
		 * 
		if (!eventoService.existeEvento(Long.parseLong(idEvento))) {
			redirect.addFlashAttribute(EVENTO_VAZIO_ERROR, messageService.getMessage(ID_EVENTO_VAZIO_ERROR));
			return "redirect:/autor/participarEvento";
		}

		Pessoa autorLogado = PessoaLogadaUtil.pessoaLogada();
		Evento evento = eventoService.buscarEventoPorId(Long.parseLong(idEvento));

		if (evento != null) {
			if (evento.getEstado() == EstadoEvento.ATIVO) {
				ParticipacaoEvento participacaoEvento = new ParticipacaoEvento();
				participacaoEvento.setEvento(evento);
				participacaoEvento.setPessoa(autorLogado);
				participacaoEvento.setPapel(Tipo.AUTOR);

				participacaoEventoService.adicionarOuEditarParticipacaoEvento(participacaoEvento);
				redirect.addFlashAttribute(PARTICAPACAO_EVENTO_SUCESSO,
						messageService.getMessage(PARTICAPAR_EVENTO_SUCESSO));
			} else {
				redirect.addFlashAttribute(PARTICIPAR_EVENTO_INATIVO_ERROR,
						messageService.getMessage(PARTICIPAR_EVENTO_INATIVO));
				return "redirect:/autor";
			}
		} else {
			redirect.addFlashAttribute(EVENTO_INEXISTENTE_ERROR, messageService.getMessage(EVENTO_NAO_EXISTE));
			return "redirect:/autor";
		}
		return "redirect:/autor/enviarTrabalhoForm/" + idEvento;
		 */
	}

	@RequestMapping(value = "/meusTrabalhos/evento/{eventoId}", method = RequestMethod.GET)
	public String listarMeusTrabalhosEmEventosAtivos(@PathVariable Long eventoId, @RequestParam(required = false, name="coautor") String coautor, Model model) {
		/*Pessoa autorLogado = PessoaLogadaUtil.pessoaLogada();
		List<Evento> eventos = new ArrayList<>();
		if (eventoId != null) {
			Evento evento = eventoService.buscarEventoPorId(eventoId);
			if(evento != null) {
				eventos.add(evento);
			}
		} else {
			if(coautor != null){
				eventos = eventoService.getMeusEventosComoCoautor(autorLogado.getId());
			} else {
				eventos = eventoService.getMeusEventosComoAutor(autorLogado.getId());				
			}
		}
		
		if (eventos != null) {
			model.addAttribute("eventos", eventos);
		}*/
		return Constants.TEMPLATE_MEUS_TRABALHOS_AUTOR;
	}

	@RequestMapping(value = "/meusTrabalhos", method = RequestMethod.GET)
	public String listarMeusTrabalhosEmEventosAtivos(@RequestParam(required= false, name="coautor") String coautor, Model model) {
		return listarMeusTrabalhosEmEventosAtivos(null, coautor, model);
	}

	@RequestMapping(value = "/reenviarTrabalho", method = RequestMethod.POST)
	public String reenviarTrabalhoForm(@RequestParam("trabalhoId") String trabalhoId,
			@RequestParam("eventoId") String eventoId,
			@RequestParam(value = "file", required = true) MultipartFile file, RedirectAttributes redirect) {
		return autorControllerProduct.reenviarTrabalhoForm(trabalhoId, eventoId, file, redirect);
	}


	@RequestMapping(value = "/listarTrabalhos/{id}", method = RequestMethod.GET)
	public String listarTrabalhos(@PathVariable String id, Model model, RedirectAttributes redirect) {
		return autorControllerProduct.listarTrabalhos(id, model, redirect);
	}

	@RequestMapping(value = "/file/{trabalho}", method = RequestMethod.GET, produces = "application/pdf")
	public void downloadPDFFile(@PathVariable("trabalho") Long idTrabalho, HttpServletResponse response)
			throws IOException {
		
		throw new UnsupportedOperationException("Não implementado.");
		
		/**
		 * Refatorar código de verificação de acesso ao arquivo
		 * 
		 * 
		
		Trabalho trabalho = trabalhoService.getTrabalhoById(idTrabalho);
		if (trabalho == null) {
			response.reset();
			response.sendRedirect("/error/500");
			response.getOutputStream().flush();
		} else {
			Long idEvento = trabalho.getEvento().getId();
			if (participacaoEventoService.isOrganizadorDoEvento(PessoaLogadaUtil.pessoaLogada(), idEvento)
					|| participacaoTrabalhoService.isParticipandoDoTrabalho(idTrabalho,
							PessoaLogadaUtil.pessoaLogada().getId())) {
				try {
					String path = trabalho.getPath();
					Path file = Paths.get(path);
					response.setContentType("application/pdf");
					response.addHeader("Content-Disposition", "attachment; filename=" + path);
					Files.copy(file, response.getOutputStream());
					response.getOutputStream().flush();
				} catch (IOException e) {
					e.printStackTrace();
					response.reset();
					response.sendRedirect("/error/404");
					response.addHeader("Status", "404 Not Found");
					response.getOutputStream().flush();
				}
			} else {
				response.reset();
				response.sendRedirect("/error/500");
				response.getOutputStream().flush();
			}
		}
		
		 */
		
	}

	/*@RequestMapping(value = "/excluirTrabalho/", method = RequestMethod.POST)
	public String excluirTrabalho(@RequestParam("trabalhoId") String trabalhoId,
			@RequestParam("eventoId") String eventoId, Model model, RedirectAttributes redirect) {
		try {
			Long idEvento = Long.parseLong(eventoId);
			Long idTrabalho = Long.parseLong(trabalhoId);
			if (trabalhoService.existeTrabalho(idTrabalho) && eventoService.existeEvento(idEvento)) {

				Evento evento = eventoService.buscarEventoPorId(Long.parseLong(eventoId));
				Date dataDeRequisicaoDeExclusao = new Date(System.currentTimeMillis());

				if (evento.getCameraReady().after(dataDeRequisicaoDeExclusao)) {
					Trabalho t = trabalhoService.getTrabalhoById(idTrabalho);
					if (PessoaLogadaUtil.pessoaLogada().equals(t.getAutor())) {
						storageService.deleteArquivo(t.getPath());
						trabalhoService.remover(Long.parseLong(trabalhoId));
						redirect.addFlashAttribute("trabalhoExcluido",
								messageService.getMessage(TRABALHO_EXCLUIDO_COM_SUCESSO));

						return "redirect:/autor/listarTrabalhos/" + eventoId;
					}
					model.addAttribute("erroExcluir", messageService.getMessage(AUTOR_SEM_PERMISSAO));
					return Constants.TEMPLATE_MEUS_TRABALHOS_AUTOR;
				} else {
					redirect.addFlashAttribute("erroExcluir", messageService.getMessage(FORA_DO_PRAZO_SUBMISSAO));
					return "redirect:/autor/listarTrabalhos/" + evento.getId();
				}
			}
			model.addAttribute("erroExcluir", messageService.getMessage(ERRO_EXCLUIR_TRABALHO));
			return Constants.TEMPLATE_MEUS_TRABALHOS_AUTOR;
		} catch (NumberFormatException e) {
			model.addAttribute("erroExcluir", messageService.getMessage(ERRO_EXCLUIR_TRABALHO));
			return Constants.TEMPLATE_MEUS_TRABALHOS_AUTOR;
		}
	}*/

	public boolean validarArquivo(MultipartFile file) {
		return autorControllerProduct.validarArquivo(file);
	}

	public Submissao configuraSubmissao(Submissao submissao, Evento evento) {
		return autorControllerProduct.configuraSubmissao(submissao, evento);
	}

}