package br.ufc.quixada.npi.gestaocompetencia.controller;

import br.ufc.quixada.npi.gestaocompetencia.model.Diagnostico;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = {"Diagnostico"})
public interface DiagnosticoController {
    @ApiOperation(
            value = "Retorna todos os diagnósticos caso o usuário seja da comissão",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<List<Diagnostico>> findAll(Usuario usuario);

    @ApiOperation(
            value = "Retorna o diagnostico mais recente do usuário logado",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<Diagnostico> findLastUser(Usuario usuario);

    @ApiOperation(
            value = "Retorna o diagnostico secundários do usuário logado",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<List<Diagnostico>> findSecondaryUser(Usuario usuario);

    @ApiOperation(
            value = "Verifica se o usuário logado tem acesso secundário ao diagnóstico informado",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<Diagnostico> verifySecondaryAccess(Diagnostico diagnostico, Usuario usuario);

    @ApiOperation(
            value = "Retorna todos os diagnósticos do usuário",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<List<Diagnostico>> findByUsuario(Usuario usuario);

    @ApiOperation(
        value = "Retorna o diagnóstico a partir do id informado",
        authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<Diagnostico> find(Diagnostico diagnostico, Usuario usuario);

    @ApiOperation(
        value = "Cadastra um novo diagnóstico",
        authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<Diagnostico> create(Diagnostico diagnostico, Usuario usuario);

    @ApiOperation(
            value = "Altera um diagnóstico já cadastrado",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<Diagnostico> update(Diagnostico diagnostico, Usuario usuario);

    @ApiOperation(
            value = "Exclui um diagnóstico já cadastrado",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<Void> delete(Diagnostico diagnostico, Usuario usuario);
}
