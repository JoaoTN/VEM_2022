package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Atendimento.Status;
import ufc.npi.prontuario.model.Avaliacao;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.Turma;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {

	List<Atendimento> findAllByResponsavelOrAjudanteOrderByDataDesc(Aluno responsavel, Aluno ajudante);

	List<Atendimento> findAllByResponsavelAndTurmaOrAjudanteAndTurma(Aluno responsavel, Turma turma1, Aluno ajudante,
			Turma turma2);

	List<Atendimento> findAllByProfessorAndTurma(Servidor professor, Turma turma);

	List<Atendimento> findAllByProfessor(Servidor professor);
	
	List<Atendimento> findAllByProfessorAndStatusNotIn(Servidor professor, List<Status> status);

	List<Atendimento> findAllByProfessorAndPaciente(Servidor professor, Paciente paciente);

	List<Atendimento> findAllByResponsavelAndPacienteOrAjudanteAndPaciente(Aluno aluno, Paciente paciente, Aluno aluno2, Paciente paciente2);
	
	List<Atendimento> findAllByStatusAndPaciente(Status status, Paciente paciente);
	
	List<Atendimento> findAllByPaciente(Paciente paciente);

	List<Atendimento> findAllByStatusAndPacienteAndIdIsNotIn(Status status, Paciente paciente, List<Integer> idAtendimentos);
	
	//busca todos os atendimentos em aberto do aluno responsavel e do aluno auxiliar para o paciente
	@Query("select at from Atendimento as at where (at.responsavel = :a1 OR at.ajudante = :a1 OR at.responsavel = :a2 OR at.ajudante = :a2) "
			+ "AND at.paciente = :pac AND at.status = :sta")
	List<Atendimento> findAllByResponsavelOrAjudanteExist(@Param("a1") Aluno aluno, @Param("a2") Aluno aluno2,
			@Param("pac") Paciente paciente, @Param("sta") Status status);
	
	//busca todos os atendimentos em aberto do aluno como responsavel ou auxiliar para o paciente
	@Query("select at from Atendimento as at where (at.responsavel = :alu OR at.ajudante = :alu) "
			+ "AND at.paciente = :pac AND at.status = :sta")
	List<Atendimento> findAllByResponsavelOrAjudanteExist(@Param("alu") Aluno aluno,
			@Param("pac") Paciente paciente, @Param("sta") Status status);
	
	List<Atendimento> findAllByStatus(Status status);
	
	List<Atendimento> findAllByAvaliacao_avaliacao(Avaliacao avaliacao);
}