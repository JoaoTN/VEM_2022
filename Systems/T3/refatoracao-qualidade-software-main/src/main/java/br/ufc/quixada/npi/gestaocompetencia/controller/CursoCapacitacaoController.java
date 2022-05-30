package br.ufc.quixada.npi.gestaocompetencia.controller;

import br.ufc.quixada.npi.gestaocompetencia.model.CursoCapacitacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = {"CursoCapacitacao"})
public interface CursoCapacitacaoController {
    @ApiOperation(
        value = "Retorna todos os cursos do perfil",
        authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<List<CursoCapacitacao>> findAll(Usuario usuario);

	ResponseEntity<CursoCapacitacao> create(Usuario usuario, CursoCapacitacao curso);

	ResponseEntity<CursoCapacitacao> update(Usuario usuario, CursoCapacitacao curso);

	ResponseEntity<String> delete(Usuario usuario, int id_curso, Perfil perfil);
}
