package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PlanoTratamento;
import ufc.npi.prontuario.model.Servidor;

public interface PlanoTratamentoService {

	List<PlanoTratamento> buscarPlanoTratamentoPorPaciente(Paciente paciente);
	void salvar(PlanoTratamento planoTratamento, Servidor responsavel, Paciente paciente) throws ProntuarioException;
	List<PlanoTratamento> buscarPlanoTratamentoPorClinicaEStatus(Disciplina disciplina, String status);
	void excluirPlanoTratamento(PlanoTratamento planoTratamento) throws ProntuarioException;	
	void finalizar(PlanoTratamento planoTratamento) throws ProntuarioException;
	void editar(PlanoTratamento planoTratamento) throws ProntuarioException;
}
