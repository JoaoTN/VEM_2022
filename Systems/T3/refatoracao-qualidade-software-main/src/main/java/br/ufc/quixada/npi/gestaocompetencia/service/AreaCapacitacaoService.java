package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.AreaCapacitacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Competencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

import java.util.List;

public interface AreaCapacitacaoService {

    AreaCapacitacao create(AreaCapacitacao areaCapacitacao);

    List<AreaCapacitacao> findAll();

    List<AreaCapacitacao> findByUsuario(Usuario usuario);

    AreaCapacitacao findByUsuarioAndCompetencia(Usuario usuario, Competencia competencia);

    List<AreaCapacitacao> findByCompetencia(Competencia competencia);

    AreaCapacitacao update(AreaCapacitacao areaCapacitacao);

    void delete(AreaCapacitacao areaCapacitacao);
}
