package br.ufc.quixada.npi.gestaocompetencia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import br.ufc.quixada.npi.gestaocompetencia.model.Comportamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.CategoriaComportamento;

public interface ComportamentoService {

	Comportamento save(Comportamento comportamento);
	
	List<Comportamento> findByCategoria(Usuario usuario, Mapeamento mapeamento, CategoriaComportamento categoria);
	
	Optional<Comportamento> findById(Long id);
	
	Optional<Comportamento> update(Comportamento comportamento);
	
	List<Comportamento> search(Specification<Comportamento> specification);
	
	Optional<Comportamento> deleteByComissao(Comportamento comportamento);

	void delete(Comportamento comportamento);
	
	Integer countByServidorAndMapeamento(Usuario servidor, Mapeamento mapeamento);

	List<Comportamento> findConsolidadosByMapeamento(Mapeamento mapeamento);
        
        List<Comportamento> findByMapeamento(Mapeamento mapeamento);
}
