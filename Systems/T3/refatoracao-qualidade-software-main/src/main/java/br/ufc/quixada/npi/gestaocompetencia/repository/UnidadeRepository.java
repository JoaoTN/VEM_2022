package br.ufc.quixada.npi.gestaocompetencia.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Integer>{

	@Query("SELECT DISTINCT m.unidades FROM UnidadeMapeada m")
    List<Unidade> findAllWithMapeamento();
	
	@Query("SELECT DISTINCT um.unidade FROM Mapeamento m join UnidadeMapeada um on m.id = um.mapeamento")
    List<Unidade> findAllFromMapeamento();
	

	List<Unidade> findAllUnidadesByChefe(Usuario usuario);

	List<Unidade> findByUnidadePai(Unidade unidade);
	@Query(
        "SELECT  DISTINCT um.unidades " +
        "FROM UnidadeMapeada um " +
        "WHERE um.unidade = :unidade"
    )
	List<Unidade> findAllSubunidadesByUnidade(Unidade unidade);
}