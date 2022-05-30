package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Disciplina;

@DatabaseSetup(DisciplinaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {DisciplinaServiceTest.DATASET})
public class DisciplinaServiceTest extends AbstractServiceTest {
	
	protected static final String DATASET = "/database-tests-disciplina.xml";
	
	@Autowired
	private DisciplinaService disciplinaService;
	
	@Test
    public void testFindAll() {
        List<Disciplina> disciplina = disciplinaService.buscarTudo();
        assertEquals(2, disciplina.size());
    }
	
	@Test
    public void testBuscarPorId() {
        Disciplina disciplina = disciplinaService.buscarPorId(1);
        assertEquals("1", disciplina.getCodigo());
        
        //Busca por Id Inexistente
        
        assertEquals(null, disciplinaService.buscarPorId(3));
    }
	
	@Test
    public void testSalvar() {
		
		Disciplina disciplina=new Disciplina();
		disciplina.setCodigo("111");
		disciplina.setNome("Disciplina3");
		disciplina.setId(3);
		
		try {
			disciplinaService.salvar(disciplina);
			assertEquals(disciplina, disciplinaService.buscarPorId(3));
		} catch (ProntuarioException e1) {
			fail("A disciplina deveria ter sido cadastrada");
		}
		
        //teste salvar com código já cadastrado
        Disciplina disciplina2 = new Disciplina();
        disciplina2.setCodigo("2");
        disciplina2.setNome("Disc 4");
        try {
        	disciplinaService.salvar(disciplina2);
        	fail("Não pode ser possível salvar com código já existente");
		} catch (ProntuarioException e) {
		}
        
        //teste salvar com nome já cadastrado
        Disciplina disciplina3 = new Disciplina();
        disciplina3.setCodigo("4");
        disciplina3.setNome("disciplina1");
        try {
        	disciplinaService.salvar(disciplina3);
        	fail("Não pode ser possível salvar com nome já existente");
		} catch (ProntuarioException e) {
		}
    }

}
