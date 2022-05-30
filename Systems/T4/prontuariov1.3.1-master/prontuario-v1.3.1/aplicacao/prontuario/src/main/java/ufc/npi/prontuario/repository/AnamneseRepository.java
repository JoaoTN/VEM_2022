package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.Anamnese.Status;
import ufc.npi.prontuario.model.Pergunta;

public interface AnamneseRepository extends JpaRepository<Anamnese, Integer> {

	List<Anamnese> findAllByStatus(Status status);
	
	Anamnese findByNome(String nome);

	
	List<Pergunta> findById(Integer id);

}
