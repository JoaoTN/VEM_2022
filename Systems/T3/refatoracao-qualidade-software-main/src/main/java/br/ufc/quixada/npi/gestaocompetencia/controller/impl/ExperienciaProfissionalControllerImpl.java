package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.ExperienciaProfissional;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.StatusExperienciaProfissional;
import br.ufc.quixada.npi.gestaocompetencia.service.ExperienciaProfissionalService;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("experiencia-profissional")
public class ExperienciaProfissionalControllerImpl {

    @Autowired
    private ExperienciaProfissionalService experienciaProfissionalService;

    @Autowired
    private PerfilService perfilService;

    private static final String PERFIL_NAO_CADASTRADO = "Perfil não cadastrado!";

    @GetMapping("")
    public ResponseEntity<List<ExperienciaProfissional>> findAll(@AuthenticationPrincipal Usuario usuario) {
        Perfil perfil = this.findPerfil(usuario);

        if(perfil !=  null) {
            return ResponseEntity.ok(
                experienciaProfissionalService.findByPerfil(perfil)
            );
        } else {
            throw new GestaoCompetenciaException(PERFIL_NAO_CADASTRADO);
        }
    }

    @PostMapping("")
    public ResponseEntity<ExperienciaProfissional> create(@AuthenticationPrincipal Usuario usuario,
    @RequestBody ExperienciaProfissional experienciaProfissional) {
        return getExperienciaProfissionalResponseEntity(usuario, experienciaProfissional, "create");
    }

    @PutMapping("")
    public ResponseEntity<ExperienciaProfissional> update(@AuthenticationPrincipal Usuario usuario,
    @RequestBody ExperienciaProfissional experienciaProfissional) {
        return getExperienciaProfissionalResponseEntity(usuario, experienciaProfissional, "update");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") ExperienciaProfissional experienciaProfissional) {
        Perfil perfil = this.findPerfil(usuario);
        if(experienciaProfissional != null && experienciaProfissional.getPerfil() != null
        && experienciaProfissional.getPerfil().equals(perfil)) {
            try {
                experienciaProfissionalService.delete(experienciaProfissional);
                return ResponseEntity.noContent().build();
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            throw new GestaoCompetenciaException("Sem permissão para excluir a experiência profissional!");
        }
    }

    private ResponseEntity<ExperienciaProfissional> getExperienciaProfissionalResponseEntity(
    @AuthenticationPrincipal Usuario usuario, @RequestBody ExperienciaProfissional experienciaProfissional,
    String tipoServico) {
        Perfil perfil = this.findPerfil(usuario);
        final String DADOS_INSUFICIENTES = tipoServico.equals("create") ?
                "Dados insuficientes para realizar o cadastro!" :
                "Dados insuficientes para realizar a alteração!";

        if(perfil !=  null) {
            if(this.validExperienciaProfissional(experienciaProfissional)) {
                LocalDate inicio = experienciaProfissional.getInicio();
                experienciaProfissional.setPerfil(perfil);

                if(StatusExperienciaProfissional.ATUAL.equals(experienciaProfissional.getStatus())) {
                    experienciaProfissional.setTermino(null);
                    return this.save(experienciaProfissional, tipoServico);
                } else {
                    LocalDate termino = experienciaProfissional.getTermino();

                    if(this.validDatasExperienciaProfissional(inicio, termino)) {
                        return this.save(experienciaProfissional, tipoServico);
                    } else {
                        throw new GestaoCompetenciaException("As datas de início e término não são válidas!");
                    }
                }
            } else {
                throw new GestaoCompetenciaException(DADOS_INSUFICIENTES);
            }
        } else {
            throw new GestaoCompetenciaException(PERFIL_NAO_CADASTRADO);
        }
    }

    private ResponseEntity<ExperienciaProfissional> save(ExperienciaProfissional experienciaProfissional,
    String tipoServico) {
        if(tipoServico.equals("create")) {
            return ResponseEntity.ok(experienciaProfissionalService.create(experienciaProfissional));
        } else {
            return ResponseEntity.ok(experienciaProfissionalService.update(experienciaProfissional));
        }
    }

    private boolean validExperienciaProfissional(ExperienciaProfissional experienciaProfissional) {
        return experienciaProfissional.getTipo() != null &&
            experienciaProfissional.getCargo() != null &&
            experienciaProfissional.getInstituicao() != null &&
            experienciaProfissional.getInicio() != null;
    }

    private boolean validDatasExperienciaProfissional(LocalDate inicio, LocalDate termino) {
        return inicio.isEqual(termino) || inicio.isBefore(termino);
    }

    private Perfil findPerfil(Usuario usuario) {
        return perfilService.findByUsuario(usuario);
    }
}
