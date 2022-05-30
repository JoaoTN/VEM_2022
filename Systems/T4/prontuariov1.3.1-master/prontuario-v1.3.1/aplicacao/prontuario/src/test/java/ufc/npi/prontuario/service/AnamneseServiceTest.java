package ufc.npi.prontuario.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.Anamnese.Status;
import ufc.npi.prontuario.model.Pergunta;
import ufc.npi.prontuario.model.Pergunta.TiposPerguntas;
import ufc.npi.prontuario.repository.PerguntaRepository;

import static org.junit.Assert.*;

@DatabaseSetup(AnamneseServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { AnamneseServiceTest.DATASET })
public class AnamneseServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-anamnese.xml";

	@Autowired
	private AnamneseService anamneseService;
	
	@Autowired 
	private PerguntaRepository perguntaRepository;

	@Test
	public void testBuscarTudo() {
		List<Anamnese> anamneses = anamneseService.buscarTudo();
		assertEquals(3, anamneses.size());
	}

	@Test
	public void testBuscarTodasFinalizadas() {
		List<Anamnese> anamneses = anamneseService.buscarTodasFinalizadas();
		assertEquals(2, anamneses.size());
	}

	@Test
	public void testBuscarPorId() {
		Anamnese anamneseValida = anamneseService.buscarPorId(2);
		assertNotNull(anamneseValida);

		assertEquals((Integer) 2, anamneseValida.getId());

		Anamnese anamneseInvalida = anamneseService.buscarPorId(4);
		assertNull(anamneseInvalida);
	}

	@Test
	public void testSalvar() {
		Anamnese anamnese = new Anamnese();
		anamnese.setNome("Anamnese de teste");
		anamnese.setDescricao("Descrição da anamnese de teste");
		anamnese.setStatus(Status.EM_ANDAMENTO);

		try {
			anamneseService.salvar(anamnese);
			assertEquals(anamnese.getId(), anamneseService.buscarPorId(anamnese.getId()));
		} catch (ProntuarioException e) {
			fail("A anamnese deveria ter sido cadastrada");
		}
	}

	@Test
	public void testSalvarPergunta() {
		Pergunta pergunta1 = new Pergunta();
		pergunta1.setTexto("Texto da pergunta 1");
		pergunta1.setTipo(TiposPerguntas.SIM_OU_NAO);

		Pergunta pergunta2 = new Pergunta();
		pergunta2.setTexto("Texto da pergunta 2");
		pergunta2.setTipo(TiposPerguntas.TEXTO);

		Pergunta pergunta3 = new Pergunta();
		pergunta3.setTexto("Texto da pergunta 3");
		pergunta3.setTipo(TiposPerguntas.TEXTO_E_SIM_OU_NAO);

		// Tentando salvar em uma anamnese existente
		anamneseService.salvarPergunta(pergunta1, 2);
		assertNotNull(pergunta1.getAnamnese());

		// Tentando salvar em uma anamnese inexistente
		anamneseService.salvarPergunta(pergunta2, 4);
		assertNull(pergunta2.getAnamnese());

		// Tentando salvar em uma anamnese finalizada
		anamneseService.salvarPergunta(pergunta3, 1);
		assertNull(pergunta3.getAnamnese());
	}
	
	@Test
	public void testExcluirPergunta() {
		Pergunta pergunta = new Pergunta();
		pergunta.setId(4);
		
		Anamnese anamnese = anamneseService.buscarPorId(2);
		
		anamneseService.excluirPergunta(pergunta, anamnese);
		
		assertFalse(perguntaRepository.exists(4));
	}
}
