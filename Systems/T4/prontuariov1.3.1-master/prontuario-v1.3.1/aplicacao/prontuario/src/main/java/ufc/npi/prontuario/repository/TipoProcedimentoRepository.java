package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ufc.npi.prontuario.model.TipoProcedimento;

public interface TipoProcedimentoRepository extends JpaRepository<TipoProcedimento, Integer> {

    TipoProcedimento findByNome(String nome);

    @Query("FROM TipoProcedimento ORDER BY nome")
    List<TipoProcedimento> findAll();
    
    List<TipoProcedimento> findByNomeContainingIgnoreCase(String nome);
}
