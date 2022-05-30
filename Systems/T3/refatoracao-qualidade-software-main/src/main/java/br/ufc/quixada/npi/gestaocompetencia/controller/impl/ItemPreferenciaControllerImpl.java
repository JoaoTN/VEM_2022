package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.ItemPreferencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.ItemPreferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("item-preferencia")
public class ItemPreferenciaControllerImpl {

    @Autowired
    private ItemPreferenciaService itemPreferenciaService;

    @GetMapping("")
    public ResponseEntity<List<ItemPreferencia>> findAll(@AuthenticationPrincipal Usuario usuario,
    @RequestParam(name = "preferencia", required = false) Preferencia preferencia) {
        if(preferencia == null) {
            return ResponseEntity.ok(itemPreferenciaService.findAll());
        } else {
            return ResponseEntity.ok(itemPreferenciaService.findByPreferencia(preferencia));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemPreferencia> find(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") ItemPreferencia itemPreferencia) {
        return ResponseEntity.ok(itemPreferencia);
    }

    @PostMapping("")
    public ResponseEntity<ItemPreferencia> create(@AuthenticationPrincipal Usuario usuario,
    @RequestBody ItemPreferencia itemPreferencia) {
        return ResponseEntity.ok(itemPreferenciaService.create(itemPreferencia));
    }

    @PutMapping("")
    public ResponseEntity<ItemPreferencia> update(@AuthenticationPrincipal Usuario usuario,
    @RequestBody ItemPreferencia itemPreferencia) {
        return ResponseEntity.ok(itemPreferenciaService.update(itemPreferencia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") ItemPreferencia itemPreferencia) {
        try {
            itemPreferenciaService.delete(itemPreferencia);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
