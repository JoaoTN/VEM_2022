package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.AreaPerfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoAreaPerfil;
import br.ufc.quixada.npi.gestaocompetencia.service.AreaPerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("area-perfil")
public class AreaPerfilControllerImpl {

    @Autowired
    private AreaPerfilService areaPerfilService;

    @GetMapping("")
    public ResponseEntity<List<AreaPerfil>> findAll(@AuthenticationPrincipal Usuario usuario,
    @RequestParam(name = "tipo", required = false) TipoAreaPerfil tipoAreaPerfil) {
        if(tipoAreaPerfil == null) {
            return ResponseEntity.ok(areaPerfilService.findByUsuario(usuario));
        } else {
            return ResponseEntity.ok(areaPerfilService.findByTipoAndUsuario(tipoAreaPerfil, usuario));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaPerfil> find(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") AreaPerfil areaPerfil) {
        return ResponseEntity.ok(areaPerfil);
    }

    @PostMapping("")
    public ResponseEntity<AreaPerfil> create(@AuthenticationPrincipal Usuario usuario,
    @RequestBody AreaPerfil areaPerfil) {
        if(areaPerfil == null) {
            throw new GestaoCompetenciaException("Área necessária para o cadastrado!");
        } else {
            areaPerfil.setUsuario(usuario);
            return ResponseEntity.ok(areaPerfilService.create(areaPerfil));
        }
    }

    @PutMapping("")
    public ResponseEntity<AreaPerfil> update(@AuthenticationPrincipal Usuario usuario,
    @RequestBody AreaPerfil areaPerfil) {
        if(areaPerfil == null) {
            throw new GestaoCompetenciaException("Área necessária para a alteração!");
        } else {
            areaPerfil.setUsuario(usuario);
            return ResponseEntity.ok(areaPerfilService.update(areaPerfil));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") AreaPerfil areaPerfil) {
        try {
            areaPerfilService.delete(areaPerfil);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
