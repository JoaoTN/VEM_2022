package ufc.npi.prontuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ufc.npi.prontuario.model.TipoPatologia;

public interface TipoPatologiaRepository extends JpaRepository<TipoPatologia, Integer> {

    TipoPatologia findByNome(String nome);

    @Query("FROM TipoPatologia ORDER BY nome")
    List<TipoPatologia> findAll();
    
    List<TipoPatologia> findByNomeContainingIgnoreCase(String nome);

    TipoPatologia findByCodigo(String codigo);
}
