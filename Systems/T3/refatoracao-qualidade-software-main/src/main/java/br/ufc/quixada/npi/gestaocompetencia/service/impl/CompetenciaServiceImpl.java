package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.quixada.npi.gestaocompetencia.model.Competencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoCompetencia;
import br.ufc.quixada.npi.gestaocompetencia.repository.CompetenciaRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.CompetenciaService;

@Service
public class CompetenciaServiceImpl implements CompetenciaService{

	@Autowired
	private CompetenciaRepository competenciaRepository;
	
	@Override
	public List<Competencia> getCompetencias(Unidade unidade, TipoCompetencia tipo) {
		if(unidade == null) {
			return competenciaRepository.findAllByTipoEqualsAndUnidadeIsNull(tipo);
		} else {
			return competenciaRepository.findByUnidadeAndTipo(unidade, tipo);
		}
	}

	@Override
	public List<Competencia> getComportamentosComportamentais(List<Competencia> competencias) {
		return competenciaRepository.getComportamentosComportamentais(competencias);
	}
	
	public Competencia save(Competencia competencia) {
		return competenciaRepository.save(competencia);
	}
	
	@Override
	public Optional<Competencia> findById(Integer id) {
		return competenciaRepository.findById(id);
	}

}
