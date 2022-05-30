package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.PreferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("preferencia")
public class PreferenciaControllerImpl {

    @Autowired
    private PreferenciaService preferenciaService;

    @GetMapping("")
    public ResponseEntity<List<Preferencia>> findAll(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(preferenciaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Preferencia> find(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") Preferencia preferencia) {
        return ResponseEntity.ok(preferencia);
    }

    @PostMapping("")
    public ResponseEntity<Preferencia> create(@AuthenticationPrincipal Usuario usuario,
    @RequestBody Preferencia preferencia) {
        return ResponseEntity.ok(preferenciaService.create(preferencia));
    }

    @PutMapping("")
    public ResponseEntity<Preferencia> update(@AuthenticationPrincipal Usuario usuario,
    @RequestBody Preferencia preferencia) {
        return ResponseEntity.ok(preferenciaService.update(preferencia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") Preferencia preferencia) {
        try {
            preferenciaService.delete(preferencia);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
