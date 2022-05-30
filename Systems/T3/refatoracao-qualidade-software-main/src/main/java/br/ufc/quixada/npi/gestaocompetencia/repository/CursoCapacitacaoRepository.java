package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import br.ufc.quixada.npi.gestaocompetencia.model.CursoCapacitacao;

import java.util.List;

public interface CursoCapacitacaoRepository extends JpaRepository<CursoCapacitacao, Integer> {

    List<CursoCapacitacao> findAllByPerfil(Perfil perfil);
}

 