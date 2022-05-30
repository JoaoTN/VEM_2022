package br.ufc.quixada.npi.gestaocompetencia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufc.quixada.npi.gestaocompetencia.model.Comportamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.CategoriaComportamento;

@Repository
public interface ComportamentoRepository extends JpaRepository<Comportamento, Long>, JpaSpecificationExecutor<Comportamento>{

	@Query(
			"SELECT c " +
			"FROM Comportamento c " +
			"WHERE c.servidor = :servidor " +
				"AND c.mapeamento = :mapeamento " +
				"AND c.categoria = :categoria ORDER BY c.descricao"
	)
	List<Comportamento> findByServidorAndMapeamentoAndCategoria(@Param(value = "servidor")Usuario servidor,@Param(value = "mapeamento") Mapeamento mapeamento,@Param(value = "categoria") CategoriaComportamento categoria);
	
	@Query(
			"SELECT count(c) " +
					"FROM Comportamento c " +
					"WHERE c.servidor = :servidor " +
					"AND c.mapeamento = :mapeamento "
	)
	Integer countByServidorAndMapeamentoAndExcluidoFalse(Usuario servidor, Mapeamento mapeamento);

	List<Comportamento> findByMapeamentoAndConsolidadoTrueOrderByDescricaoAtualizada(Mapeamento mapeamento);
        
        @Query(
			"SELECT c " +
			"FROM Comportamento c " +
			"WHERE c.mapeamento = :mapeamento"
	)
	List<Comportamento> findByMapeamento(Mapeamento mapeamento);
}
