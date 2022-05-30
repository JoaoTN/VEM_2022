package br.ufc.quixada.npi.gestaocompetencia.service;

import java.util.List;
import java.util.Optional;

import br.ufc.quixada.npi.gestaocompetencia.model.Competencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoCompetencia;

public interface CompetenciaService {
	
	List<Competencia> getCompetencias(Unidade unidade, TipoCompetencia tipo);
	List<Competencia> getComportamentosComportamentais(List<Competencia> competencias);
	Competencia save(Competencia competencia);
	Optional<Competencia> findById(Integer id);
}
