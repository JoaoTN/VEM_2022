package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento.Status;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.model.Servidor;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, Integer> {

	List<Procedimento> findAllByOdontogramaAndAtendimentoResponsavelAndPreExistenteIsFalseOrOdontogramaAndAtendimentoAjudanteAndPreExistenteIsFalse(
			Odontograma odontograma, Aluno aluno, Odontograma odontograma2, Aluno aluno2);

	List<Procedimento> findAllByOdontogramaAndAtendimentoProfessorAndPreExistenteIsFalse(Odontograma odontograma, Servidor servidor);

	List<Procedimento> findAllByOdontogramaAndAtendimentoStatusAndPreExistenteIsFalse(Odontograma odontograma, Status status);

	List<Procedimento> findAllByOdontogramaAndIdIsNotInAndAtendimentoStatusAndPreExistenteIsFalse(Odontograma odontograma,
			List<Integer> idProcedimentos, Status status);
	
	
	List<Procedimento> findAllByOdontogramaAndAtendimentoResponsavelAndPreExistenteIsTrueOrOdontogramaAndAtendimentoAjudanteAndPreExistenteIsTrue(
			Odontograma odontograma, Aluno aluno, Odontograma odontograma2, Aluno aluno2);

	List<Procedimento> findAllByOdontogramaAndAtendimentoProfessorAndPreExistenteIsTrue(Odontograma odontograma, Servidor servidor);

	List<Procedimento> findAllByOdontogramaAndAtendimentoStatusAndPreExistenteIsTrue(Odontograma odontograma, Status status);

	List<Procedimento> findAllByOdontogramaAndIdIsNotInAndAtendimentoStatusAndPreExistenteIsTrue(Odontograma odontograma,
			List<Integer> idProcedimentos, Status status);
	
	
}
