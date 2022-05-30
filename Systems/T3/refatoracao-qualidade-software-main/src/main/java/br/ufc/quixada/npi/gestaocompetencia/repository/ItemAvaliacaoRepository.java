package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAvaliacaoRepository extends JpaRepository<ItemAvaliacao, Long>, JpaSpecificationExecutor<ItemAvaliacao> {

    ItemAvaliacao findByAvaliacaoAndFator(Avaliacao avaliacao, Comportamento fator);

    ItemAvaliacao findByAvaliacaoAndResponsabilidade(Avaliacao avaliacao, Responsabilidade responsabilidade);
}
