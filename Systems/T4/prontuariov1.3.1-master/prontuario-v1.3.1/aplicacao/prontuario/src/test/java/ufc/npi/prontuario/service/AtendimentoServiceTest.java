package ufc.npi.prontuario.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_ATENDIMENTO;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.SetUsuarioId;
import ufc.npi.prontuario.model.Turma;

@DatabaseSetup(AtendimentoServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { AtendimentoServiceTest.DATASET })
public class AtendimentoServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-atendimento.xml";

	@Autowired
	private AtendimentoService atendimentoService;

	@Autowired
	private AlunoService alunoService;

	@Test
	public void buscarTudoPorAluno() {
		Aluno responsavel = new Aluno();
		SetUsuarioId.setIdUsuario(responsavel,2);

		List<Atendimento> atendimentos = atendimentoService.buscarTudoPorAluno(responsavel);

		assertNotNull(atendimentos);
		assertEquals(2, atendimentos.size());
	}

	@Test
	public void salvarFluxoDeErro() {
		Paciente paciente = new Paciente();
		paciente.setId(1);

		Turma turma = new Turma();
		turma.setId(1);

		Aluno responsavel = alunoService.buscarPorId(2);

		Aluno ajudante = alunoService.buscarPorId(3);

		Servidor professor = new Servidor();
		SetUsuarioId.setIdUsuario(professor,101);

		Date data = new Date();

		Atendimento atendimento = new Atendimento();
		atendimento.setAjudante(ajudante);
		atendimento.setData(data);
		atendimento.setPaciente(paciente);
		atendimento.setProfessor(professor);
		atendimento.setResponsavel(responsavel);
		atendimento.setTurma(turma);

		try {
			atendimentoService.salvar(atendimento);
			fail("Deveria ter sido lançada a exceção do atendimento em aberto");
		} catch (ProntuarioException e) {
			assertThat(e).isInstanceOf(ProntuarioException.class).hasMessage(ERRO_ADICIONAR_ATENDIMENTO);
		}
	}
	
	@Test
	public void salvarFluxoNormalPacienteDiferente() {
		Paciente paciente = new Paciente();
		paciente.setId(2);

		Turma turma = new Turma();
		turma.setId(1);

		Aluno responsavel = alunoService.buscarPorId(2);

		Aluno ajudante = alunoService.buscarPorId(3);

		Servidor professor = new Servidor();
		SetUsuarioId.setIdUsuario(professor,101);

		Date data = new Date();

		Atendimento atendimento = new Atendimento();
		atendimento.setAjudante(ajudante);
		atendimento.setData(data);
		atendimento.setPaciente(paciente);
		atendimento.setProfessor(professor);
		atendimento.setResponsavel(responsavel);
		atendimento.setTurma(turma);

		try {
			atendimentoService.salvar(atendimento);
		} catch (ProntuarioException e) {
			fail("Não deveria ter sido lançada uma exceção.");
		}
	}
	
	@Test
	public void salvarFluxoNormalResponsavelDiferente() {
		Paciente paciente = new Paciente();
		paciente.setId(1);

		Turma turma = new Turma();
		turma.setId(1);

		Aluno responsavel = alunoService.buscarPorId(3);

		Aluno ajudante = alunoService.buscarPorId(3);

		Servidor professor = new Servidor();
		SetUsuarioId.setIdUsuario(professor,101);

		Date data = new Date();

		Atendimento atendimento = new Atendimento();
		atendimento.setAjudante(ajudante);
		atendimento.setData(data);
		atendimento.setPaciente(paciente);
		atendimento.setProfessor(professor);
		atendimento.setResponsavel(responsavel);
		atendimento.setTurma(turma);

		try {
			atendimentoService.salvar(atendimento);
		} catch (ProntuarioException e) {
			fail("Não deveria ter sido lançada uma exceção.");
		}
	}
	
	@Test
	public void salvarFluxoNormalTurmaDiferente() {
		Paciente paciente = new Paciente();
		paciente.setId(1);

		Turma turma = new Turma();
		turma.setId(3);

		Aluno responsavel = alunoService.buscarPorId(2);

		Aluno ajudante = alunoService.buscarPorId(3);

		Servidor professor = new Servidor();
		SetUsuarioId.setIdUsuario(professor,101);

		Date data = new Date();

		Atendimento atendimento = new Atendimento();
		atendimento.setAjudante(ajudante);
		atendimento.setData(data);
		atendimento.setPaciente(paciente);
		atendimento.setProfessor(professor);
		atendimento.setResponsavel(responsavel);
		atendimento.setTurma(turma);

		try {
			atendimentoService.salvar(atendimento);
		} catch (ProntuarioException e) {
			fail("Não deveria ter sido lançada uma exceção.");
		}
	}
}
