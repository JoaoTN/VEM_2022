package br.ufc.quixada.npi.gestaocompetencia.controller;

import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.EscalaAvaliacao;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemAvaliacaoController {
    @ApiOperation(
            value = "Retorna o item de avaliação a partir do id informado",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<ItemAvaliacao> find(ItemAvaliacao itemAvaliacao);

    @ApiOperation(
            value = "Retorna os itens de acordo com a avaliação",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<List<ItemAvaliacao>> findAllByAvaliacao(Avaliacao avaliacao);

    @ApiOperation(
            value = "Retorna todos os itens de avaliação",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<List<ItemAvaliacao>> filterItemAvaliacao(
            Avaliacao[] avaliacao, Responsabilidade[] responsabilidade, Comportamento[] comportamento,
            EscalaAvaliacao[] nota, Usuario usuario
    );

    @ApiOperation(
            value = "Edita o item de avaliação",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<ItemAvaliacao> update(ItemAvaliacao itemAvaliacao, ItemAvaliacao itemAvaliacaoAtualizado);

    @ApiOperation(
            value = "Exclui o item de avaliação",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<Void> delete(ItemAvaliacao itemAvaliacao);
}
