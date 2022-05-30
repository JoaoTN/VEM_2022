package ufc.quixada.npi.contest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Trilha;
import ufc.quixada.npi.contest.repository.EventoRepository;
import ufc.quixada.npi.contest.repository.TrabalhoRepository;
import ufc.quixada.npi.contest.repository.TrilhaRepository;
import ufc.quixada.npi.contest.util.GetEvento;

import java.util.List;

@Service
public class TrilhaService {

	@Autowired
	private TrilhaRepository trilhaRepository;

	@Autowired
	private TrabalhoRepository trabalhoRepository;

	@Autowired
	private EventoRepository eventoRepository;
	
	public void adicionarOuAtualizarTrilha(Evento evento, Trilha trilha) {
		if (validacaoTrilha(evento, trilha)) {
			trilha.setEvento(evento);
		}
		
		if (validacaoTrilha(evento, trilha)) {
			trilhaRepository.save(trilha);
		}
	}
	
	private boolean validacaoTrilha(Evento evento, Trilha trilha) {
		String nome = trilha.getNome();
		if (trilha != null && !nome.isEmpty()) {
			if (!exists(nome, GetEvento.getId(evento))) {
				return true;
			}
		}
		return false;
	}

	public List<Trilha> buscarTrilhas(Long id) {
		return trilhaRepository.findAllByEventoId(id);
	}

	public Trilha get(Long trilhaId, Long eventoId) {
		return trilhaRepository.findByTrilhaIdAndEventoId(trilhaId, eventoId);
	}

	public boolean exists(String nome, Long eventoId) {
		return trilhaRepository.findByNomeAndEventoId(nome, eventoId);
	}

	public boolean existeTrabalho(Long id) {
		return trabalhoRepository.existsTrilhaId(id);
	}

	public int buscarQtdTrilhasPorEvento(Long eventoId) {
		return trilhaRepository.findAllByEventoId(eventoId).size();
	}

	public boolean existeTrilha(Long id) {
		return trilhaRepository.exists(id);
	}

	public void excluir(Evento evento, Trilha trilha) {
		trilha.excluir(evento, trilhaRepository);
	}

}