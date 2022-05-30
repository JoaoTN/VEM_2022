package br.ufc.quixada.npi.gestaocompetencia.repository;

import java.time.LocalDate;
import java.util.List;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MapeamentoRepository extends JpaRepository<Mapeamento, Integer>,JpaSpecificationExecutor<Mapeamento>{
    
    @Query(
            "SELECT DISTINCT um " +
            "FROM UnidadeMapeada um " +
            "WHERE um.mapeamento = :mapeamento"
    )
    List<UnidadeMapeada> findAllUnidadesMapeadasByMapeamento(Mapeamento mapeamento);

    @Query(
            "SELECT DISTINCT um.unidade " +
            "FROM UnidadeMapeada um " +
            "WHERE um.mapeamento.inicioPerComportComissao <= :data " +
            "AND um.mapeamento.fimPerComportComissao >= :data"
    )
    List<Unidade> findAllUnidadesInPerComportComissao(@Param(value = "data")LocalDate data);

    @Query(
            "SELECT DISTINCT um.unidade " +
                    "FROM UnidadeMapeada um " +
                    "WHERE um.mapeamento.inicioPerRespChefiaSuperior <= :data " +
                    "AND um.mapeamento.fimPerRespChefiaSuperior >= :data"
    )
    List<Unidade> findAllUnidadesInPerRespChefiaSuperior(@Param(value = "data")LocalDate data);

    @Query(
            "SELECT  DISTINCT um.unidades " +
            "FROM UnidadeMapeada um " +
            "WHERE um.unidade = :unidade " +
            "AND um.mapeamento.inicioPerComportComissao <= :data " +
            "AND um.mapeamento.fimPerComportComissao >= :data"
    )
    List<Unidade> findAllSubunidadesInPerComportComissao(@Param(value = "data")LocalDate data, Unidade unidade);

    @Query(
            "SELECT  DISTINCT um.unidades " +
                    "FROM UnidadeMapeada um " +
                    "WHERE um.unidade = :unidade " +
                    "AND um.mapeamento.inicioPerRespCom <= :data " +
                    "AND um.mapeamento.fimPerRespCom >= :data"
    )
    List<Unidade> findAllSubunidadesInPerRespCom(@Param(value = "data")LocalDate data, Unidade unidade);

    @Query(
            "SELECT  DISTINCT um.unidades " +
                    "FROM UnidadeMapeada um " +
                    "WHERE um.unidade = :unidade " +
                    "AND um.mapeamento.inicioPerRespConsol <= :data " +
                    "AND um.mapeamento.fimPerRespConsol >= :data"
    )
    List<Unidade> findAllSubunidadesInPerRespConsol(@Param(value = "data")LocalDate data, Unidade unidade);

	@Query("SELECT um.mapeamento FROM UnidadeMapeada um WHERE um.unidade = :unidade OR :unidade MEMBER OF um.unidades ORDER BY um.mapeamento.inicioPerComportServidor DESC")
    List<Mapeamento> findLast(Pageable pageable, Unidade unidade);

    @Query("SELECT um.mapeamento FROM UnidadeMapeada um WHERE um.unidade = :unidade OR :unidade MEMBER OF um.unidades ORDER BY um.mapeamento.inicioPerComportServidor DESC")
    List<Mapeamento> findAllByUnidade(Unidade unidade);

    List<Mapeamento> findAll();

    @Query("SELECT um.mapeamento FROM UnidadeMapeada um WHERE um.mapeamento = :mapeamento AND (um.unidade = :unidade OR :unidade MEMBER OF um.unidades)")
    Mapeamento findByUnidadeAndMapeamento(Unidade unidade, Mapeamento mapeamento);

    @Query("SELECT um.unidades FROM UnidadeMapeada um WHERE um.mapeamento = :mapeamento")
    List<Unidade> findUnidades(Mapeamento mapeamento);

    @Query("SELECT um.unidade FROM UnidadeMapeada um WHERE um.mapeamento = :mapeamento")
    List<Unidade> findParentUnidades(Mapeamento mapeamento);

    @Query(
        value ="select * from Mapeamento m WHERE m.fim_per_resp_com < CURRENT_DATE",
        nativeQuery = true)
    List<Mapeamento> getFinalizados();
}