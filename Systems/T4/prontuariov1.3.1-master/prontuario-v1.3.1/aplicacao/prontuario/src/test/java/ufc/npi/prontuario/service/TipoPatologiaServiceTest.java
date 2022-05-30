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
import ufc.npi.prontuario.model.TipoPatologia;

@DatabaseSetup(TipoPatologiaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { TipoPatologiaServiceTest.DATASET })
public class TipoPatologiaServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests.xml";

	@Autowired
	private TipoPatologiaService tipoPatologiaService;

	@Test
	public void testFindAll() {
		List<TipoPatologia> tipoPatologia = tipoPatologiaService.buscarTudo();
		assertEquals(1, tipoPatologia.size());
	}

	@Test
	public void testBuscarById() {

		TipoPatologia tipoPatologia = tipoPatologiaService.buscarPorId(1);
		assertEquals("Tipo1", tipoPatologia.getNome());

		// Busca por ID Inexistente

		assertEquals(null, tipoPatologiaService.buscarPorId(222));

	}

	@Test
	public void testSalvar() {

		TipoPatologia tipoPatologia = new TipoPatologia();
		tipoPatologia.setDescricao("descrição 111");
		tipoPatologia.setNome("Tipo Patologia 111");
		tipoPatologia.setId(3);

		try {
			tipoPatologiaService.salvar(tipoPatologia);
			assertEquals(tipoPatologia, tipoPatologiaService.buscarPorId(3));
		} catch (ProntuarioException e) {
			fail("Deveria ter cadastrado o tipo de patologia");
		}
	}

}
