package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.service.ItemPreferenciaService;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilPreferenciaService;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilService;
import br.ufc.quixada.npi.gestaocompetencia.service.PreferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("perfil-preferencia")
public class PerfilPreferenciaControllerImpl {

    @Autowired
    private PerfilPreferenciaService perfilPreferenciaService;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private PreferenciaService preferenciaService;

    @Autowired
    private ItemPreferenciaService itemPreferenciaService;

    @GetMapping("")
    public ResponseEntity<List<PerfilPreferencia>> getOrCreate(@AuthenticationPrincipal Usuario usuario) {
        Perfil perfil = this.findPerfil(usuario);
        List<PerfilPreferencia> perfilPreferencias = new ArrayList<>(perfilPreferenciaService.findByPerfil(perfil));

        if(perfilPreferencias.isEmpty()) {
            List<Preferencia> preferencias = new ArrayList<>(preferenciaService.findAll());

            if(!preferencias.isEmpty()) {
                for(Preferencia preferencia : preferencias) {
                    PerfilPreferencia perfilPreferencia = new PerfilPreferencia();

                    PerfilPreferenciaKey perfilPreferenciaKey = new PerfilPreferenciaKey();
                    perfilPreferenciaKey.setPerfilId(perfil.getId());
                    perfilPreferenciaKey.setPreferenciaId(preferencia.getId());

                    perfilPreferencia.setPerfilPreferenciaKey(perfilPreferenciaKey);
                    perfilPreferencia.setPerfil(perfil);
                    perfilPreferencia.setPreferencia(preferencia);
                    perfilPreferencia.setItemPreferencia(null);

                    perfilPreferencias.add(perfilPreferenciaService.create(perfilPreferencia));
                }
            }
        }

        return ResponseEntity.ok(perfilPreferencias);
    }

    @PutMapping("")
    public ResponseEntity<PerfilPreferencia> update(@AuthenticationPrincipal Usuario usuario,
    @RequestBody PerfilPreferencia perfilPreferencia) {
        if(perfilPreferencia != null) {
            Perfil perfil = this.findPerfil(usuario);

            if(perfil == null) {
                throw new GestaoCompetenciaException("Perfil não cadastrado!");
            } else {
                Preferencia preferencia = perfilPreferencia.getPreferencia();

                if(preferencia == null) {
                    throw new GestaoCompetenciaException("Preferência necessária para o cadastro!");
                } else {
                    ItemPreferencia itemPreferencia = perfilPreferencia.getItemPreferencia();

                    if(itemPreferencia != null && validItemPreferencia(preferencia, itemPreferencia)) {
                        PerfilPreferenciaKey perfilPreferenciaKey = new PerfilPreferenciaKey();
                        perfilPreferenciaKey.setPerfilId(perfil.getId());
                        perfilPreferenciaKey.setPreferenciaId(preferencia.getId());
                        perfilPreferencia.setPerfilPreferenciaKey(perfilPreferenciaKey);

                        perfilPreferencia.setPerfil(perfil);
                        return ResponseEntity.ok(perfilPreferenciaService.update(perfilPreferencia));
                    } else {
                        throw new GestaoCompetenciaException("O item selecionado não corresponde a preferência");
                    }
                }
            }
        } else {
            throw new GestaoCompetenciaException("Preferência necessária para o cadastro!");
        }
    }

    /*@PostMapping("")
    public ResponseEntity<List<PerfilPreferencia>> create(@AuthenticationPrincipal Usuario usuario,
    @RequestBody PerfilPreferencia[] perfilPreferencias) {
        if(perfilPreferencias != null) {
            Perfil perfil = this.findPerfil(usuario);

            if(perfil == null) {
                throw new GestaoCompetenciaException("Perfil não cadastrado!");
            } else {
                List<PerfilPreferencia> perfilPreferenciasRetorno = new ArrayList<>();

                for(PerfilPreferencia p : perfilPreferencias) {
                    Preferencia preferencia = p.getPreferencia();
                    List<ItemPreferencia> itensPreferencia = itemPreferenciaService.findByPreferencia(preferencia);

                    if(itensPreferencia.contains(p.getItemPreferencia())) {
                        perfilPreferenciasRetorno.add(
                            perfilPreferenciaService.create(this.setDadosPerfilPreferencia(p, usuario))
                        );
                    }
                }

                return ResponseEntity.ok(perfilPreferenciasRetorno);
            }
        } else {
            throw new GestaoCompetenciaException("Preferências necessárias para o cadastro!");
        }
    }*/

    private Perfil findPerfil(Usuario usuario) {
        return perfilService.findByUsuario(usuario);
    }

    private boolean validItemPreferencia(Preferencia preferencia, ItemPreferencia itemPreferencia) {
        List<ItemPreferencia> itensPreferencia = itemPreferenciaService.findByPreferencia(preferencia);

        for(ItemPreferencia i : itensPreferencia) {
            if(i.equals(itemPreferencia)) {
                return true;
            }
        }

        return false;
    }

    /*private PerfilPreferencia setDadosPerfilPreferencia(PerfilPreferencia perfilPreferencia,
    Usuario usuario) {
        Perfil perfil = this.findPerfil(usuario);

        if(perfil == null) {
            throw new GestaoCompetenciaException("Perfil não cadastrado!");
        } else {
            Preferencia preferencia = perfilPreferencia.getPreferencia();

            if(preferencia == null) {
                throw new GestaoCompetenciaException("Preferência necessária para o cadastro!");
            } else {
                PerfilPreferencia perfilPreferenciaSalva =
                    perfilPreferenciaService.findByPerfilAndPreferencia(perfil, preferencia);

                if(perfilPreferenciaSalva == null) {
                    PerfilPreferenciaKey perfilPreferenciaKey = new PerfilPreferenciaKey();
                    perfilPreferenciaKey.setPerfilId(perfil.getId());
                    perfilPreferenciaKey.setPreferenciaId(preferencia.getId());

                    perfilPreferencia.setPerfilPreferenciaKey(perfilPreferenciaKey);
                }

                perfilPreferencia.setPerfil(perfil);
                return perfilPreferencia;
            }
        }
    }*/
}
