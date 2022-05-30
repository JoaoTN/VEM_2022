package ufc.quixada.npi.contest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.repository.EventoRepository;
import ufc.quixada.npi.contest.repository.TrabalhoRepository;
import ufc.quixada.npi.contest.util.GetEvento;
import ufc.quixada.npi.contest.util.GetPessoa;
import ufc.quixada.npi.contest.validator.ContestException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventoService {

	private static final String TITULO_EMAIL_ORGANIZADOR = "TITULO_EMAIL_CONVITE_ORGANIZADOR";
	private static final String TEXTO_EMAIL_ORGANIZADOR = "TEXTO_EMAIL_CONVITE_ORGANIZADOR";
	private static final String ASSUNTO_EMAIL_CONFIRMACAO = "ASSUNTO_EMAIL_CONFIRMACAO";
	private static final String ASSUNTO_EMAIL_CONFIRMACAO_REENVIO = "ASSUNTO_EMAIL_CONFIRMACAO_REENVIO";
	private static final String TEXTO_EMAIL_CONFIRMACAO = ".Fique atento aos prazos, o próximo passo será a fase das revisões, confira no edital os prazos. Boa sorte!";

	private Logger logger = LoggerFactory.getLogger(EventoService.class);

	@Autowired
	EventoRepository eventoRepository;

	@Autowired
	TrabalhoRepository trabalhoRepository;

	@Autowired
	PessoaService pessoaService;

	@Autowired
	MessageService messageService;

	@Autowired
	EnviarEmailService emailService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private TrilhaService trilhaService;

	public List<Evento> findAll() {
		return eventoRepository.findAll();
	}

	public void adicionarOuAtualizarEvento(Evento evento) throws ContestException {
		periodoSubimissao(evento);
		prazoNotificao(evento);
		cameraReady(evento);
		
		eventoRepository.save(evento);
	}
	
	public void periodoSubimissao(Evento evento) throws ContestException {
		if (evento.getTerminoSubmissao().before(evento.getInicioSubmissao())){
			throw new ContestException("O t�rmino da submiss�o deve ser posterior ao in�cio da submiss�o");
		}
	}
	
	public void prazoNotificao(Evento evento) throws ContestException {
		if (!evento.getPrazoNotificacao().after(evento.getTerminoSubmissao())) {
			throw new ContestException("O prazo de notifica��o deve ser posterior ao t�rmino de submiss�o");
		}
	}
	
	public void cameraReady(Evento evento) throws ContestException {
		if (!evento.getCameraReady().after(evento.getPrazoNotificacao())) {
			throw new ContestException("O camera ready deve ser posterior ao prazo de notifica��o");
		}
	}

	public boolean removerEvento(Long id) {
		if (eventoRepository.findOne(id) != null) {
			eventoRepository.delete(id);
			return true;
		}

		return false;
	}

	public Evento buscarEventoPorId(Long id) {
		return eventoRepository.findOne(id);
	}

	public List<Evento> buscarEventos() {
		return (List<Evento>) eventoRepository.findAll();
	}

	public Boolean existeEvento(Long id) {
		if (id == null || id.toString().isEmpty()) {
			return false;
		} else {
			return eventoRepository.exists(id);
		}
	}

	public List<Evento> buscarEventoPorEstado(EstadoEvento estado) {
		return eventoRepository.findEventoByEstado(estado);
	}

	public List<Evento> buscarEventosAtivosEPublicos() {
		return eventoRepository.findEventosAtivosEPublicos();
	}

	/*
	 * public List<Evento> eventosParaParticipar(Long idPessoa) { return
	 * eventoRepository.eventosParaParticipar(idPessoa); }
	 */

	/*
	 * public List<Evento> getMeusEventos(Long id) { return
	 * eventoRepository.findDistinctEventoByParticipacoesPessoaId(id); }
	 */

	/*
	 * public List<Evento> getMeusEventosComoAutor(Long idAutor) { return
	 * eventoRepository.eventosPorPapel(idAutor, Tipo.AUTOR); }
	 * 
	 * public List<Evento> getMeusEventosComoCoautor(Long idAutor) { return
	 * eventoRepository.eventosPorPapel(idAutor, Tipo.COAUTOR); }
	 */

	/*
	 * public List<Evento> getMeusEventosInativosComoOrganizador(Long idOrganizador)
	 * { return eventoRepository.eventosComoOrganizadorEstado(idOrganizador,
	 * EstadoEvento.INATIVO); }
	 */

	/*
	 * public List<Evento> getMeusEventosAtivosComoRevisor(Long idRevisor) { return
	 * eventoRepository.eventosPorPapelEstado(idRevisor, Tipo.REVISOR,
	 * EstadoEvento.ATIVO); }
	 */

	public List<Evento> getEventosByEstadoEVisibilidadePublica(EstadoEvento estado) {
		return eventoRepository.findEventoByEstadoAndVisibilidade(estado, VisibilidadeEvento.PUBLICO);
	}

	public void notificarPessoasParticipantesNoTrabalhoMomentoDoEnvioDoArtigo(Trabalho trabalho, String email,
			Evento evento) {
		String assunto = messageService.getMessage(ASSUNTO_EMAIL_CONFIRMACAO) + " " + trabalho.getTitulo();
		String corpo = "Olá, seu trabalho intitulado " + trabalho.getTitulo()
				+ " foi enviado com sucesso para o evento " + evento.getNome() + TEXTO_EMAIL_CONFIRMACAO;
		String titulo = "[CONTEST] Confirmação de envio do trabalho: " + trabalho.getTitulo();

		emailService.enviarEmail(titulo, assunto, email, corpo);
	}

	public void notificarPessoasParticipantesNoTrabalhoMomentoDoReenvioDoArtigo(Trabalho trabalho, String email,
			Evento evento) {
		String assunto = messageService.getMessage(ASSUNTO_EMAIL_CONFIRMACAO_REENVIO) + " " + trabalho.getTitulo();
		String corpo = "Olá, uma nova versão do seu trabalho intitulado " + trabalho.getTitulo()
				+ " foi reenviado com sucesso para o evento " + evento.getNome();
		String titulo = "[CONTEST] Confirmação de reenvio do trabalho: " + trabalho.getTitulo();

		emailService.enviarEmail(titulo, assunto, email, corpo);
	}

	public void notificarAutoresTrabalhoAdicionadoASessao(Trabalho trabalho, String email) {

		String assunto = "Seu trabalho " + " " + trabalho.getTitulo() + " foi adicionado à uma sessão";
		String corpo = "Olá, seu trabalho intitulado " + trabalho.getTitulo() + " "
				+ " foi adicionado com sucesso na sessao " + trabalho.getSessao().getNome() + " no evento: "
				+ trabalho.getEvento().getNome() + " Data : " + trabalho.getSessao().getDataSecao() + " Local : "
				+ trabalho.getSessao().getLocal();
		String titulo = "[CONTEST] Notificação de adição do trabalho: " + " " + trabalho.getTitulo() + " à sessão: "
				+ trabalho.getSessao().getNome();
		emailService.enviarEmail(titulo, assunto, email, corpo);
	}

	public boolean notificarPessoaAoAddTrabalho(Evento evento, String email, String corpo) {
		String nameEvento = evento.getNome();
		String assunto = messageService.getMessage(TITULO_EMAIL_ORGANIZADOR) + " " + nameEvento;
		String titulo = "[CONTEST] Convite para o Evento: " + nameEvento;

		return emailService.enviarEmail(titulo, assunto, email, corpo);
	}

	public void adicionarOrganizador(Evento evento, Pessoa pessoa) {

		if (null == evento.getOrganizadores()) {
			evento.setOrganizadores(new ArrayList<Pessoa>());
		}

		if (!evento.getOrganizadores().contains(pessoa)) {
			evento.getOrganizadores().add(pessoa);
			try {
				adicionarOuAtualizarEvento(evento);
			} catch (ContestException e) {
				logger.error(e.getMessage());
			}
		}

		emailService.enviarEmail("Contest", "Equipe de organização", GetPessoa.getEmail(pessoa), "Você foi incluído(a) na equipe de organização do evento \"" + evento.getNome() + "\"");
	}

	public void excluirOrganizador(Evento evento, Pessoa pessoa) {
		List<Pessoa> organizadores = evento.getOrganizadores();
		if (null != organizadores) {
			organizadores.removeIf(p -> p == pessoa);
			try {
				adicionarOuAtualizarEvento(evento);
			} catch (ContestException e) {
				logger.error(e.getMessage());
			}
		}
	}

	public void adicionarRevisor(Evento evento, Pessoa pessoa) {

		if (null == evento.getRevisores()) {
			evento.setRevisores(new ArrayList<Pessoa>());
		}

		if (!evento.getRevisores().contains(pessoa)) {
			evento.getRevisores().add(pessoa);
			try {
				adicionarOuAtualizarEvento(evento);
			} catch (ContestException e) {
				logger.error(e.getMessage());
			}
		}

		emailService.enviarEmail("Contest", "Equipe de revisão", GetPessoa.getEmail(pessoa), "Você foi incluído(a) na equipe de revisão do evento \"" + evento.getNome() + "\"");
	}

	public void excluirRevisor(Evento evento, Pessoa pessoa) {

		boolean x = trabalhoRepository.existTrablhoAlocado(GetEvento.getId(evento), GetPessoa.getId(pessoa));
		
		if (null != evento.getRevisores() && !x) {
			evento.getRevisores().removeIf(p -> p == pessoa);
			try {
				adicionarOuAtualizarEvento(evento);
			} catch (ContestException e) {
				logger.error(e.getMessage());
			}
		}
	}


	public List<Pessoa> findRevisores(Evento evento, String nome) {
		return eventoRepository.findRevisores(evento, nome);
	}
}
