package br.ufc.quixada.npi.gestaocompetencia.service;

public interface ArquivoService {
	
	String uploadProfileImage(String userID, String base64, String contentType) throws Exception;

}
