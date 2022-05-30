package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Servidor;

public interface ServidorService {
	
	void salvar(Servidor servidor) throws ProntuarioException;
	
	void atualizar(Servidor servidor) throws ProntuarioException;
	
	//List<Servidor> buscarTudo();

	List<Servidor> buscarProfessores();
	
	Servidor buscarPorId(Integer id);

	void removerServidor(Integer id) throws ProntuarioException;
}
