package ufc.npi.prontuario.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PacienteAnamnese;

@DatabaseSetup(PacienteServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { PacienteServiceTest.DATASET })
public class PacienteServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests.xml";

	@Autowired
	private PacienteService pacienteService;

	@Test
	public void testFindAll() {
		List<Paciente> paciente = pacienteService.buscarTudo();
		assertEquals(1, paciente.size());
	}

	@Test
	public void testBuscarByCpf() {

		Paciente paciente = pacienteService.buscarByCpf("123123");
		assertEquals("123123", paciente.getCpf());

		// Busca por Cpf Inexistente

		assertEquals(null, pacienteService.buscarByCpf("1"));

	}

	@Test
	public void testSalvar() {

		Paciente paciente = new Paciente();
		paciente.setCpf("1231234");
		paciente.setNome("José");

		try {
			pacienteService.salvar(paciente);

		} catch (ProntuarioException e) {
			fail("Paciente não salvo.");
		}

		assertEquals(paciente, pacienteService.buscarByCpf("1231234"));

		// Teste salvar com cpf ja existente

		Paciente paciente2 = new Paciente();
		paciente2.setCpf("123123");
		try {
			pacienteService.salvar(paciente2);
			fail("Deveria ter lançado exceção para Cpf de paciente já cadastrado.");
		} catch (ProntuarioException e) {
			
			String erro = "Não foi possível adicionar o paciente! Este CPF já foi cadastrado.";
			assertThat(e).isInstanceOf(ProntuarioException.class).hasMessage(erro);
		}
	}

	@Test
	public void testAdicionarAnamnese() {

		Anamnese anamnese = new Anamnese();
		anamnese.setNome("anamnese1");
		anamnese.setDescricao("descrição1");
		anamnese.setId(1111);

		PacienteAnamnese pacienteAnamnese = new PacienteAnamnese();
		pacienteAnamnese.setAnamnese(anamnese);
		pacienteAnamnese.setDescricao("descrição1");

		Paciente paciente = new Paciente();
		paciente.setCidade("Quixadá");
		paciente.setCpf("1111");
		paciente.setNome("José");

		pacienteService.adicionarAnamnese(paciente, pacienteAnamnese);

		List<PacienteAnamnese> pacienteAnamneseList = paciente.getPacienteAnamneses();

		assertEquals(anamnese, pacienteAnamneseList.get(0).getAnamnese());

	}

}
