package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.controller.ItemAvaliacaoController;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.EscalaAvaliacao;
import br.ufc.quixada.npi.gestaocompetencia.service.ItemAvaliacaoService;
import br.ufc.quixada.npi.gestaocompetencia.service.impl.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("itens-avaliacao")
public class ItemAvaliacaoControllerImpl implements ItemAvaliacaoController {
    @Autowired
    private ItemAvaliacaoService itemAvaliacaoService;

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Override
    @GetMapping({"", "/"})
    public ResponseEntity<List<ItemAvaliacao>> filterItemAvaliacao(
            @RequestParam(name = "avaliacao", required=false) Avaliacao[] avaliacoes,
            @RequestParam(name = "responsabilidade", required = false) Responsabilidade[] responsabilidades,
            @RequestParam(name = "comportamento", required = false) Comportamento[] comportamentos,
            @RequestParam(name = "nota", required = false) EscalaAvaliacao[] notas,
            @AuthenticationPrincipal Usuario avaliador
    ){
        ItemAvaliacaoSearch itemAvaliacaoSearch;

        // Ajustes para que tanto o chefe como o vice-chefe possam filtrar os itens de avaliação
        if(avaliador.getUnidade().getChefe() != null && avaliador.getUnidade().getChefe().equals(avaliador)) {
            itemAvaliacaoSearch = new ItemAvaliacaoSearch(avaliacoes, responsabilidades, comportamentos, notas, avaliador, avaliador.getUnidade().getViceChefe());
        } else if(avaliador.getUnidade().getViceChefe() != null && avaliador.getUnidade().getViceChefe().equals(avaliador)) {
            itemAvaliacaoSearch = new ItemAvaliacaoSearch(avaliacoes, responsabilidades, comportamentos, notas, avaliador, avaliador.getUnidade().getChefe());
        } else {
            itemAvaliacaoSearch = new ItemAvaliacaoSearch(avaliacoes, responsabilidades, comportamentos, notas, avaliador, avaliador);
        }
        
        ItemAvaliacaoSpecification itemAvaliacaoSpecification = new ItemAvaliacaoSpecification(itemAvaliacaoSearch);
        return ResponseEntity.ok(itemAvaliacaoService.search(itemAvaliacaoSpecification));
    }

    @Override
    @GetMapping("/{item}")
    public ResponseEntity<ItemAvaliacao> find(@PathVariable("item") ItemAvaliacao itemAvaliacao) {
        return ResponseEntity.ok(itemAvaliacao);
    }

    @Override
    @GetMapping("/avaliacao/{avaliacao}")
    public ResponseEntity<List<ItemAvaliacao>> findAllByAvaliacao(Avaliacao avaliacao) {
        return ResponseEntity.ok(avaliacao.getItens());
    }

    @GetMapping("/diagnostico/{diagnostico}/{tipo}/{perspectiva}")
    public ResponseEntity<List<ItemAvaliacao>> findAllByTipo(Diagnostico diagnostico, @PathVariable("tipo") Avaliacao.TipoAvaliacao tipo,
     @PathVariable Avaliacao.Perspectiva perspectiva, @AuthenticationPrincipal Usuario usuario) {
        List<ItemAvaliacao> itens = new ArrayList<>();
        List<Avaliacao> avaliacoes = avaliacaoService.find(usuario, diagnostico, tipo, perspectiva);

        if(avaliacoes.isEmpty() && usuario.getUnidade().getChefe() != null && usuario.getUnidade().getChefe().equals(usuario)) {
            avaliacoes = avaliacaoService.find(usuario.getUnidade().getViceChefe(), diagnostico, tipo, perspectiva);
        } else if(avaliacoes.isEmpty() && usuario.getUnidade().getViceChefe() != null && usuario.getUnidade().getViceChefe().equals(usuario)) {
            avaliacoes = avaliacaoService.find(usuario.getUnidade().getChefe(), diagnostico, tipo, perspectiva);
        }

        for (Avaliacao avaliacao : avaliacoes) {
            itens.addAll(avaliacao.getItens());
        }

        return ResponseEntity.ok(itens);
    }

    @Override
    @PutMapping("/{item}")
    public ResponseEntity<ItemAvaliacao> update(@PathVariable("item") ItemAvaliacao itemAvaliacao, @RequestBody ItemAvaliacao itemAvaliacaoAtualizado) {
        itemAvaliacao.setNota(itemAvaliacaoAtualizado.getNota());
        itemAvaliacao.setNaoAplica(itemAvaliacaoAtualizado.isNaoAplica());
        return ResponseEntity.ok(itemAvaliacaoService.update(itemAvaliacao));
    }

    @Override
    @DeleteMapping("/{item}")
    public ResponseEntity<Void> delete(@PathVariable("item") ItemAvaliacao itemAvaliacao) {
        try {
            itemAvaliacaoService.delete(itemAvaliacao);
            return ResponseEntity.noContent().build();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
