package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.util.List;
import br.ufc.quixada.npi.gestaocompetencia.model.UnidadeMapeada;
import br.ufc.quixada.npi.gestaocompetencia.service.ComissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.Etapa;
import br.ufc.quixada.npi.gestaocompetencia.service.MapeamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("mapeamento")
public class MapeamentoControllerImpl {

    @Autowired
    private MapeamentoService mapeamentoService;

    @Autowired
    private ComissaoService comissaoService;

    @GetMapping("")
    public ResponseEntity<List<Mapeamento>> findAll(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(mapeamentoService.findAll());
    }

    @GetMapping("/last")
    public ResponseEntity<Mapeamento> findLastUser(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(mapeamentoService.findLastMapeamento(usuario));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Mapeamento>> findAllUser(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(mapeamentoService.findAllByUnidade(usuario.getUnidade()));
    }

    @GetMapping("/secondary")
    public ResponseEntity<List<Mapeamento>> findSecondaryUser(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(mapeamentoService.findAllSecondary(usuario.getUnidade()));
    }

    @GetMapping("/{mapeamento}")
    public ResponseEntity<Mapeamento> find(@PathVariable Mapeamento mapeamento, @AuthenticationPrincipal Usuario usuario) {
        if(mapeamentoService.verifyAccess(mapeamento, usuario.getUnidade()) || comissaoService.isMembroComissao(usuario)) {
            return ResponseEntity.ok(mapeamento);
        } else {
            return null;
        }
    }

    @GetMapping("/{mapeamento}/acesso")
    public ResponseEntity<Mapeamento> verifySecondaryAccess(@PathVariable Mapeamento mapeamento, @AuthenticationPrincipal Usuario usuario) {
        if (mapeamentoService.verifySecondaryAccess(mapeamento, usuario.getUnidade())) {
            return ResponseEntity.ok(mapeamento);
        } else {
            return null;
        }
    }

    @GetMapping("/{mapeamento}/status")
    public ResponseEntity<Boolean> getStatus(@PathVariable Mapeamento mapeamento) {
        return ResponseEntity.ok(mapeamentoService.isEmAndamento(mapeamento));
    }

    @GetMapping("/unidadesmapeadas/all")
    public ResponseEntity<List<UnidadeMapeada>> findAllUnidadesMapeadas() {
        return ResponseEntity.ok(mapeamentoService.findAllUnidadesMapeadas());
    }

    @GetMapping("/{mapeamento}/unidades")
    public ResponseEntity<List<Unidade>> findUnidadesMapeadas(@PathVariable Mapeamento mapeamento) {
        if (mapeamento != null) {
            return ResponseEntity.ok(mapeamentoService.getUnidadesByMapeamento(mapeamento));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/normalizacao/{etapa}/unidades")
    public ResponseEntity<List<Unidade>> unidadesInNormalizacao(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Etapa etapa){
        return ResponseEntity.ok(mapeamentoService.findAllUnidadesInNormalizacao(etapa));
    }

    @GetMapping("/normalizacao/{unidade}/{etapa}/subunidades")
    public ResponseEntity<List<Unidade>> subnidadesInNormalizacao(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Unidade unidade,
            @PathVariable Etapa etapa ){
        return ResponseEntity.ok(mapeamentoService.findAllSubunidadesInNormalizacao(unidade, etapa));
    }

    @PostMapping("")
    public ResponseEntity<Mapeamento> create(@AuthenticationPrincipal Usuario usuario, @RequestBody Mapeamento mapeamento){
        return ResponseEntity.ok(mapeamentoService.create(mapeamento));
    }

    @PutMapping("")
    public ResponseEntity<Mapeamento> update(@RequestBody Mapeamento mapeamento){
        return ResponseEntity.ok(mapeamentoService.update(mapeamento));
    }

    @DeleteMapping("/{mapeamento}")
    public ResponseEntity delete(@PathVariable Mapeamento mapeamento){
        if (mapeamentoService.delete(mapeamento)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @GetMapping("/finalizados")
    public ResponseEntity<List<Mapeamento>> getFinalizados(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(mapeamentoService.getFinalizados(usuario));
    }
}