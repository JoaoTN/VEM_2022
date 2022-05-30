package ufc.quixada.npi.contest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.repository.TrabalhoRepository;
import ufc.quixada.npi.contest.util.GetEvento;
import ufc.quixada.npi.contest.util.GetPessoa;
import ufc.quixada.npi.contest.util.PessoaLogadaUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TrabalhoService {
	
	private Avaliacao APROVADO = Avaliacao.APROVADO;
	private Avaliacao RESSALVAS = Avaliacao.RESSALVAS;
	private Avaliacao REPROVADO = Avaliacao.REPROVADO;
	private Avaliacao NAO_REVISADO = Avaliacao.NAO_REVISADO;

	@Autowired
	private TrabalhoRepository trabalhoRepository;

	@Autowired
	private EventoService eventoService;

	@Autowired
	private RevisaoService revisaoService;

	@Autowired
	private EnviarEmailService emailService;

	public Trabalho getTrabalhoById(Long idTrabalho) {
		return trabalhoRepository.findOne(idTrabalho);
	}

	public boolean existeTrabalho(Long idTrabalho) {
		return trabalhoRepository.exists(idTrabalho);
	}

	public List<Trabalho> getTrabalhosEvento(Evento evento) {
		return trabalhoRepository.getByEventoOrderByTitulo(evento);
	}

	public List<Trabalho> getTrabalhosSemSessaoNoEvento(Evento evento) {
		return trabalhoRepository.getTrabalhosSemSessaoNoEvento(GetEvento.getId(evento));
	}

	public List<Trabalho> getTrabalhosTrilha(Trilha trilha) {
		return trabalhoRepository.findByTrilha(trilha);
	}

	public void adicionarTrabalho(Trabalho trabalho) {
		trabalhoRepository.save(trabalho);
		String title = trabalho.getTitulo();
		String nameEvento = trabalho.getEvento().getNome();

		sendEmail(trabalho.getAutor().getEmail(), title, nameEvento);
		
		sendEmailOrientador(trabalho, title, nameEvento);
		
		sendEmailCoautores(trabalho, title, nameEvento);
	}
	
	private void sendEmailOrientador(Trabalho trabalho, String title, String nameEvento) {
		Pessoa orientador = trabalho.getOrientador();
		if (orientador != null) {
			sendEmail(orientador.getEmail(), title, nameEvento);
		}
	}
	
	private void sendEmailCoautores(Trabalho trabalho, String title, String nameEvento) {
		List<Pessoa> coautores = trabalho.getCoautores();
		if(coautores != null && !coautores.isEmpty()) {
			for(Pessoa pessoa : coautores) sendEmail(GetPessoa.getEmail(pessoa), title, nameEvento);
		}
	}
	
	private void sendEmail(String email, String title, String name) {
		emailService.enviarEmail("Contest", "Submissão de trabalho", email, getCorpoEmailSubmisaoTrabalho(title, name));
	}

	private String getCorpoEmailSubmisaoTrabalho(String nomeTrabalho, String nomeEvento) {
		return "O trabalho \"" + nomeTrabalho + "\" foi submetido com sucesso no evento \"" + nomeEvento + "\"";
	}

	public List<Trabalho> getTrabalhosParaRevisar(Pessoa revisor, Evento evento) {
		return trabalhoRepository.getTrabalhosParaRevisar(revisor, evento);
	}

	public List<Trabalho> getTrabalhosRevisadosDoRevisor(Long idRevisor, Long idEvento) {
		return trabalhoRepository.getTrabalhosRevisados(idRevisor, idEvento);
	}
	
	public List<Trabalho> getTrabalhosRevisadosComentadosByEvento(Long evento){
		return trabalhoRepository.getTrabalhoRevisadoComentadoEvento(evento);
	}

	public List<Trabalho> getTrabalhosBySessao(Sessao sessao) {
		return trabalhoRepository.getTrabalhoBySessao(sessao);
	}

	/*public List<Trabalho> getTrabalhosComoAutorECoautorNoEvento(Pessoa pessoa, Evento evento) {
		return trabalhoRepository.getTrabalhoComoAutorECoautorNoEvento(pessoa.getId(), evento.getId());
	}*/
	public void remover(Long id) {
		trabalhoRepository.delete(id);
	}

	public void removerSessao(Trabalho trabalho) {
		trabalho.setSessao(null);
		trabalhoRepository.save(trabalho);
	}

	public Avaliacao mensurarAvaliacoes(Trabalho trabalho) {
		int numeroDeAprovacao = 0;
		int numeroDeReprovacao = 0;
		int numeroDeRessalvas = 0;
		int numeroRevisoes = 0;
		
		TrabalhoProduct trabalhoProduct = trabalho.getTrabalhoProduct();
		List<Revisao> revisoes = trabalhoProduct.getRevisoes();
		// List<Revisao> revisoes = trabalho.getRevisoes();

		if (revisoes != null) {
			numeroRevisoes = revisoes.size();
			for (Revisao revisao : revisoes) {
				Avaliacao avaliacao = revisao.getAvaliacao();

				if (avaliacao == Avaliacao.APROVADO) {
					numeroDeAprovacao++;
				} else if (avaliacao == Avaliacao.REPROVADO) {
					numeroDeReprovacao++;
				} else if (avaliacao == Avaliacao.RESSALVAS) {
					numeroDeRessalvas++;
				}
			}
		}

		if (numeroDeAprovacao != 0 && numeroDeReprovacao != 0) {
			return Avaliacao.MODERACAO;
		}

		int maioria = (numeroRevisoes / 2) + 1;

		if (numeroDeReprovacao == numeroRevisoes || numeroDeReprovacao >= maioria)
			return Avaliacao.REPROVADO;

		if (numeroDeAprovacao == numeroRevisoes || numeroDeAprovacao >= maioria) {
			return Avaliacao.APROVADO;
		}

		if (numeroDeRessalvas == numeroRevisoes) {
			return Avaliacao.RESSALVAS;
		}

		return Avaliacao.MODERACAO;
	}
/*
	public List<String> pegarConteudo(Trabalho trabalho) {

		String conteudoAux;
		String conteudo;

		List<String> resultadoAvaliacoes = new ArrayList<>();

		TrabalhoProduct trabalhoProduct = trabalho.getTrabalhoProduct();
		List<Revisao> revisoes = trabalhoProduct.getRevisoes();
		StringBuilder bld = new StringBuilder();
		for (Revisao revisao : revisoes) {
			String content = revisao.getConteudo();
			conteudo = content.substring(1, content.length() - 1);
			bld.append("REVISOR : " + revisao.getRevisor().getNome().toUpperCase() + " , TRABALHO: "
					+ trabalho.getId().toString());

			while (!conteudo.isEmpty()) {
				if (conteudo.contains(",")) {
					conteudoAux = conteudo.substring(0, conteudo.indexOf(','));
					if (!conteudoAux.contentEquals("comentarios")) {
						bld.append((" ," + contentFormat(conteudoAux)));
						conteudoAux = conteudo.substring(conteudo.indexOf(',') + 1);
						conteudo = conteudoAux;
					}
				} else {
					resultadoAvaliacoes.add((bld + (" , AVALIAÇÃO FINAL : " + revisao.getAvaliacao()).toString()));
					conteudo = "";
				}
			}
			bld.delete(0, bld.length());
		}

		return resultadoAvaliacoes;
	}
	
	private String contentFormat(String conteudo) {
		return conteudo.replaceAll("\"", " ").replaceAll("_", " ")
				.replaceAll("avaliacao", "AVALIAÇÃO").replaceAll("OTIMO", "ÓTIMO")
				.replaceAll("merito", "MÉRITO").replaceAll("relevancia", "RELEVÂNCIA").toUpperCase();
	}
*/
	public List<Trabalho> buscarTodosTrabalhosDaSessao(Long idSessao) {
		return trabalhoRepository.findTrabalhoBySessaoId(idSessao);

	}

	public void notificarAutoresEnvioTrabalho(Evento evento, Trabalho trabalho) {
		eventoService.notificarPessoasParticipantesNoTrabalhoMomentoDoEnvioDoArtigo(trabalho, PessoaLogadaUtil.pessoaLogada().getEmail(),
				evento);

		throw new UnsupportedOperationException("Não implementado.");
		
		/**
		 * Pensar as alterações necessarias 
		 * 

		List<Pessoa> coautores = trabalho.getCoAutoresDoTrabalho();
		for (Pessoa coautor : coautores) {
			eventoService.notificarPessoasParticipantesNoTrabalhoMomentoDoEnvioDoArtigo(trabalho, coautor.getEmail(), evento);
		}
		
		*/
	}
	public void notificarAutoresReenvioTrabalho(Evento evento, Trabalho trabalho) {
		eventoService.notificarPessoasParticipantesNoTrabalhoMomentoDoReenvioDoArtigo(trabalho, PessoaLogadaUtil.pessoaLogada().getEmail(),
				evento);
		throw new UnsupportedOperationException("Não implementado.");
		
		/**
		 * Pensar as alterações necessarias 
		 * 

		List<Pessoa> coautores = trabalho.getCoAutoresDoTrabalho();
		for (Pessoa coautor : coautores) {
			eventoService.notificarPessoasParticipantesNoTrabalhoMomentoDoReenvioDoArtigo(trabalho, coautor.getEmail(), evento);
		}
		
		*/

	}
	public void notificarAutorPrincipalDoArtigo(Sessao sessao) {
		List<Trabalho> trabalhosSessao = getTrabalhosBySessao(sessao);
		for (Trabalho trabalho : trabalhosSessao) {
			eventoService.notificarAutoresTrabalhoAdicionadoASessao(trabalho, trabalho.getAutor().getEmail());

		}

	}

	// OK
    public List<Trabalho> getTrabalhos(Evento evento, Pessoa autor) {
		return trabalhoRepository.findByEventoAndAutor(evento, autor);
    }

	// OK
    public List<Trabalho> getTrabalhosAguardandoRevisor(Evento evento) {
		return trabalhoRepository.getTrabalhosAguardandoRevisor(evento);
	}

	// OK
	public List<Trabalho> getTrabalhosAguardandoRevisao(Evento evento) {
		return trabalhoRepository.getTrabalhosByAvaliacao(evento, Arrays.asList(Avaliacao.NAO_REVISADO));
	}

	// OK
	public List<Trabalho> getTrabalhosRevisados(Evento evento) {
		return trabalhoRepository.getTrabalhosByAvaliacao(evento, Arrays.asList(APROVADO, REPROVADO, RESSALVAS));
	}

	// OK
	public void alocarRevisores(Trabalho trabalho, List<Pessoa> revisores) {
		for (Pessoa pessoa : revisores) {
			if (trabalho.getTrabalhoProduct().isRevisor(pessoa)) {
				continue;
			}
			Revisao revisao = new Revisao();
			revisao.setRevisor(pessoa);
			revisao.setAvaliacao(Avaliacao.NAO_REVISADO);
			revisao.setTrabalho(trabalho);
			revisaoService.addOrUpdate(revisao);
		}
	}

	public void atualizar(Trabalho trabalho) {
		trabalhoRepository.save(trabalho);
	}

	@RequestMapping(value = "/sessao/presenca", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String atibuirPresenca(@RequestBody PresencaJsonWrapper dadosPresenca) {
		Trabalho trabalho = getTrabalhoById(dadosPresenca.getTrabalhoId());
		if (trabalho != null) {
			trabalho.setStatusApresentacao(!trabalho.getStatusApresentacao());
			adicionarTrabalho(trabalho);
		}
		return "{\"result\":\"ok\"}";
	}
}
