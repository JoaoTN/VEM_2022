package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Usuario;

public interface PatologiaService {

	public List<Patologia> salvar(String faceDente, List<Integer> idPatologias, String localString,
			Integer idOdontograma, String descricao, Aluno aluno) throws ProntuarioException;

	void tratar(Patologia patologia);

	List<Patologia> buscarPatologiasOdontograma(Odontograma odontograma, Usuario usuario);

	List<Patologia> buscarPatologiasTratadas(Odontograma odontograma);

	List<Patologia> buscarPatologiasDentePaciente(Odontograma odontograma, String face, String dente, Aluno aluno);

	void deletar(Patologia patologia);
}
