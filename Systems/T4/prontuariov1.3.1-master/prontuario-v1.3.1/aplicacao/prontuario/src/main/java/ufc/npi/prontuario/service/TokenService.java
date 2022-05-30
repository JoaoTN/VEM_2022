package ufc.npi.prontuario.service;

import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.model.Usuario;

public interface TokenService {

	Token buscarPorUsuario(Usuario usuario);

	Token buscar(String token);

	boolean existe(String token);

	void salvar(Token token);

	void deletar(Token token);

	void enviarTokenDeRecuperacao(Usuario usuario);
}
