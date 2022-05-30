package ufc.npi.prontuario.service;

import ufc.npi.prontuario.model.Odontograma;

public interface OdontogramaService {

	public Odontograma buscarPorPacienteId(Integer id);
	
	public Odontograma buscarPorId(Integer id);
}
