package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.AreaPerfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoAreaPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaPerfilRepository extends JpaRepository<AreaPerfil, Integer> {

    @Query(
            "SELECT a FROM AreaPerfil a " +
                    "WHERE a.usuario is null OR a.usuario = :usuario"
    )
    List<AreaPerfil> findAllByUsuario(Usuario usuario);

    @Query(
            "SELECT a FROM AreaPerfil a " +
                    "WHERE a.tipo = :tipoAreaPerfil " +
                    "AND (a.usuario is null OR a.usuario = :usuario)"
    )
    List<AreaPerfil> findAllByTipoAndUsuario(TipoAreaPerfil tipoAreaPerfil, Usuario usuario);

    List<AreaPerfil> findAllByTipoEquals(TipoAreaPerfil tipoAreaPerfil);
}
