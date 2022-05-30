package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.model.SetUsuarioId;

@DatabaseSetup(ProcedimentoServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {ProcedimentoServiceTest.DATASET})
public class ProcedimentoServiceTest extends AbstractServiceTest{
	
	public static final String DATASET = "/database-tests-procedimento.xml";
	
	@Test
	public void testSalvar() {
		Aluno aluno = new Aluno();
		SetUsuarioId.setIdUsuario(aluno,1);
		List<Integer> idProcedimentos = new ArrayList<Integer>();
		idProcedimentos.add(1);
		idProcedimentos.add(2);
		
		List<Procedimento> procedimentos = new ArrayList<Procedimento>();
		
		/*try {
			procedimentos = procedimentoService.salvar(faceDente, idProcedimentos, local, idOdontograma, descricao, aluno, false);
		} catch (ProntuarioException e) {
			e.printStackTrace();
		}*/
		
		assertEquals(2, procedimentos.size());
	}
}