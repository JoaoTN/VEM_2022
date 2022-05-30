package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ALTERAR_SENHA;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.model.SetSenhaUsuario;
import ufc.npi.prontuario.repository.UsuarioRepository;
import ufc.npi.prontuario.service.TokenService;
import ufc.npi.prontuario.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TokenService tokenService;

	@Override
	public void alterarSenha(Integer usuarioId, String senhaAtual, String novaSenha) throws ProntuarioException {
		Usuario aux = usuarioRepository.findOne(usuarioId);

		if (new BCryptPasswordEncoder().matches(senhaAtual, aux.getSenha())) {
			SetSenhaUsuario.setUsuarioSenha(aux,novaSenha);
			aux.encodePassword();
		} else {
			throw new ProntuarioException(ERRO_ALTERAR_SENHA);
		}

		usuarioRepository.save(aux);
	}

	@Override
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Override
	public void recuperarSenha(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email);
		
		if (Objects.nonNull(usuario)) {
			tokenService.enviarTokenDeRecuperacao(usuario);
		}
		
	}

	@Override
	public void novaSenha(Token token, String senha) {
		if (token != null) {
			Usuario usuario = token.getUsuario();
			SetSenhaUsuario.setUsuarioSenha(usuario,senha);
			usuario.encodePassword();

			usuarioRepository.save(usuario);

			tokenService.deletar(token);
		}
	}

	@Override
	public void alterarDados(Integer id, String nome, String email, String matricula) {
		Usuario usuario = usuarioRepository.getOne(id);

		usuario.setNome(nome);
		usuario.setMatricula(matricula);
		usuario.setEmail(email);

		usuarioRepository.save(usuario);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, usuario.getPassword(), usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
