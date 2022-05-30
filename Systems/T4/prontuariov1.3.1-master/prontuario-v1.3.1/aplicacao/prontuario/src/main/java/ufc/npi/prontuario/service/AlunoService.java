package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;

public interface AlunoService {

	void salvar(Aluno aluno) throws ProntuarioException;

	void atualizar(Aluno aluno) throws ProntuarioException;

	List<Aluno> buscarTudo();

	Aluno buscarPorId(Integer id);

	Aluno buscarPorMatricula(String matricula);
	
	void removerAluno(Integer id) throws ProntuarioException;

	List<Aluno> buscarAjudantes(Integer idTurma, Aluno responsavel);
}
