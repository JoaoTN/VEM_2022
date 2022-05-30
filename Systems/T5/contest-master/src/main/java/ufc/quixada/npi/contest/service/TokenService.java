package ufc.quixada.npi.contest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.model.Token;
import ufc.quixada.npi.contest.repository.TokenRepository;
import ufc.quixada.npi.contest.util.Constants;

import java.util.UUID;

@Service
public class TokenService {

	@Autowired
	private TokenRepository tokenRepository;

	public Token buscarPorUsuario(Pessoa pessoa) {
		return tokenRepository.findByPessoa(pessoa);
	}

	public Token buscar(String token) {
		return tokenRepository.findOne(token);
	}

	public boolean existe(String token) {
		return tokenRepository.exists(token);
	}

	public void salvar(Token token) {
		tokenRepository.save(token);
	}

	public void deletar(Token token) {
		tokenRepository.delete(token);
	}
	
	public Token novoToken(Pessoa pessoa, String acao) throws IllegalArgumentException{
		Token token = criarToken(pessoa);
		
		switch(acao){
		case Constants.ACAO_COMPLETAR_CADASTRO:
			setAcao(token, Constants.ACAO_COMPLETAR_CADASTRO);
			break;
		case Constants.ACAO_RECUPERAR_SENHA:
			setAcao(token, Constants.ACAO_RECUPERAR_SENHA);
			break;
		default:
			throw new IllegalArgumentException("Acao não existente para geração do token.");
		}
		
		salveToken(token);
		
		return token;
		
	}
	
	private Token criarToken(Pessoa pessoa) {
		Token token = new Token();
		token.setPessoa(pessoa);
		return token;
	}
	
	private Token setAcao(Token token, String constants) {
		token.setAcao(constants);
		return token;
	}
	
	private void salveToken(Token token) {
		do {
			token.setToken(UUID.randomUUID().toString());
		} while (this.existe(token.getToken()));
		
		this.salvar(token);
	}

}