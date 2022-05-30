package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.controller.DiagnosticoController;
import br.ufc.quixada.npi.gestaocompetencia.exception.NotAllowedException;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.Diagnostico;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.ComissaoService;
import br.ufc.quixada.npi.gestaocompetencia.service.DiagnosticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("diagnosticos")
public class DiagnosticoControllerImpl implements DiagnosticoController {

    @Autowired
    private DiagnosticoService diagnosticoService;

    @Autowired
    private ComissaoService comissaoService;

    @Override
    @GetMapping({"", "/"})
    public ResponseEntity<List<Diagnostico>> findAll(@AuthenticationPrincipal Usuario usuario) {
        List<Diagnostico> diagnosticos;
        if(comissaoService.isMembroComissao(usuario)) {
            diagnosticos = diagnosticoService.findAll();
        } else {
            throw new NotAllowedException("Sem permissão para visualizar os diagnósticos devido o usuário não fazer parte da comissão");
        }

        return ResponseEntity.ok(diagnosticos);
    }

    @Override
    @GetMapping("/last")
    public ResponseEntity<Diagnostico> findLastUser(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(diagnosticoService.findLast(usuario.getUnidade()));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Diagnostico>> findByUsuario(@AuthenticationPrincipal Usuario usuario) {
        List<Diagnostico> diagnosticos = diagnosticoService.findByUsuario(usuario);
        return ResponseEntity.ok(diagnosticos);
    }

    @Override
    @GetMapping("/secondary")
    public ResponseEntity<List<Diagnostico>> findSecondaryUser(@AuthenticationPrincipal Usuario usuario) {
        List<Diagnostico> diagnosticos = new ArrayList<>();
        return ResponseEntity.ok(diagnosticos);
    }

    @Override
    @GetMapping("/{diagnostico}/acesso")
    public ResponseEntity<Diagnostico> verifySecondaryAccess(@PathVariable Diagnostico diagnostico, @AuthenticationPrincipal Usuario usuario) {
        if(diagnosticoService.verifySecondaryAccess(diagnostico, usuario.getUnidade())) {
            return ResponseEntity.ok(diagnostico);
        } else {
            return null;
        }
    }

    @Override
    @GetMapping("/{diagnostico}")
    public ResponseEntity<Diagnostico> find(@PathVariable Diagnostico diagnostico, @AuthenticationPrincipal Usuario usuario) {
        if(diagnosticoService.verifyAccess(diagnostico, usuario.getUnidade())) {
            return ResponseEntity.ok(diagnostico);
        } else {
            return null;
        }
    }

    @Override
    @PostMapping("")
    public ResponseEntity<Diagnostico> create(@RequestBody Diagnostico diagnostico, @AuthenticationPrincipal Usuario usuario) {
        if(!comissaoService.isMembroComissao(usuario)) {
            throw new NotAllowedException("Sem permissão para cadastrar o diagnóstico");
        } else {
            return ResponseEntity.ok(diagnosticoService.create(diagnostico));
        }
    }

    @Override
    @PutMapping("")
	public ResponseEntity<Diagnostico> update(@RequestBody Diagnostico diagnostico, @AuthenticationPrincipal Usuario usuario){
        if(!comissaoService.isMembroComissao(usuario)) {
            throw new NotAllowedException("Sem permissão para alterar o diagnóstico");
        } else {
            return ResponseEntity.ok(diagnosticoService.update(diagnostico));
        }
	}

	@Override
    @DeleteMapping("/{diagnostico}")
	public ResponseEntity<Void> delete(@PathVariable Diagnostico diagnostico, @AuthenticationPrincipal Usuario usuario) {
        if(diagnostico == null) {
            throw new ResourceNotFoundException("Diagnóstico", "Código", null);
        }

        if(!comissaoService.isMembroComissao(usuario)) {
            throw new NotAllowedException("Sem permissão para excluir este diagnóstico");
        }

        try {
            diagnosticoService.delete(diagnostico);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
	}
}
