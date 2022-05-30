package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.time.LocalDate;
import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufc.quixada.npi.gestaocompetencia.service.CursoCapacitacaoService;

@RestController
@RequestMapping("curso-capacitacao")
public class CursoCapacitacaoControllerImpl {

    @Autowired
    private CursoCapacitacaoService cursoCapacitacaoService;

    @Autowired
    private PerfilService perfilService;

    private static final String PERFIL_NAO_CADASTRADO = "Perfil não cadastrado!";

    @GetMapping("")
    public ResponseEntity<List<CursoCapacitacao>> findAll(@AuthenticationPrincipal Usuario usuario) {
        Perfil perfil = this.findPerfil(usuario);

        if(perfil == null) {
            throw new GestaoCompetenciaException(PERFIL_NAO_CADASTRADO);
        } else {
            return ResponseEntity.ok(cursoCapacitacaoService.findByPerfil(perfil));
        }
    }

    @PostMapping("")
    public ResponseEntity<CursoCapacitacao> create(@AuthenticationPrincipal Usuario usuario,
    @RequestBody CursoCapacitacao cursoCapacitacao){
        return this.getCursoCapacitacaoResponseEntity(usuario, cursoCapacitacao, "create");
    }

    @PutMapping("")
    public ResponseEntity<CursoCapacitacao> update(@AuthenticationPrincipal Usuario usuario,
    @RequestBody CursoCapacitacao cursoCapacitacao){
        return this.getCursoCapacitacaoResponseEntity(usuario, cursoCapacitacao, "update");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Usuario usuario,
    @PathVariable("id") CursoCapacitacao curso){
        Perfil perfil = this.findPerfil(usuario);
        if(curso != null && curso.getPerfil() != null && curso.getPerfil().equals(perfil)) {
            try {
                cursoCapacitacaoService.delete(curso);
                return ResponseEntity.noContent().build();
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            throw new GestaoCompetenciaException("Sem permissão para excluir o curso!");
        }
    }

    private ResponseEntity<CursoCapacitacao> getCursoCapacitacaoResponseEntity(
    @AuthenticationPrincipal Usuario usuario, @RequestBody CursoCapacitacao cursoCapacitacao,
    String tipoServico) {
        Perfil perfil = this.findPerfil(usuario);
        final String DADOS_INSUFICIENTES = tipoServico.equals("create") ?
            "Dados insuficientes para realizar o cadastro!" :
            "Dados insuficientes para realizar a alteração!";

        if(perfil !=  null) {
            if(this.validCursoCapacitacao(cursoCapacitacao)) {
                LocalDate inicio = cursoCapacitacao.getInicio();
                LocalDate termino = cursoCapacitacao.getTermino();

                if (this.validDatasCursoCapacitacao(inicio, termino)) {
                    cursoCapacitacao.setPerfil(perfil);

                    if(tipoServico.equals("create")) {
                        return ResponseEntity.ok(cursoCapacitacaoService.create(cursoCapacitacao));
                    } else {
                        return ResponseEntity.ok(cursoCapacitacaoService.update(cursoCapacitacao));
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

    private boolean validCursoCapacitacao(CursoCapacitacao cursoCapacitacao) {
        return cursoCapacitacao.getCargaHoraria() != null
            && cursoCapacitacao.getInstituicao()  != null
            && cursoCapacitacao.getNome() != null
            && cursoCapacitacao.getInicio() != null
            && cursoCapacitacao.getTermino() != null;
    }

    private boolean validDatasCursoCapacitacao(LocalDate inicio, LocalDate termino) {
        return inicio.isEqual(termino) || inicio.isBefore(termino);
    }

    private Perfil findPerfil(Usuario usuario) {
        return perfilService.findByUsuario(usuario);
    }
}
