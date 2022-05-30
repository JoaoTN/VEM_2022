package br.ufc.quixada.npi.gestaocompetencia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Responsabilidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

public interface ResponsabilidadeService {

	Optional<Responsabilidade> findById(Integer id);

	List<Responsabilidade> findAll(Usuario usuario);

	List<Responsabilidade> findAll(Unidade unidade, Mapeamento mapeamento);

	List<Responsabilidade> findConsolidadas(Unidade unidade, Mapeamento mapeamento);

	Responsabilidade save(Responsabilidade responsabilidade, Usuario usuario);
	
	Responsabilidade update(Responsabilidade responsabilidade);
	
	void delete(Responsabilidade responsabilidade);
	
	List<Responsabilidade> search(Specification<Responsabilidade> specification);
}
