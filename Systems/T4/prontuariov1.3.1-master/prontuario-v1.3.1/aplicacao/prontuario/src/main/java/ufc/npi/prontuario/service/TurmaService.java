package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.Turma;
import ufc.npi.prontuario.model.Usuario;

public interface TurmaService {

	void salvar(Turma turma) throws ProntuarioException;

	Turma buscarPorId(Integer id);

	void inscreverAluno(Turma turma, String matricula) throws ProntuarioException;
	
	void removerInscricao(Turma turma, Aluno aluno) throws ProntuarioException;

	List<Turma> buscarTudo();

	void alterarStatus(Turma turma);

	void adicionarProfessorTurma(Turma turma, List<Servidor> professores);
	
	List<Turma> buscarAtivasPorAluno(Aluno aluno);
	
	List<Usuario> buscarProfessores(List<Servidor> professores);

	List<Turma> buscarTurmas(Servidor servidor);

	void removerTurma(Integer id) throws ProntuarioException;
	
	List<Turma> buscarTurmasProfessor(Servidor servidor);
	
	void removerProfessor(Turma turma, Servidor professor) throws ProntuarioException;

}
