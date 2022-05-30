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
import ufc.npi.prontuario.model.TipoProcedimento;

@DatabaseSetup(TipoProcedimentoServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { TipoProcedimentoServiceTest.DATASET })
public class TipoProcedimentoServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests.xml";

	@Autowired
	private TipoProcedimentoService tipoProcedimentoService;

	@Test
	public void testFindAll() {
		List<TipoProcedimento> tipoProcedimento= tipoProcedimentoService.buscarTudo();
		assertEquals(1, tipoProcedimento.size());
	}

	@Test
	public void testBuscarById() {

		TipoProcedimento tipoProcedimento = tipoProcedimentoService.buscarPorId(1);
		assertEquals("Tipo1", tipoProcedimento.getNome());

		// Busca por ID Inexistente

		assertEquals(null, tipoProcedimentoService.buscarPorId(222));

	}

	@Test
	public void testSalvar() {

		TipoProcedimento tipoProcedimento = new TipoProcedimento();
		tipoProcedimento.setDescricao("descrição 111");
		tipoProcedimento.setNome("Tipo Procedimento 111");
		tipoProcedimento.setId(2);

		try {
			tipoProcedimentoService.salvar(tipoProcedimento);
			assertEquals(tipoProcedimento, tipoProcedimentoService.buscarPorId(3));
		} catch (ProntuarioException e) {
			fail("Deveria ter cadastrado o tipo de procedimento");
		}
		
		//assertEquals(tipoProcedimento, tipoProcedimentoService.buscarPorId(2));

	}

}
