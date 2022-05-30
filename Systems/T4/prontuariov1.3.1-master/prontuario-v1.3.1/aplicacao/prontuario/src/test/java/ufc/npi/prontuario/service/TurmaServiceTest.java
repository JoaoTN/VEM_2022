package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.GetProfessorTurma;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.SetUsuarioId;
import ufc.npi.prontuario.model.Turma;

@DatabaseSetup(TurmaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {TurmaServiceTest.DATASET})
public class TurmaServiceTest extends AbstractServiceTest{
	
	public static final String DATASET = "/database-tests-turma.xml";
	
	@Autowired
	private TurmaService turmaService;
	
	@Autowired
	private ServidorService servidorService;
	
	private List<Servidor> professores;
	private Disciplina disciplina;
	
	@Before
	public void iniciarVariaveis() {
		professores = new ArrayList<Servidor>();
		professores.add(servidorService.buscarPorId(101));
		disciplina = new Disciplina();
	}
	
	@Test
	public void testBuscarTudo(){
		assertEquals(3, turmaService.buscarTudo().size());
	}
	
	@Test
	public void testBuscarPorId(){
		//Busca por Id existente
		Turma turma = turmaService.buscarPorId(1);
		assertEquals("Turma 1", turma.getNome());
		
		//Busca por Id inexistente
		assertEquals(null, turmaService.buscarPorId(4));
	}
	
	@Test
	public void testSalvar(){
		disciplina.setId(1);
		
		Turma turma = new Turma("TURMA A", 2016, 2, disciplina, professores);
		salvar(turma);
		assertEquals(turma, turmaService.buscarPorId(4));
		
		turma.setNome("TURMA B");
		salvar(turma);
		assertEquals("TURMA B", turmaService.buscarPorId(4).getNome());
	}
	
	@Test
	public void testBuscarAtivasPorAluno(){
		Aluno aluno = new Aluno();
		
		//Busca de aluno matriculado em turmas
		SetUsuarioId.setIdUsuario(aluno,1);
		assertEquals(2, turmaService.buscarAtivasPorAluno(aluno).size());
		
		//Busca de aluno não matriculado em turmas
		SetUsuarioId.setIdUsuario(aluno,4);
		assertEquals(true, turmaService.buscarAtivasPorAluno(aluno).isEmpty());
	}
	
	@Test
	public void testAlterarStatus(){
		Turma turma = turmaService.buscarPorId(1);
		
		//Alterar status de uma turma ativa
		turmaService.alterarStatus(turma);
		assertEquals(false, turma.getAtivo());
		
		//Alterar status de uma turma não-ativa
		turmaService.alterarStatus(turma);
		assertEquals(true, turma.getAtivo());
	}
	
	@Test
	public void testInscreverAluno(){
		String matricula = "2222";
		Turma turma = turmaService.buscarPorId(2);
		
		//Inscrever um aluno na turma
		try {
			turmaService.inscreverAluno(turma, matricula);
		} catch (ProntuarioException e) {
			fail("O aluno deveria ter sido cadastrado");
		}
		
		//Inscrever um aluno já existente na turma
		try {
			turmaService.inscreverAluno(turma, matricula);
			fail("O aluno não deveria ter sido cadastrado");
		} catch (ProntuarioException e) {
		}
		
		//Inscrever um aluno inexistente
		matricula = "6666";
		try {
			turmaService.inscreverAluno(turma, matricula);
			fail("Aluno inexistente não deveria ter sido cadastrado");
		} catch (ProntuarioException e) {
		}
	}
	
	@Test
	public void testRemoverInscricao(){
		Turma turma = turmaService.buscarPorId(1);
		Aluno aluno = new Aluno();
		
		//Remover inscricao de aluno sem atendimentos realizados
		SetUsuarioId.setIdUsuario(aluno,1);
		try {
			turmaService.removerInscricao(turma, aluno);
		} catch (ProntuarioException e) {
			fail("A inscrição do aluno deveria ter sido removida");
		}
		
		//Remover inscricao de aluno com atendimentos realizados
		SetUsuarioId.setIdUsuario(aluno,3);
		try {
			turmaService.removerInscricao(turma, aluno);
			fail("A inscrição do aluno não deveria ter sido removida");
		} catch (ProntuarioException e) {
		}
	}
	
	@Test
	public void testAdicionarProfessorTurma(){
		Turma turma = turmaService.buscarPorId(1);
		List<Servidor> professores = new ArrayList<Servidor>();
		professores.add(servidorService.buscarPorId(102));
		professores.add(servidorService.buscarPorId(103));
		
		turmaService.adicionarProfessorTurma(turma, professores);
		assertEquals(3, mostrarTurmaProfessores(turma).size());
	}
	
	public List<Servidor> mostrarTurmaProfessores(Turma turma){
		return GetProfessorTurma.mostrarTurmaProfessores(turma);
	}

	@Test
	public void testBuscarProfessores(){
		//Busca todos os professores que não lecionam na turma
		Turma turma = turmaService.buscarPorId(2);
		assertEquals(2, turmaService.buscarProfessores(mostrarTurmaProfessores(turma)).size());
	}
	
	@Test
	public void testbuscarTurmasProfessor(){
		Servidor professor = new Servidor();
		SetUsuarioId.setIdUsuario(professor,101);
		assertEquals(2, turmaService.buscarTurmasProfessor(professor).size());
	}
	
	private void salvar(Turma turma) {
		
		try{
			turmaService.salvar(turma);
		}catch(ProntuarioException e){
			fail("Turma não salva.");
		}
		
	}
}
