package ufc.npi.prontuario.service;

import ufc.npi.prontuario.model.Token;

public interface EmailService {

	void emailRecuperacaoSenha(Token token);
	
	void notificarAtendimentoAndamento();
	
	void notificarAtendimentoAvaliacao();

}
