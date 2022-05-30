package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.repository.UnidadeRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.UnidadeService;

@Service
public class UnidadeServiceImpl implements UnidadeService{

	@Autowired
	private UnidadeRepository unidadeRepository;
	
	@Override
	public List<Unidade> findAll() {
		return unidadeRepository.findAll();
	}

	@Override
	public List<Unidade> findAllUnidadesByGestor(Usuario usuario) {
		return unidadeRepository.findAllUnidadesByChefe(usuario);
	}

	@Override
	public List<Unidade> findAllSubunidadesByUnidade(Unidade unidade) {
		return unidadeRepository.findByUnidadePai(unidade);
	}
	
	@Override
	public List<Unidade> findAllWithMapeamento() {
		List<Unidade> unidades = unidadeRepository.findAllWithMapeamento();
		unidades.addAll(unidadeRepository.findAllFromMapeamento());
		return unidades;
	}

	@Override
	public List<Unidade> findByUnidadePai(Unidade unidade) {
		return unidadeRepository.findByUnidadePai(unidade);
	}

	@Override
	public boolean hasPermissionRead(Unidade unidade, Usuario usuario) {
		Unidade unidadeAux = unidade;

		while(unidadeAux != null) {
			if (unidadeAux.isChefe(usuario) || unidadeAux.isViceChefe(usuario)) {
				return true;
			} else {
				unidadeAux = unidadeAux.getUnidadePai();
			}
		}

		return false;
	}
}