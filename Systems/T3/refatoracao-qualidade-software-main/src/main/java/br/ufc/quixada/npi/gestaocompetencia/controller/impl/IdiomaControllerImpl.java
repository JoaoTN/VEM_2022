package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.service.IdiomaService;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilIdiomaService;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("idioma")
public class IdiomaControllerImpl {

    @Autowired
    private PerfilIdiomaService perfilIdiomaService;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private IdiomaService idiomaService;

    @GetMapping("")
    public ResponseEntity<List<Idioma>> findAll(@AuthenticationPrincipal Usuario usuario) {
        Perfil perfil = perfilService.findByUsuario(usuario);
        List<Idioma> idiomas = idiomaService.findByUsuario(usuario);

        if (perfil != null) {
            List<PerfilIdioma> perfilIdiomas = perfilIdiomaService.findByPerfil(perfil);
            for (PerfilIdioma p : perfilIdiomas) {
                idiomas.removeIf(i -> p.getIdioma() != null && i.equals(p.getIdioma()));
            }
        }

        return ResponseEntity.ok(idiomas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Idioma> find(@AuthenticationPrincipal Usuario usuario, @PathVariable("id") Idioma idioma) {
        return ResponseEntity.ok(idioma);
    }

    @PostMapping("")
    public ResponseEntity<Idioma> create(@AuthenticationPrincipal Usuario usuario, @RequestBody Idioma idioma) {
        if(idioma == null) {
            throw new GestaoCompetenciaException("Idioma necessário para o cadastrado!");
        } else {
            idioma.setUsuario(usuario);
            return ResponseEntity.ok(idiomaService.create(idioma));
        }
    }

    @PutMapping("")
    public ResponseEntity<Idioma> update(@AuthenticationPrincipal Usuario usuario, @RequestBody Idioma idioma) {
        if(idioma == null) {
            throw new GestaoCompetenciaException("Idioma necessário para a alteração!");
        } else {
            idioma.setUsuario(usuario);
            return ResponseEntity.ok(idiomaService.update(idioma));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Usuario usuario, @PathVariable("id") Idioma idioma) {
        try {
            idiomaService.delete(idioma);
            return ResponseEntity.noContent().build();
        } catch(ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
