package ufc.npi.prontuario.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.SetUsuarioId;
import ufc.npi.prontuario.model.SetSenhaUsuario;

@DatabaseSetup(ServidorServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {ServidorServiceTest.DATASET})
public class ServidorServiceTest extends AbstractServiceTest{

	protected static final String DATASET = "/database-tests-servidor.xml";
	
	@Autowired
	private ServidorService servidorService;
	
	@Test 
	public void testBuscarTudo(){
		assertEquals(5, servidorService.buscarProfessores().size());
	}
	
	@Test
	public void testBuscarPorId(){
		Servidor servidor = new Servidor();
		SetUsuarioId.setIdUsuario(servidor,1);
		Servidor servidor2 = servidorService.buscarPorId(1);
		
		assertEquals(servidor.getId(), servidor2.getId());
		
		//test buscar por um id não existente
		assertEquals(null, servidorService.buscarPorId(10));
	}
	
	@Test
	public void testSalvar(){
		Servidor servidor = new Servidor();
		servidor.setEmail("servidor3@email.com");
		servidor.setNome("Servidor 3");
		servidor.setMatricula("555554");
		SetSenhaUsuario.setUsuarioSenha(servidor,"1234");
		servidor.encodePassword();
		
		try {
			servidorService.salvar(servidor);
			Servidor servidor2 = servidorService.buscarPorId(servidor.getId());
			assertEquals(servidor.getId(), servidor2.getId());
		} catch (ProntuarioException e) {
			fail("Deveria ter cadastrado o servidor");
		}
		
		//Test salvar com email já existente
		Servidor servidor3 = new Servidor();
		servidor3.setEmail("servidor1@email.com");
		servidor3.setNome("Servidor 13");
		servidor3.setMatricula("555559");
		SetSenhaUsuario.setUsuarioSenha(servidor3,"1234");
		servidor3.encodePassword();
		
		try {
			servidorService.salvar(servidor3);
			fail("Deveria ter lançado exceção para matricula ou email já cadastrados.");
		} catch (ProntuarioException e) {
			String erro = "Não foi possível adicionar o servidor! Matricula ou email já cadastrados.";
			assertThat(e)
			.isInstanceOf(ProntuarioException.class)
			.hasMessage(erro);
		}
		
		//Test salvar com matricula já existente
		Servidor servidor4 = new Servidor();
		servidor4.setEmail("servidor4@email.com");
		servidor4.setNome("Servidor 4");
		servidor4.setMatricula("555556");
		SetSenhaUsuario.setUsuarioSenha(servidor4,"1234");
		servidor4.encodePassword();
				
		try {
			servidorService.salvar(servidor4);
			fail("Deveria ter lançado exceção para matricula ou email já cadastrados.");
		} catch (ProntuarioException e) {
			String erro = "Não foi possível adicionar o servidor! Matricula ou email já cadastrados.";
			assertThat(e)
			.isInstanceOf(ProntuarioException.class)
			.hasMessage(erro);
		}
	}
}
