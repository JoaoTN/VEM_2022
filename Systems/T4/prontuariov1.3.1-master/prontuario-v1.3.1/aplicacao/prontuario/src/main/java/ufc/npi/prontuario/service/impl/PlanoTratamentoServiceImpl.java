package ufc.npi.prontuario.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PlanoTratamento;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.PlanoTratamento.Status;
import ufc.npi.prontuario.repository.PlanoTratamentoRepository;
import ufc.npi.prontuario.service.PlanoTratamentoService;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADD_PLANO_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_EXCLUIR_PLANO_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_FINALIZAR_PLANO_TRATAMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR_EDITAR_PLANO_TRATAMENTO;

@Service
public class PlanoTratamentoServiceImpl implements PlanoTratamentoService {

	@Autowired
	private PlanoTratamentoRepository planoTratamentoRepository;

	@Override
	public List<PlanoTratamento> buscarPlanoTratamentoPorPaciente(Paciente paciente) {
		return planoTratamentoRepository.findAllByPaciente(paciente);
	}
//////////////////////////////////////////////////
	@Override
	public void excluirPlanoTratamento(PlanoTratamento planoTratamento) throws ProntuarioException {
		PlanoTratamento old = planoTratamentoRepository.findOne(planoTratamento.getId());
		if (!planoEmEspera(old)) {
			throw new ProntuarioException(ERROR_EXCLUIR_PLANO_TRATAMENTO);
		} else {
			planoTratamentoRepository.delete(old);
		}
	}
	
	public boolean planoEmEspera(PlanoTratamento old) {
		return old.getStatus().equals(Status.EM_ESPERA);
	}
///////////////////////////////////////////
	@Override
	public void salvar(PlanoTratamento planoTratamento, Servidor responsavel, Paciente paciente)
			throws ProntuarioException {
		if (pacienteNaoTemTratamento(planoTratamento)) {
			planoTratamento.setResponsavel(responsavel);
			planoTratamento.setPaciente(paciente);
			planoTratamentoRepository.save(planoTratamento);
		} else {
			throw new ProntuarioException(ERRO_ADD_PLANO_TRATAMENTO);
		}
	}
//////////////////////////////////////////////////////////////////////////////////
	private boolean pacienteNaoTemTratamento(PlanoTratamento planoTratamento) {
		List<Status> statusList = Arrays.asList(Status.EM_ESPERA, Status.EM_ANDAMENTO);
		Integer quantidateTratamentos = planoTratamentoRepository.countByPacienteAndClinicaAndStatusIn(
				pegarPaciente(planoTratamento), pegarClinica(planoTratamento), statusList);

		return quantidateTratamentos == 0;
	}
	public Paciente pegarPaciente(PlanoTratamento planoTratamento) {
		return planoTratamento.getPaciente();
	}
	
	public Disciplina pegarClinica(PlanoTratamento planoTratamento) {
		return planoTratamento.getClinica();
	}
	//////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////
	@Override
	public void finalizar(PlanoTratamento planoTratamento) throws ProntuarioException {
		PlanoTratamento old = planoTratamentoRepository.findOne(planoTratamento.getId());
		if (equalStatus(old)) {
		 	configurarStatus(old);
		 	planoTratamentoRepository.save(old);
		} else {
			throw new ProntuarioException(ERROR_FINALIZAR_PLANO_TRATAMENTO);
		}

	}
	public boolean equalStatus(PlanoTratamento old) {
		Status status = old.getStatus();
		return status.equals(Status.EM_ESPERA);
	} 
	
	public void configurarStatus(PlanoTratamento old) {
		old.setStatus(Status.CONCLUIDO);
	}
/////////////////////////////////////////////////////////////////
	@Override
	public List<PlanoTratamento> buscarPlanoTratamentoPorClinicaEStatus(Disciplina disciplina, String status) {

		if (!status.equalsIgnoreCase("")) {
			Status s = Status.valueOf(status);
			return planoTratamentoRepository.findByClinicaAndStatus(disciplina, s);
		} else {
			return planoTratamentoRepository.findByClinica(disciplina);
		}

	}
////////////////////////////////////////////////////////////////////////
	@Override
	public void editar(PlanoTratamento planoTratamento) throws ProntuarioException {
		PlanoTratamento old = planoTratamentoRepository.findOne(planoTratamento.getId());
		if (planoTratamentoConcluidoInterrompido(old)) {
			throw new ProntuarioException(ERROR_EDITAR_PLANO_TRATAMENTO);
		} else {
			planoTratamentoRepository.save(planoTratamento);
		}
	}
	
	public boolean planoTratamentoConcluidoInterrompido(PlanoTratamento old) {
		return old == null || (tratamentoConcluido(old) || tratamentoInterrompido(old));
	}
	
	public boolean tratamentoConcluido(PlanoTratamento old) {
		return old.getStatus().equals(Status.CONCLUIDO);
	}
	public boolean tratamentoInterrompido(PlanoTratamento old) {
		return old.getStatus().equals(Status.INTERROMPIDO);
	}
///////////////////////////////////////////////////////////////////////////
}
