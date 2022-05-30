package br.ufc.quixada.npi.gestaocompetencia.controller.impl;
import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.model.*;

import br.ufc.quixada.npi.gestaocompetencia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("perfil")
public class PerfilControllerImpl {

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private AreaCapacitacaoService areaCapacitacaoService;

    @GetMapping("")
    public ResponseEntity<Perfil> getOrCreate(@AuthenticationPrincipal Usuario usuario) {
        Perfil perfil = perfilService.findByUsuario(usuario);
        if(perfil == null) {
            this.create(usuario);
            return ResponseEntity.ok(perfilService.findByUsuario(usuario));
        } else {
            return ResponseEntity.ok(perfil);
        }
    }

    @PostMapping("")
    public ResponseEntity<Perfil> create(@AuthenticationPrincipal Usuario usuario) {
        Perfil novoPerfil = new Perfil();
        novoPerfil.setUsuario(usuario);
        return ResponseEntity.ok(perfilService.create(novoPerfil));
    }
    
    private boolean verificarPerfilValido(Perfil perfil, Perfil perfilSalvo) {
    	return perfil != null && perfil.getId() != null && perfilSalvo != null
    	        && perfil.getId().equals(perfilSalvo.getId());
    }
    
    private void adicionarNovaAreaCapacitacaoAoPerfil(AreaCapacitacao areaCapacitacao, Usuario usuario) {
    	areaCapacitacao.setNome(areaCapacitacao.getCompetencia().getNome());
        areaCapacitacao.setUsuario(usuario);
        areaCapacitacao = areaCapacitacaoService.create(areaCapacitacao);
    }
    
    private void modificarAreaCapacitacaoExistenteNoPerfil(AreaCapacitacao areaCapacitacao, AreaCapacitacao areaCapacitacaoSalva) {
    	areaCapacitacao.setId(areaCapacitacaoSalva.getId());
        areaCapacitacao.setNome(areaCapacitacaoSalva.getCompetencia().getNome());
        areaCapacitacao = areaCapacitacaoService.update(areaCapacitacao);
    }
    
    private void atualizarAreaCapacitacao(Usuario usuario, AreaCapacitacao areaCapacitacao) {
    	 if(areaCapacitacao.getCompetencia() != null) {
             AreaCapacitacao areaCapacitacaoSalva = areaCapacitacaoService.findByUsuarioAndCompetencia(
                 usuario, areaCapacitacao.getCompetencia()
             );

             if(areaCapacitacaoSalva == null) {
                 this.adicionarNovaAreaCapacitacaoAoPerfil(areaCapacitacao, usuario);
             } else {
                 this.modificarAreaCapacitacaoExistenteNoPerfil(areaCapacitacao, areaCapacitacaoSalva);
             }
         }
    }

    @PutMapping("")
    public ResponseEntity<Perfil> update(@AuthenticationPrincipal Usuario usuario, @RequestBody Perfil perfil) {
        Perfil perfilSalvo = perfilService.findByUsuario(usuario);
        boolean perfilValido = verificarPerfilValido(perfil, perfilSalvo);
        
        if(perfilValido) {

            for(AreaCapacitacao areaCapacitacao : perfil.getAreasCapacitacaoInstrutor()) {
            	atualizarAreaCapacitacao(usuario, areaCapacitacao);
            }

            perfil.setUsuario(perfilSalvo.getUsuario());
            return ResponseEntity.ok(perfilService.update(perfil));
        } else {
            throw new GestaoCompetenciaException("Perfil necessário para realizar a operação");
        }
    }
}
