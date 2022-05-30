package br.ufc.quixada.npi.gestaocompetencia.controller;

import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

@Api(tags = {"Usuario"})
public interface UsuarioController {
    @ApiOperation(
            value = "Retorna o usário a partir do id informado",
            authorizations = {@Authorization(value = "Token Authorization")}
    )
    ResponseEntity<Usuario> find(Usuario usuario);
}
