package ufc.npi.prontuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.model.Usuario;

public interface TokenRepository extends JpaRepository<Token, String> {

	Token findByUsuario(Usuario usuario);
}
