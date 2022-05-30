package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.AreaPerfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.ufc.quixada.npi.gestaocompetencia.model.Idioma;

import java.util.List;

@Repository
public interface IdiomaRepository extends JpaRepository<Idioma, Integer> {

    @Query(
            "SELECT i FROM Idioma i " +
                    "WHERE i.usuario is null OR i.usuario = :usuario"
    )
    List<Idioma> findAllByUsuario(Usuario usuario);
}
