package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufc.quixada.npi.gestaocompetencia.model.Comportamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.CategoriaComportamento;
import br.ufc.quixada.npi.gestaocompetencia.repository.ComportamentoRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.ComportamentoService;

@Service
@Transactional
public class ComportamentoServiceImpl implements ComportamentoService{

	@Autowired
	private ComportamentoRepository comportamentoRepository;
	
	@Override
	public Comportamento save(Comportamento comportamento) {
		return comportamentoRepository.save(comportamento);
	}

	@Override
	public List<Comportamento> findByCategoria(Usuario servidor, Mapeamento mapeamento,
			CategoriaComportamento categoria) {
		return comportamentoRepository.findByServidorAndMapeamentoAndCategoria
				(servidor, mapeamento, categoria);
	}
	
	@Override
	public Optional<Comportamento> findById(Long id) {
		return comportamentoRepository.findById(id);
	}
	
	@Override
	public Optional<Comportamento> update(Comportamento comportamento) {
		return Optional.of(comportamentoRepository.save(comportamento));
	}
	
	@Override
	public Optional<Comportamento> deleteByComissao(Comportamento comportamento) {
		comportamento.setExcluido(true);
		return Optional.of(comportamentoRepository.save(comportamento));
	}

	@Override
	public void delete(Comportamento comportamento) {
		comportamentoRepository.delete(comportamento);
	}

	@Override
	public List<Comportamento> search(Specification<Comportamento> specification) {
		return comportamentoRepository.findAll(specification);
	}

	@Override
	public Integer countByServidorAndMapeamento(Usuario servidor, Mapeamento mapeamento) {
		return comportamentoRepository.countByServidorAndMapeamentoAndExcluidoFalse(servidor, mapeamento);
	}

	@Override
	public List<Comportamento> findConsolidadosByMapeamento(Mapeamento mapeamento) {
		return comportamentoRepository.findByMapeamentoAndConsolidadoTrueOrderByDescricaoAtualizada(mapeamento);
	}
        
        @Override
	public List<Comportamento> findByMapeamento(Mapeamento mapeamento) {
		return comportamentoRepository.findByMapeamento(mapeamento);
	}

}
