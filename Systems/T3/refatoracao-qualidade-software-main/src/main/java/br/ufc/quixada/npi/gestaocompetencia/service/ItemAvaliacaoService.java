package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.ItemAvaliacao;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ItemAvaliacaoService {

    ItemAvaliacao findByFator(ItemAvaliacao itemAvaliacao);

    ItemAvaliacao findByResponsabilidade(ItemAvaliacao itemAvaliacao);

    List<ItemAvaliacao> findAll();

    List<ItemAvaliacao> search (Specification<ItemAvaliacao> specification);

    ItemAvaliacao update(ItemAvaliacao itemAvaliacao);

    void delete(ItemAvaliacao itemAvaliacao);

    ItemAvaliacao save(ItemAvaliacao itemAvaliacao);
}
