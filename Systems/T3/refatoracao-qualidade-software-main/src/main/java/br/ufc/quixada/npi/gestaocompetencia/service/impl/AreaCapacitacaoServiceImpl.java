package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.AreaCapacitacao;
import br.ufc.quixada.npi.gestaocompetencia.model.Competencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.repository.AreaCapacitacaoRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.AreaCapacitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaCapacitacaoServiceImpl implements AreaCapacitacaoService {

    @Autowired
    private AreaCapacitacaoRepository areaCapacitacaoRepository;

    @Override
    public AreaCapacitacao create(AreaCapacitacao areaCapacitacao) {
        return areaCapacitacaoRepository.save(areaCapacitacao);
    }

    @Override
    public List<AreaCapacitacao> findAll() {
        return areaCapacitacaoRepository.findAll();
    }

    @Override
    public List<AreaCapacitacao> findByUsuario(Usuario usuario) {
        return areaCapacitacaoRepository.findAllByUsuario(usuario);
    }

    @Override
    public AreaCapacitacao findByUsuarioAndCompetencia(Usuario usuario, Competencia competencia) {
        return areaCapacitacaoRepository.findByUsuarioAndCompetencia(usuario, competencia);
    }

    @Override
    public List<AreaCapacitacao> findByCompetencia(Competencia competencia) {
        return areaCapacitacaoRepository.findAllByCompetencia(competencia);
    }

    @Override
    public AreaCapacitacao update(AreaCapacitacao areaCapacitacao) {
        return areaCapacitacaoRepository.save(areaCapacitacao);
    }

    @Override
    public void delete(AreaCapacitacao areaCapacitacao) {
        areaCapacitacaoRepository.delete(areaCapacitacao);
    }
}
