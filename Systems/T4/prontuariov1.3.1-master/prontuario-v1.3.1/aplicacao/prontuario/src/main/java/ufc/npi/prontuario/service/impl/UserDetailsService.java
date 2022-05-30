package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.PESSOA_NAO_ENCONTRADA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.repository.UsuarioRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String param) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmailOrMatricula(param, param);

		if (usuario == null) {
			throw new UsernameNotFoundException(PESSOA_NAO_ENCONTRADA);
		}

		return usuario;
	}

}
