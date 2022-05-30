package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.AreaCapacitacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Competencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.AreaCapacitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("area-capacitacao")
public class AreaCapacitacaoControllerImpl {

    @Autowired
    private AreaCapacitacaoService areaCapacitacaoService;

    @GetMapping("")
    public ResponseEntity<List<AreaCapacitacao>> findAll(@AuthenticationPrincipal Usuario usuario,
    @RequestParam(name = "competencia", required = false) Competencia competencia) {
        if(competencia == null) {
            return ResponseEntity.ok(areaCapacitacaoService.findByUsuario(usuario));
        } else {
            return ResponseEntity.ok(areaCapacitacaoService.findByCompetencia(competencia));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaCapacitacao> find(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") AreaCapacitacao areaCapacitacao) {
        return ResponseEntity.ok(areaCapacitacao);
    }

    @PostMapping("")
    public ResponseEntity<AreaCapacitacao> create(@AuthenticationPrincipal Usuario usuario,
    @RequestBody AreaCapacitacao areaCapacitacao) {
        if(areaCapacitacao == null) {
            throw new GestaoCompetenciaException("Área de interesse necessária para o cadastrado!");
        } else {
            areaCapacitacao.setUsuario(usuario);
            return ResponseEntity.ok(areaCapacitacaoService.create(areaCapacitacao));
        }
    }

    @PutMapping("")
    public ResponseEntity<AreaCapacitacao> update(@AuthenticationPrincipal Usuario usuario,
    @RequestBody AreaCapacitacao areaCapacitacao) {
        return ResponseEntity.ok(areaCapacitacaoService.update(areaCapacitacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") AreaCapacitacao areaCapacitacao) {
        try {
            areaCapacitacaoService.delete(areaCapacitacao);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
