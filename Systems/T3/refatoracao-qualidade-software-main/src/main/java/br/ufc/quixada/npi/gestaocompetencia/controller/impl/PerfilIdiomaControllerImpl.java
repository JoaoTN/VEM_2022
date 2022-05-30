package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilIdiomaService;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("perfil-idioma")
public class PerfilIdiomaControllerImpl {

    @Autowired
    private PerfilIdiomaService perfilIdiomaService;

    @Autowired
    private PerfilService perfilService;

    private static final String PERFIL_NAO_CADASTRADO = "Perfil não cadastrado!";

    @GetMapping("")
    public ResponseEntity<List<PerfilIdioma>> find(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(
                perfilIdiomaService.findByPerfil(
                        this.findPerfil(usuario)
                )
        );
    }

    @PostMapping("")
    public ResponseEntity<PerfilIdioma> create(@AuthenticationPrincipal Usuario usuario,
    @RequestBody PerfilIdioma perfilIdioma) {
        return ResponseEntity.ok(
                perfilIdiomaService.create(
                        this.setDadosPerfilIdioma(perfilIdioma, usuario)
                )
        );
    }

    @PutMapping("")
    public ResponseEntity<PerfilIdioma> update(@AuthenticationPrincipal Usuario usuario,
    @RequestBody PerfilIdioma perfilIdioma) {
        return ResponseEntity.ok(
                perfilIdiomaService.update(
                        this.setDadosPerfilIdioma(perfilIdioma, usuario)
                )
        );
    }

    @DeleteMapping("")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Usuario usuario,
    @RequestParam Idioma idioma) {
        Perfil perfil = this.findPerfil(usuario);

        if(perfil == null) {
            throw new GestaoCompetenciaException(PERFIL_NAO_CADASTRADO);
        } else {
            if(idioma == null) {
                throw new GestaoCompetenciaException("Idioma não informado!");
            } else {
                PerfilIdioma perfilIdioma = perfilIdiomaService.findByPerfilAndIdioma(perfil, idioma);

                if(perfilIdioma != null && perfilIdioma.getPerfil() != null && perfilIdioma.getPerfil().equals(perfil)) {
                    try {
                        perfilIdiomaService.delete(perfilIdioma);
                        return ResponseEntity.noContent().build();
                    } catch (ResourceNotFoundException e) {
                        return ResponseEntity.notFound().build();
                    }
                } else {
                    throw new GestaoCompetenciaException("Você não tem permissão para excluir o idioma!");
                }
            }
        }
    }

    private Perfil findPerfil(Usuario usuario) {
        return perfilService.findByUsuario(usuario);
    }

    private PerfilIdioma setDadosPerfilIdioma(PerfilIdioma perfilIdioma, Usuario usuario) {
        Perfil perfil = this.findPerfil(usuario);

        if(perfil == null) {
            throw new GestaoCompetenciaException(PERFIL_NAO_CADASTRADO);
        } else {
            Idioma idioma = perfilIdioma.getIdioma();
            if(idioma == null) {
                throw new GestaoCompetenciaException("Idioma necessário para o cadastro!");
            } else {
                PerfilIdioma perfilIdiomaSalvo =
                        perfilIdiomaService.findByPerfilAndIdioma(
                                perfil, idioma
                        );

                if(perfilIdiomaSalvo == null) {
                    PerfilIdiomaKey perfilIdiomaKey = new PerfilIdiomaKey();
                    perfilIdiomaKey.setPerfilId(perfil.getId());
                    perfilIdiomaKey.setIdiomaId(idioma.getId());

                    perfilIdioma.setPerfilIdiomaKey(perfilIdiomaKey);
                }

                perfilIdioma.setPerfil(perfil);
                return perfilIdioma;
            }
        }
    }
}
