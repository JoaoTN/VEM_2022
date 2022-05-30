package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.CursoCapacitacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;

import java.util.List;

public interface CursoCapacitacaoService {

    CursoCapacitacao create(CursoCapacitacao cursoCapacitacao);

    List<CursoCapacitacao> findAll();

    List<CursoCapacitacao> findByPerfil(Perfil perfil);

    CursoCapacitacao update(CursoCapacitacao cursoCapacitacao);

    void delete(CursoCapacitacao cursoCapacitacao);
}
