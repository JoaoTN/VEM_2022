package br.ufc.quixada.npi.gestaocompetencia.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.UsuarioService;

@Service
public class UserDetailsCustom implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.findByEmail(username);

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário e/ou senha inválidos");
		} else {
			return usuario;
		}
	}
}
