package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.Turma;

public interface TurmaRepository extends JpaRepository<Turma, Integer> {

	List<Turma> findByAlunoTurmasAlunoAndAtivoIsTrueOrderByNome(Aluno aluno);
	
	List<Turma> findAllByProfessores(Servidor professor);
	
	Turma findByNomeAndAnoAndSemestreAndDisciplina(String nome, Integer ano, Integer semestre, Disciplina disciplina);
}
