package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.repository.CursoCapacitacaoRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.CursoCapacitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoCapacitacaoServiceImpl implements CursoCapacitacaoService {

	@Autowired
    private CursoCapacitacaoRepository cursoCapacitacaoRepository;

	@Override
	public CursoCapacitacao create(CursoCapacitacao cursoCapacitacao) {
		return cursoCapacitacaoRepository.save(cursoCapacitacao);
	}

	@Override
	public List<CursoCapacitacao> findAll() {
		return cursoCapacitacaoRepository.findAll();
	}

	@Override
	public List<CursoCapacitacao> findByPerfil(Perfil perfil) {
		return cursoCapacitacaoRepository.findAllByPerfil(perfil);
	}

	@Override
	public CursoCapacitacao update(CursoCapacitacao cursoCapacitacao) {
		return cursoCapacitacaoRepository.save(cursoCapacitacao);
	}

	@Override
	public void delete(CursoCapacitacao cursoCapacitacao) {
		cursoCapacitacaoRepository.delete(cursoCapacitacao);
	}
}
