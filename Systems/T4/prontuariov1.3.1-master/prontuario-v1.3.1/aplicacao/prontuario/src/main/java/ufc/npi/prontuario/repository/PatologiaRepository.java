package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento.Status;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Servidor;

public interface PatologiaRepository extends JpaRepository<Patologia, Integer> {

	List<Patologia> findByOdontogramaAndTratamentoIsNotNull(Odontograma odontograma);
	
	
	List<Patologia> findAllByOdontogramaAndAtendimentoResponsavelOrOdontogramaAndAtendimentoAjudante(
			Odontograma odontograma, Aluno responsavel, Odontograma odontograma2, Aluno ajudante);

	List<Patologia> findAllByOdontogramaAndAtendimentoProfessor(Odontograma odontograma, Servidor professor);

	List<Patologia> findAllByOdontogramaAndAtendimentoStatus(Odontograma odontograma, Status status);

	List<Patologia> findAllByOdontogramaAndIdIsNotInAndAtendimentoStatus(Odontograma odontograma,
			List<Integer> idPatologias, Status status);
	
	List<Patologia> findAllByOdontograma(Odontograma odontograma);
}
