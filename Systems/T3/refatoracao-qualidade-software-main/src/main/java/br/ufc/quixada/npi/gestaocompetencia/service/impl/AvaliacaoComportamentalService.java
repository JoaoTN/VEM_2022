package br.ufc.quixada.npi.gestaocompetencia.service.impl;


import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.ItemAvaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.quixada.npi.gestaocompetencia.model.Avaliacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Comportamento;
import br.ufc.quixada.npi.gestaocompetencia.repository.AvalicaoComportamentalRepository;

@Service
public class AvaliacaoComportamentalService {
	
	@Autowired
	private AvalicaoComportamentalRepository repository;
	
	
	public ItemAvaliacao updateAvaliacao(ItemAvaliacao avaliacao) {
		return repository.save(avaliacao);
	}
	
	public ItemAvaliacao createAvaliacao(ItemAvaliacao avaliacao) {
		return repository.save(avaliacao);
	}
	
	public void deleteAvaliacao(ItemAvaliacao avaliacao) {
		repository.delete(avaliacao);
	}

	public ItemAvaliacao put(ItemAvaliacao atualizada) {
		return repository.save(atualizada);
	}
	
	public ItemAvaliacao findByAvaliacaoAndFator(Avaliacao avaliacao, Comportamento fator) {
		return repository.findByAvaliacaoAndFator(avaliacao,fator);
	}
	
	public List<ItemAvaliacao> findByAvaliacao(Avaliacao avaliacao) {
		return repository.findByAvaliacao(avaliacao);
	}

}
