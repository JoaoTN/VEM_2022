package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ufc.npi.prontuario.model.Papel;
import ufc.npi.prontuario.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	public Usuario findByEmail(String email);
	
	public Usuario findByEmailOrMatricula(String email, String matricula);

	public List<Usuario> findByIdNotInAndPapeisOrderByNome(List<Integer> users, Papel papel);

	public List<Usuario> findByPapeisOrderByNome(Papel papel);

	public Usuario findByMatricula(String matricula);

	public List<Usuario> findAllByEmail(String email);

	public List<Usuario> findAllByMatricula(String matricula);
}
