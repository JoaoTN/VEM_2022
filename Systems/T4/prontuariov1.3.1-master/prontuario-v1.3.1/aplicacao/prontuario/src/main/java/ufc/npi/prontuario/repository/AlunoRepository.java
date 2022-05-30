package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import ufc.npi.prontuario.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {

	public Aluno findByMatricula(@Param("matricula") String matricula);

	public Aluno findByEmail(@Param("email") String email);

	public List<Aluno> findAllByAlunoTurmasTurmaIdEqualsAndAlunoTurmasAtivoIsTrue(Integer idTurma);
}
