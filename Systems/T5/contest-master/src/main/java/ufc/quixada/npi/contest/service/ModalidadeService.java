package ufc.quixada.npi.contest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Modalidade;
import ufc.quixada.npi.contest.repository.EventoRepository;
import ufc.quixada.npi.contest.repository.ModalidadeRepository;
import ufc.quixada.npi.contest.util.GetEvento;

@Service
public class ModalidadeService {

	@Autowired
	private ModalidadeRepository modalidadeRepository;

	@Autowired
	private EventoRepository eventoRepository;

	public void adicionarModalidadeSubmissao(Evento evento, String nome) {
		if (!nome.isEmpty()) {
			if (!exists(nome, GetEvento.getId(evento))) {
				List<Modalidade> modalidadeEvento = evento.getModalidadesSubmissao();
				modalidadeEvento.add(new Modalidade(nome));
				evento.setModalidadesSubmissao(modalidadeEvento);
				//evento.addModalidadeSubmissao(new Modalidade(nome));
				eventoRepository.save(evento);
			}

		}
	}

	public void remover(Long modalidadeId) {
		modalidadeRepository.delete(modalidadeRepository.findOne(modalidadeId));
	}

	public boolean exists(String nomeModalidade, Long eventoId) {
		return modalidadeRepository.findByNomeAndEventoId(nomeModalidade, eventoId);
	}

	public void excluirModalidadeSubmissao(Evento evento, Modalidade modalidade) {
		/*if (null != modalidade && modalidade.getTrabalhos().isEmpty()) {
			if(null != evento.getModalidades() && evento.getModalidades().removeIf(m -> m.getId() == modalidade.getId())) {
				modalidadeRepository.delete(modalidade);
				
			}
		}*/
	}

}