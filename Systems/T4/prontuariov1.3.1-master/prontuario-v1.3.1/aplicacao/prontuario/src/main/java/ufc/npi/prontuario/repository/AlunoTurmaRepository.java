package ufc.npi.prontuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.AlunoTurma;
import ufc.npi.prontuario.model.AlunoTurmaId;

public interface AlunoTurmaRepository extends JpaRepository<AlunoTurma, AlunoTurmaId> {

}
