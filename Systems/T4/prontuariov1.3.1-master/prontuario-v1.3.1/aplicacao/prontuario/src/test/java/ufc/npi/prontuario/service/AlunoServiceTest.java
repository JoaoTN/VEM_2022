package ufc.npi.prontuario.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.GetMatriculaUsuario;
import ufc.npi.prontuario.model.SetUsuarioId;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.model.SetSenhaUsuario;

@DatabaseSetup(AlunoServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {AlunoServiceTest.DATASET})
public class AlunoServiceTest extends AbstractServiceTest {
	
	protected static final String DATASET = "/database-tests-aluno.xml";
	
	@Autowired
	private AlunoService alunoService;
	
	@Test
    public void testBuscarTudo() {
        List<Aluno> alunos = alunoService.buscarTudo();
        assertEquals(3, alunos.size());
    }
	
	@Test
	public void testBuscarPorId(){
		Aluno aluno;
		aluno = alunoService.buscarPorId(1);
		long id = aluno.getId();
		assertEquals(1, id);
		
		//buscar por id inexistente
		aluno = alunoService.buscarPorId(4);
		assertEquals(null, aluno);
	}
	
	@Test
	public void testBuscarPorMatricula(){
		Aluno aluno;
		aluno = alunoService.buscarPorMatricula("3333");
		assertEquals("3333", GetMatriculaUsuario.getUsuarioMatricula(aluno));
		
		//buscar por matricula inexistente
		aluno = alunoService.buscarPorMatricula("4444");
		assertEquals(null, aluno);
	}
	
	@Test
	public void testBuscarAjudantes(){
		Aluno ajudante = new Aluno();
		SetUsuarioId.setIdUsuario(ajudante,1);
		List<Aluno> alunos = alunoService.buscarAjudantes(1, ajudante);
		assertEquals(2, alunos.size());
		
		//Verifica se o ajudante está na lista
		assertEquals(false, alunos.contains(ajudante));
		
		//buscar por ajudante inexistente
		SetUsuarioId.setIdUsuario(ajudante,4);
		alunos = alunoService.buscarAjudantes(2, ajudante);
		assertNull(alunos);
	}

	@Test
	public void testSalvar(){
		Aluno aluno = new Aluno();
		aluno.setEmail("aluno@aluno.com");
		aluno.setMatricula("308020");
		aluno.setNome("Aluno 1");
		SetSenhaUsuario.setUsuarioSenha(aluno,"1234");
		aluno.encodePassword();
		aluno.setAnoIngresso(2010);
		aluno.setSemestreIngresso(1);
		
		try {
			alunoService.salvar(aluno);
		} catch (ProntuarioException e) {
			fail("Deveria ter cadastrado o aluno");
		}
		
		Aluno aluno2 = alunoService.buscarPorId(aluno.getId());
		
		assertEquals(aluno.getId(), aluno2.getId());
		
		//tentar salvar com matricula já existente
		Aluno aluno3 = new Aluno();
		aluno3.setNome("Aluno 3");
		aluno3.setMatricula("3333");
		aluno3.setEmail("aluno3@email.com");
		aluno3.setAnoIngresso(2016);
		aluno3.setSemestreIngresso(1);
		
		try {
			alunoService.salvar(aluno3);
			fail("Deveria ter lançado exceção para matricula ou email já cadastrados.");
		} catch (ProntuarioException e) {
			String erro = "Não foi possível adicionar o aluno! Matricula ou email já cadastrados.";
			assertThat(e)
			.isInstanceOf(ProntuarioException.class)
			.hasMessage(erro);
		}
		
		//tentar salvar com email já existente
		Aluno aluno4 = new Aluno();
		aluno4.setNome("Aluno 4");
		aluno4.setMatricula("003210");
		aluno4.setEmail("aluno@aluno.com");
				
		try {
			alunoService.salvar(aluno4);
			fail("Deveria ter lançado exceção para matricula ou email já cadastrados.");
		} catch (ProntuarioException e) {
			String erro = "Não foi possível adicionar o aluno! Matricula ou email já cadastrados.";
			assertThat(e)
			.isInstanceOf(ProntuarioException.class)
			.hasMessage(erro);
		}
	}
}
