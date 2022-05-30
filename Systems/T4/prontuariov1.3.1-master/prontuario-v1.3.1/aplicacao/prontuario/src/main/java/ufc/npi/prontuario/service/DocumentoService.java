package ufc.npi.prontuario.service;

import org.springframework.web.multipart.MultipartFile;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Documento;
import ufc.npi.prontuario.model.DocumentoDownload;
import ufc.npi.prontuario.model.Paciente;

public interface DocumentoService {

	void salvar(Paciente paciente, MultipartFile[] files) throws ProntuarioException;
	
	Documento buscarArquivo(Documento documento) throws ProntuarioException;
	
	void deletar(Documento documento, Paciente paciente);
	
	DocumentoDownload downloadDocumento(Documento documento, String procedimento);
}
