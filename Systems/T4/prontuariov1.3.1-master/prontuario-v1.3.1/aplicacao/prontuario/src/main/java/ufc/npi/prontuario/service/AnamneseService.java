package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.Pergunta;

public interface AnamneseService {

	public void salvar(Anamnese anamnese) throws ProntuarioException;

	public void salvarPergunta(Pergunta pergunta, Integer idAnamnese);

	public void excluirPergunta(Pergunta pergunta, Anamnese anamnese);

	// public List<Pergunta> ordenarPergunta(Pergunta pergunta);

	public List<Anamnese> buscarTudo();

	public List<Anamnese> buscarTodasFinalizadas();

	public Anamnese buscarPorId(Integer id);

	public void remover(Anamnese anamnese) throws ProntuarioException;

	public void finalizar(Anamnese anamnese);

	public Anamnese alterarOrdemAnamnese(Anamnese anamnese, Integer pergunta, Integer novaOrdem);

}
