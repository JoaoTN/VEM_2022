package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

	Perfil findByUsuario(Usuario usuario);

	@Query(
			"SELECT p.pathCurriculum FROM Perfil p " +
					"WHERE p = :perfil"
	)
	String findPathCurriculum(Perfil perfil);
}
