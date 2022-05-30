package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.AreaCapacitacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Competencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaCapacitacaoRepository extends JpaRepository<AreaCapacitacao, Integer> {

    @Query(
            "SELECT a FROM AreaCapacitacao a " +
                    "WHERE a.usuario is null OR a.usuario = :usuario"
    )
    List<AreaCapacitacao> findAllByUsuario(Usuario usuario);

    AreaCapacitacao findByUsuarioAndCompetencia(Usuario usuario, Competencia competencia);

    @Query(
            "SELECT a FROM AreaCapacitacao a " +
                    "WHERE a.competencia = :competencia"
    )
    List<AreaCapacitacao> findAllByCompetencia(Competencia competencia);
}
