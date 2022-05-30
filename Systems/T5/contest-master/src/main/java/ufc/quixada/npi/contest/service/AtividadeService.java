package ufc.quixada.npi.contest.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.quixada.npi.contest.model.Atividade;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Participacao;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.repository.AtividadeRepository;
import ufc.quixada.npi.contest.repository.ParticipacaoRepository;

@Service
public class AtividadeService {

	@Autowired
	private AtividadeRepository atividadeRepository;

	@Autowired
	private ParticipacaoRepository participacaoRepository;

	public void adicionarOuAtualizar(Atividade atividade) {
		atividadeRepository.save(atividade);
	}

	public void gerarCodigo(Atividade atividade) {
		atividade.setCodigo(getCodigoEvento(atividade.getEvento()).toUpperCase()
				+ UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		adicionarOuAtualizar(atividade);
	}
	
	private String getCodigoEvento(Evento evento) {
		return evento.getCodigo();
	}

	public List<Atividade> getAtividadePorEvento(Long idEvento) {
		return atividadeRepository.findAtividadeByEventoId(idEvento);
	}

	public List<Participacao> getAtividadePorParticipacao(Pessoa participante) {
		return participacaoRepository.findByParticipante(participante);
	}

	public Atividade getAtividadePorCodigo(String codigo) {
		return atividadeRepository.findByCodigo(codigo);
	}

	public boolean hasParticipacoes(Atividade atividade) {
		if (atividade.getParticipacoes() != null && !atividade.getParticipacoes().isEmpty()) {
			return true;
		}

		return false;
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub
	}

    public List<Pessoa> getParticipantes(Evento evento) {
		return participacaoRepository.findParticipantes(evento);
    }
}
