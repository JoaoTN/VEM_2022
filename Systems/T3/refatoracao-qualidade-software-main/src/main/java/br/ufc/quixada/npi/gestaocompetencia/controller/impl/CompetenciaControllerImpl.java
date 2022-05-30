package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.util.Collection;

import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.ufc.quixada.npi.gestaocompetencia.model.Competencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoCompetencia;

@RestController
@RequestMapping("competencias")
public class CompetenciaControllerImpl {
	
	@Autowired
	private CompetenciaService competenciaService;
	
	@GetMapping({"/{unidade}"})
	public ResponseEntity<Collection<Competencia>> getCompetenciasResponsabilidade(@PathVariable Unidade unidade){
		return ResponseEntity.ok(competenciaService.getCompetencias(unidade,TipoCompetencia.TECNICA));
	}
	
	@GetMapping({"/comportamental"})
	public ResponseEntity<Collection<Competencia>> getCompetenciasComportamentais(@AuthenticationPrincipal Usuario usuario){
		return ResponseEntity.ok(competenciaService.getCompetencias(null, TipoCompetencia.COMPORTAMENTAL));
	}

    @GetMapping({"/tecnica"})
    public ResponseEntity<Collection<Competencia>> getCompetenciasTecnicas(@AuthenticationPrincipal Usuario usuario,
    @RequestParam(required = false) Unidade unidade){
        return ResponseEntity.ok(competenciaService.getCompetencias(unidade, TipoCompetencia.TECNICA));
    }
	
	@PostMapping({""})
	public ResponseEntity<Competencia> save(@AuthenticationPrincipal Usuario usuario, @RequestBody Competencia competencia){
        if(competencia == null) {
            throw new GestaoCompetenciaException("Competência necessária para o cadastrado!");
        } else {
            return ResponseEntity.ok(competenciaService.save(competencia));
        }
	}
}
