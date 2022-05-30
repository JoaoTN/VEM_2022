package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.time.LocalDate;
import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import br.ufc.quixada.npi.gestaocompetencia.model.NivelEscolaridade;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.NivelEscolaridadeService;


@Controller
@RequestMapping("nivel-escolaridade")
public class NivelEscolaridadeController {

    @Autowired
    private NivelEscolaridadeService nivelEscolaridadeSerivce;

    @Autowired
    private PerfilService perfilService;

    private static final String PERFIL_NAO_CADASTRADO = "Perfil não cadastrado!";

    @GetMapping("")
    public ResponseEntity<List<NivelEscolaridade>> findByFormacao(@AuthenticationPrincipal Usuario usuario) {
        Perfil perfil = this.findPerfil(usuario);

        if(perfil != null) {
            return ResponseEntity.ok(nivelEscolaridadeSerivce.findByPerfil(perfil));
        } else {
            throw new GestaoCompetenciaException(PERFIL_NAO_CADASTRADO);
        }
    }

    @PostMapping("")
    public ResponseEntity<NivelEscolaridade> create(@AuthenticationPrincipal Usuario usuario,
    @RequestBody NivelEscolaridade nivelEscolaridade){
        return this.getNivelEscolaridadeResponseEntity(usuario, nivelEscolaridade, "create");
    }

    @PutMapping("")
    public ResponseEntity<NivelEscolaridade> update(@AuthenticationPrincipal Usuario usuario,
    @RequestBody NivelEscolaridade nivelEscolaridade){
        return this.getNivelEscolaridadeResponseEntity(usuario, nivelEscolaridade, "update");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") NivelEscolaridade nivelEscolaridade) {
        Perfil perfil = this.findPerfil(usuario);

        if(nivelEscolaridade != null && nivelEscolaridade.getPerfil() != null
        && nivelEscolaridade.getPerfil().equals(perfil)) {
            try {
                nivelEscolaridadeSerivce.delete(nivelEscolaridade);
                return ResponseEntity.noContent().build();
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            throw new GestaoCompetenciaException("Sem permissão para excluir o nível de escolaridade!");
        }
    }

    private ResponseEntity<NivelEscolaridade> getNivelEscolaridadeResponseEntity(
    @AuthenticationPrincipal Usuario usuario, @RequestBody NivelEscolaridade nivelEscolaridade,
    String tipoServico) {
        Perfil perfil = this.findPerfil(usuario);
        final String DADOS_INSUFICIENTES = tipoServico.equals("create") ?
            "Dados insuficientes para realizar o cadastro!" :
            "Dados insuficientes para realizar a alteração!";

        if(perfil !=  null) {
            if(this.validNivelEscolaridade(nivelEscolaridade)) {
                LocalDate inicio = nivelEscolaridade.getInicio();
                LocalDate termino = nivelEscolaridade.getTermino();

                if(this.validDatasNivelEscolaridade(inicio, termino)) {
                    nivelEscolaridade.setPerfil(perfil);

                    if(tipoServico.equals("create")) {
                        return ResponseEntity.ok(nivelEscolaridadeSerivce.create(nivelEscolaridade));
                    } else {
                        return ResponseEntity.ok(nivelEscolaridadeSerivce.update(nivelEscolaridade));
                    }
                } else {
                    throw new GestaoCompetenciaException("As datas de início e término não são válidas!");
                }
            } else {
                throw new GestaoCompetenciaException(DADOS_INSUFICIENTES);
            }
        } else {
            throw new GestaoCompetenciaException(PERFIL_NAO_CADASTRADO);
        }

    }

    private boolean validNivelEscolaridade(NivelEscolaridade nivelEscolaridade) {
        return nivelEscolaridade.getEscolaridade() != null
            && nivelEscolaridade.getInstituicao() != null
            && nivelEscolaridade.getStatus() != null
            && nivelEscolaridade.getInicio() != null
            && nivelEscolaridade.getTermino() != null;
    }

    private boolean validDatasNivelEscolaridade(LocalDate inicio, LocalDate termino) {
        return inicio.isEqual(termino) || inicio.isBefore(termino);
    }

    private Perfil findPerfil(Usuario usuario) {
        return perfilService.findByUsuario(usuario);
    }
}












