package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.controller.UsuarioController;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuarios")
public class UsuarioControllerImpl implements UsuarioController {
    @Override
    @GetMapping("/usuario")
    public ResponseEntity<Usuario> find(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(usuario);
    }
}
