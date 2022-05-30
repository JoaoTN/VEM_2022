package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_EXCLUIR_ANAMNESE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_NOME_ANAMNESE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.Anamnese.Status;
import ufc.npi.prontuario.model.Pergunta;
import ufc.npi.prontuario.repository.AnamneseRepository;
import ufc.npi.prontuario.repository.PerguntaRepository;
import ufc.npi.prontuario.service.AnamneseService;

@Service
public class AnamneseServiceImpl implements AnamneseService {

	@Autowired
	private AnamneseRepository anamneseRepository;

	@Autowired
	PerguntaRepository perguntaRepository;
	
//////////////////////////////////////////////////////////////
	@Override
	public void salvar(Anamnese anamnese) throws ProntuarioException {
		if (anamnese.verificarNomeDescricao()) {
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}
		if (anamnese.findNameInRepository(anamneseRepository)) {
			throw new ProntuarioException(ERROR_NOME_ANAMNESE);
		}
		anamnese.setStatus(Status.EM_ANDAMENTO);
		anamneseRepository.save(anamnese);
	}
////////////////////////////////////////////////////////////
	
	@Override
	public void salvarPergunta(Pergunta pergunta, Integer idAnamnese) {
		Anamnese anamnese = addPerguntaEmAnamnese(pergunta, idAnamnese);
		if (anamnese != null) {
			anamneseRepository.save(anamnese);
		}
	}

////////////////////////////////////////////////////////////
	private Anamnese addPerguntaEmAnamnese(Pergunta pergunta, Integer idAnamnese) {
		Anamnese anamnese = anamneseRepository.findOne(idAnamnese);

		if (validarAnamnese(anamnese)) {
			setOrdemPergunta(pergunta, anamnese);
			setPerguntaAnamnese(pergunta, anamnese);
			anamnese.getPerguntas().add(pergunta);
			return anamnese;
		}
		return null;
	}
	
	public void setOrdemPergunta(Pergunta pergunta, Anamnese anamnese) {
		pergunta.setOrdem(ordemDaPergunta(anamnese.getPerguntas()));
	}
	
	public void setPerguntaAnamnese(Pergunta pergunta, Anamnese anamnese) {
		pergunta.setAnamnese(anamnese);
	}

////////////////////////////////////////////////////////////
	
	private boolean validarAnamnese(Anamnese anamnese) {
		return anamnese != null && anamnese.getStatus() != Status.FINALIZADA;
	}

	private int ordemDaPergunta(List<Pergunta> perguntas){
		return perguntas.isEmpty() ? 1 : perguntas.get(perguntas.size() - 1).getOrdem() + 1;
	}

	@Override
	public void excluirPergunta(Pergunta pergunta, Anamnese anamnese) {
		perguntaRepository.delete(pergunta);
		
		removerPerguntaDaAnamnese(anamnese, pergunta);
	}
////////////////////////////////////////////////////////////	
	private void removerPerguntaDaAnamnese(Anamnese anamnese, Pergunta pergunta) {
		if (anamneseNaoFinalizada(anamnese)) {
			List<Pergunta> perguntas = anamnese.getPerguntas();
			
			removerPerguntaDaLista(perguntas, pergunta);
			
			anamneseRepository.saveAndFlush(anamnese);
		}
	}
	
	public boolean anamneseNaoFinalizada(Anamnese anamnese) {
		return anamnese.getStatus() != Status.FINALIZADA;
	}
////////////////////////////////////////////////////////////	
	private void removerPerguntaDaLista(List<Pergunta> perguntas, Pergunta pergunta) {
		int posicaoPergunta = perguntas.indexOf(pergunta);
		
		if (posicaoPergunta > -1) {
			List<Pergunta> perguntasParaReordenar = perguntas.subList(posicaoPergunta + 1, perguntas.size());
			
			for (Pergunta perguntaParaReordenar : perguntasParaReordenar) {
				Integer novaOrdem = perguntaParaReordenar.getOrdem() - 1;
				perguntaParaReordenar.setOrdem(novaOrdem);
			}
			
			perguntas.remove(posicaoPergunta);
		}
	}

