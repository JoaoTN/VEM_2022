package br.ufc.quixada.npi.gestaocompetencia.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.quixada.npi.gestaocompetencia.model.Avaliacao;
import br.ufc.quixada.npi.gestaocompetencia.model.ItemAvaliacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Comportamento;

@Repository
public interface AvalicaoComportamentalRepository extends JpaRepository<ItemAvaliacao, Long>{

	ItemAvaliacao findByAvaliacaoAndFator(Avaliacao avaliacao, Comportamento fator);

	List<ItemAvaliacao> findByAvaliacao(Avaliacao avaliacao);

}
