package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.SetUsuarioId;
import ufc.npi.prontuario.model.Tratamento;

@DatabaseSetup(PatologiaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {PatologiaServiceTest.DATASET})
public class PatologiaServiceTest extends AbstractServiceTest{

	public static final String DATASET = "/database-tests-patologia.xml";
	
	@Autowired
	private PatologiaService patologiaService;
	
	@Test
	public void testBuscarPatologiasEmTratamento(){
		Odontograma odontograma = new Odontograma();
		odontograma.setId(1);
		Aluno aluno = new Aluno();
		SetUsuarioId.setIdUsuario(aluno,1);
		assertEquals(2, patologiaService.buscarPatologiasOdontograma(odontograma, aluno).size());
	}
	
	@Test
	public void testBuscarPatologiasTratadas(){
		Odontograma odontograma = new Odontograma();
		odontograma.setId(1);
		
		assertEquals(1, patologiaService.buscarPatologiasTratadas(odontograma).size());
	}
	
	@Test
	public void testSalvar() throws ProntuarioException{
		String faceDente = "24_O";
		String local = "FACE";
		Integer idOdontograma = 1;
		String descricao = "DIAGNÓSTICO DE PATOLOGIAS";
		List<Integer> idPatologias = new ArrayList<Integer>();
		idPatologias.add(1);
		idPatologias.add(2);
		Aluno aluno = new Aluno();
		SetUsuarioId.setIdUsuario(aluno,1);
		List<Patologia> patologias = patologiaService.salvar(faceDente, idPatologias, local, idOdontograma, descricao, aluno);
		
		Odontograma odontograma = new Odontograma();
		odontograma.setId(idOdontograma);
		assertEquals(true, patologiaService.buscarPatologiasOdontograma(odontograma, aluno).containsAll(patologias));
	}
	
	@Test
	public void testTratar(){
		Odontograma odontograma = new Odontograma();
		odontograma.setId(1);
		Aluno aluno = new Aluno();
		SetUsuarioId.setIdUsuario(aluno,1);
		//Patologia patologia = patologiaService.buscarPatologiasOdontograma(odontograma, aluno).get(1);
		Tratamento tratamento = new Tratamento();
		tratamento.setId(1);
		
		//patologiaService.tratar(patologia, tratamento);
		
		assertEquals(2, patologiaService.buscarPatologiasTratadas(odontograma).size());
	}
	
	
}