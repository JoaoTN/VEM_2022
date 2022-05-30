package ufc.npi.prontuario.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.repository.OdontogramaRepository;
import ufc.npi.prontuario.service.OdontogramaService;

@Service
public class OdontogramaServiceImpl implements OdontogramaService{

	@Autowired
	private OdontogramaRepository odontogramaRepository;
	
	@Override
	public Odontograma buscarPorPacienteId(Integer id) {
		return odontogramaRepository.findByPaciente_id(id);
	}

	@Override
	public Odontograma buscarPorId(Integer id) {
		return odontogramaRepository.findOne(id);
	}

}
