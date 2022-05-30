package ufc.npi.prontuario.service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.model.Usuario;

public interface UsuarioService {

	Usuario buscarPorEmail(String email);

	void alterarSenha(Integer usuarioId, String senhaAtual, String novaSenha) throws ProntuarioException;

	void recuperarSenha(String email);

	void novaSenha(Token token, String senha);

	void alterarDados(Integer id, String nome, String email, String matricula);
}
