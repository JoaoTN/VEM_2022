package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Disciplina;

public interface DisciplinaService {
	void salvar(Disciplina disciplina) throws ProntuarioException;

	List<Disciplina> buscarTudo();

	Disciplina buscarPorId(Integer id);
	
	void removerDisciplina(Integer id) throws ProntuarioException;

	void atualizar(Disciplina disciplina) throws ProntuarioException;;
}
