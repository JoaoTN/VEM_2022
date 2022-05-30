package br.ufc.quixada.npi.gestaocompetencia.service.impl;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Responsabilidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.repository.ResponsabilidadeRepository;
import br.ufc.quixada.npi.gestaocompetencia.repository.UnidadeMapeadaRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.ResponsabilidadeService;

@Service
public class ResponsabilidadeServiceImpl implements ResponsabilidadeService{

	@Autowired
	private ResponsabilidadeRepository responsabilidadeRepository;
	
	@Autowired
	private UnidadeMapeadaRepository unidadeMapeadaRepository;

	@Override
	@Transactional
	public Optional<Responsabilidade> findById(Integer id) {
		return responsabilidadeRepository.findById(id);
	}

	@Override
	@Transactional
	public Responsabilidade update(Responsabilidade responsabilidade) {
		return responsabilidadeRepository.save(responsabilidade);
	}

	@Override
	@Transactional
	public void delete(Responsabilidade responsabilidade) {
		responsabilidadeRepository.delete(responsabilidade);
	}

	@Override
	@Transactional
	public List<Responsabilidade> findAll(Usuario usuario) {
		return responsabilidadeRepository.findByUsuarioGestor(usuario);
	}

	@Override
	@Transactional
	public List<Responsabilidade> findAll(Unidade unidade, Mapeamento mapeamento) {
		return responsabilidadeRepository.findByUnidadeAndMapeamento(unidade, mapeamento);
	}

	@Override
	public List<Responsabilidade> findConsolidadas(Unidade unidade, Mapeamento mapeamento) {
		return responsabilidadeRepository.findByUnidadeAndMapeamentoAndConsolidadaTrue(unidade, mapeamento);
	}

	@Override
	@Transactional
	public Responsabilidade save(Responsabilidade responsabilidade, Usuario usuario) {
		responsabilidade.setUsuarioGestor(usuario);
		return responsabilidadeRepository.save(responsabilidade);
	}

	@Override
	@Transactional
	public List<Responsabilidade> search(Specification<Responsabilidade> specification) {
		return responsabilidadeRepository.findAll(specification);
	}
}
