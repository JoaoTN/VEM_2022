package br.ufc.quixada.npi.gestaocompetencia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufc.quixada.npi.gestaocompetencia.model.Competencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoCompetencia;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Integer> {
	
	@Query("select c from Competencia c ORDER BY c.nome")
	List<Competencia> findCompetencias();
	
	@Query("SELECT c " + 
			"FROM Competencia c " + 
			"WHERE c in :competencias AND c.tipo = 'COMPORTAMENTAL'"+
			"  ORDER BY c.nome")
	List<Competencia> getComportamentosComportamentais(@Param(value = "competencias") List<Competencia> competencias);
	
	List<Competencia> findByTipo(TipoCompetencia tipo);

	@Query("SELECT c "
		+ "FROM Competencia c "
		+ "WHERE (c.unidade is null OR c.unidade = :unidade) AND c.tipo = :tipo"
		+ "  ORDER BY c.nome")
	List<Competencia> findByUnidadeAndTipo(@Param(value = "unidade")Unidade unidade, @Param(value = "tipo") TipoCompetencia tipo);

	List<Competencia> findAllByTipoEqualsAndUnidadeIsNull(TipoCompetencia tipo);
}
