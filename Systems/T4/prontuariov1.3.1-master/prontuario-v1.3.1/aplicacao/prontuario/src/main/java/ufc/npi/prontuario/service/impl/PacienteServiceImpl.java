package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_PACIENTE_CNS_EXISTENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_PACIENTE_CPF_EXISTENTE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.GetPacienteId;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PacienteAnamnese;
import ufc.npi.prontuario.repository.PacienteRepository;
import ufc.npi.prontuario.service.PacienteService;

@Service
public class PacienteServiceImpl implements PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;
////////////////////////////////////////////////////////////////////
    @Override
    public void salvar(Paciente paciente) throws ProntuarioException {
        if (pacienteIdNull(paciente)) {
            criarOdontogramaEmPaciente(paciente);
        }

        if (!pacienteCpfVazio(paciente)) {
            verificarSePacienteTemCPF(paciente);
        }

        if (!pacienteCnsNull(paciente)) {
            verificarSePacienteTemCNS(paciente);
        }
		pacienteRepository.save(paciente);
	}
    
    public boolean pacienteIdNull(Paciente paciente) {
    	return GetPacienteId.mostrarIdPaciente(paciente) == null;
    }
    
    public boolean pacienteCpfVazio(Paciente paciente) {
    	return paciente.getCpf().isEmpty();
    }
    public boolean pacienteCnsNull(Paciente paciente) {
    	return paciente.getCns().isEmpty();
    }
////////////////////////////////////////////////////////////////////
	private void inicializarOdontogramaPaciente(Paciente paciente) {
		if (GetPacienteId.mostrarIdPaciente(paciente) == null) {
			paciente.setOdontograma(new Odontograma());
		}
	}

	private void verificarCpfDuplicado(Paciente novoPaciente) throws ProntuarioException {
		if (!novoPaciente.getCpf().isEmpty()) {
			Paciente pacienteExistente = buscarByCpf(novoPaciente.getCpf());

			if (pacienteExistente != null && !pacienteExistente.equals(novoPaciente)) {
				throw new ProntuarioException(ERRO_PACIENTE_CPF_EXISTENTE);
			}
		}
	}

	private void verificarCnsDuplicado(Paciente novoPaciente) throws ProntuarioException {
		if (!novoPaciente.getCns().isEmpty()) {
			Paciente pacienteExistente = buscarByCns(novoPaciente.getCns());
      
			if (pacienteExistente != null && !pacienteExistente.equals(novoPaciente)) {
				throw new ProntuarioException(ERRO_PACIENTE_CNS_EXISTENTE);
			}
		}
	}

    private void criarOdontogramaEmPaciente(Paciente paciente) {
        paciente.setOdontograma(new Odontograma());
    }

    private void verificarSePacienteTemCPF(Paciente paciente) throws ProntuarioException {
        Paciente pacienteExistente = buscarByCpf(paciente.getCpf());

        if (pacienteExistente != null && !pacienteExistente.equals(paciente)) {
            throw new ProntuarioException(ERRO_PACIENTE_CPF_EXISTENTE);
        }
    }

    private void verificarSePacienteTemCNS(Paciente paciente) throws ProntuarioException {
        Paciente pacienteExistente = buscarByCns(paciente.getCns());

        if (pacienteExistente != null && !pacienteExistente.equals(paciente)) {
            throw new ProntuarioException(ERRO_PACIENTE_CNS_EXISTENTE);
        }
    }

    @Override
    public List<Paciente> buscarTudo() {
        return pacienteRepository.findAll();
    }


	@Override
	public void adicionarAnamnese(Paciente paciente, PacienteAnamnese anamnese) {
		paciente.addPacienteAnamnese(anamnese);
		pacienteRepository.save(paciente);
	}

	@Override
	public Paciente buscarByCpf(String cpf) {

		return pacienteRepository.findByCpf(cpf);
	}

	@Override
	public Paciente buscarByCns(String cns) {
		return pacienteRepository.findByCns(cns);
	}
}
