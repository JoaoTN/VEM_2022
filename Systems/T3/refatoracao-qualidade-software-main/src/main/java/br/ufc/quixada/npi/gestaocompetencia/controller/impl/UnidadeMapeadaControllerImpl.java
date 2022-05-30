package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.NotAllowedException;
import br.ufc.quixada.npi.gestaocompetencia.model.UnidadeMapeada;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.ComissaoService;
import br.ufc.quixada.npi.gestaocompetencia.service.impl.UnidadeMapeadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("unidades-mapeadas")
public class UnidadeMapeadaControllerImpl {
    @Autowired
    private UnidadeMapeadaService unidadeMapeadaService;

    @Autowired
    private ComissaoService comissaoService;

    @PostMapping("")
    public ResponseEntity<UnidadeMapeada> create(@AuthenticationPrincipal Usuario usuario, @RequestBody UnidadeMapeada unidadeMapeada) {
        if(comissaoService.isMembroComissao(usuario)) {
            return ResponseEntity.ok(unidadeMapeadaService.create(unidadeMapeada));
        } else {
            throw new NotAllowedException("Sem permissão para realizar a operação");
        }
    }
}
