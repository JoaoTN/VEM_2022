package br.ufc.quixada.npi.gestaocompetencia.repository;
import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import br.ufc.quixada.npi.gestaocompetencia.model.Responsabilidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

@Repository
public interface ResponsabilidadeRepository extends JpaRepository<Responsabilidade, Integer>, JpaSpecificationExecutor<Responsabilidade>{

	List<Responsabilidade> findByUnidadeAndMapeamento(Unidade unidade, Mapeamento mapeamento);

	List<Responsabilidade> findByUsuarioGestor(Usuario usuario);

	List<Responsabilidade> findByUnidadeAndMapeamentoAndConsolidadaTrue(Unidade unidade, Mapeamento mapeamento);

}
