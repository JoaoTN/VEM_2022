package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer>{

	List<Disciplina> findAllByOrderByNome();
	
	Disciplina findByNome(String nome);

	Disciplina findByCodigo(String codigo);
}
