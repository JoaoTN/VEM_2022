package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PacienteAnamnese;

public interface PacienteService {

	void salvar(Paciente paciente) throws ProntuarioException;
	
	List<Paciente> buscarTudo();

	void adicionarAnamnese(Paciente paciente, PacienteAnamnese anamnese);
	
	Paciente buscarByCpf(String cpf);

    Paciente buscarByCns(String cns);
}