	@Override
	public List<Anamnese> buscarTudo() {
		List<Anamnese> anamneses = anamneseRepository.findAll();
		return anamneses;
	}

	@Override
	public Anamnese buscarPorId(Integer id) {
		Anamnese anamnese = anamneseRepository.findOne(id);
		return anamnese;
	}

	@Override
	public List<Anamnese> buscarTodasFinalizadas() {
		return anamneseRepository.findAllByStatus(Status.FINALIZADA);
	}

	@Override
	public void remover(Anamnese anamnese) throws ProntuarioException {
		if (anamnese != null && anamnese.getStatus().equals(Status.FINALIZADA)) {
			throw new ProntuarioException(ERROR_EXCLUIR_ANAMNESE);
		}
		anamneseRepository.delete(anamnese);
	}

	@Override
	public void finalizar(Anamnese anamnese) {
		anamnese.setStatus(Status.FINALIZADA);
		anamneseRepository.save(anamnese);
	}

	@Override
	public Anamnese alterarOrdemAnamnese(Anamnese anamnese, Integer pergunta, Integer novaOrdem) {
		Pergunta old = perguntaRepository.findOne(pergunta);
		Anamnese anamneseOld = anamneseRepository.findOne(anamnese.getId());

		if (anamneseEmAndamento(anamneseOld)) {

			Pergunta a = anamneseOld.getPerguntas().get(novaOrdem - 1);
			int indexPerguntaAntiga = pegarIndexPergunta(a, anamneseOld);
			int indexPergunta = pegarIndexPergunta(old, anamneseOld);

			anamneseOld.getPerguntas().remove(old);

			if (novaOrdem > old.getOrdem()) {
				reodenarPerguntasAsc(indexPergunta, indexPerguntaAntiga, anamneseOld);
			} else {
				reodenarPerguntasDesc(indexPergunta, indexPerguntaAntiga, anamneseOld);
			}

			alterarOrdemPergunta(old, novaOrdem);

			adicionarPerguntaEmAnamneseESalvar(anamneseOld, old);
		}
		return anamneseOld;
	}

	private boolean anamneseEmAndamento(Anamnese anamnese) {
		return anamnese.getStatus().equals(Status.EM_ANDAMENTO);
	}

	private int pegarIndexPergunta(Pergunta pergunta, Anamnese anamnese) {
		return anamnese.getPerguntas().indexOf(pergunta);
	}

	private void alterarOrdemPergunta(Pergunta pergunta, int index) {
		pergunta.setOrdem(index);
	}

	private void reodenarPerguntasAsc (int indexPergunta, int indexPerguntaAntiga, Anamnese anamneseOld) {
		Pergunta perguntaIter;
		for (int i = indexPergunta; i < indexPerguntaAntiga; i++) {
			perguntaIter = anamneseOld.getPerguntas().get(i);
			alterarOrdemPergunta(perguntaIter, perguntaIter.getOrdem() - 1);
		}
	}

	private void reodenarPerguntasDesc (int indexPergunta, int indexPerguntaAntiga, Anamnese anamneseOld) {
		Pergunta perguntaIter;
		for (int i = indexPergunta - 1; i >= indexPerguntaAntiga; i--) {
			perguntaIter = anamneseOld.getPerguntas().get(i);
			alterarOrdemPergunta(perguntaIter, perguntaIter.getOrdem() + 1);
		}
	}

	private void adicionarPerguntaEmAnamneseESalvar(Anamnese anamneseOld, Pergunta old) {
		anamneseOld.addPergunta(old);
		Collections.sort(anamneseOld.getPerguntas());
		anamneseRepository.save(anamneseOld);
	}

}
