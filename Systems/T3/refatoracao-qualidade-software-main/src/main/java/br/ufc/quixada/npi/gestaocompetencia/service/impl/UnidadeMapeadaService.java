package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.UnidadeMapeada;
import br.ufc.quixada.npi.gestaocompetencia.repository.UnidadeMapeadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnidadeMapeadaService {

	@Autowired
	private UnidadeMapeadaRepository unidadeMapeadaRepository;

    public UnidadeMapeada create(UnidadeMapeada unidadeMapeada) {
        return  unidadeMapeadaRepository.save(unidadeMapeada);
    }

    public void delete(UnidadeMapeada unidadeMapeada) {
        unidadeMapeadaRepository.delete(unidadeMapeada);
    }
	
	public UnidadeMapeada findByMapeamento(Mapeamento mapeamento) {
		return unidadeMapeadaRepository.findByMapeamento(mapeamento);
	}

}
