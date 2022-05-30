package ufc.npi.prontuario.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.model.Odontograma;

@DatabaseSetup(OdontogramaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { OdontogramaServiceTest.DATASET })
public class OdontogramaServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-odontograma.xml";

	@Autowired
	private OdontogramaService odontogramaService;

	@Test
	public void buscarPorPacienteId() {
		Odontograma odontograma = odontogramaService.buscarPorPacienteId(1);
		assertNotNull(odontograma);

		odontograma = odontogramaService.buscarPorPacienteId(2);
		assertNull(odontograma);
	}
}
