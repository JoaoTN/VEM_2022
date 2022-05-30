package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.Etapa;
import br.ufc.quixada.npi.gestaocompetencia.repository.ComportamentoRepository;
import br.ufc.quixada.npi.gestaocompetencia.repository.UnidadeMapeadaRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.UnidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import br.ufc.quixada.npi.gestaocompetencia.repository.MapeamentoRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.MapeamentoService;

@Service
public class MapeamentoServiceImpl implements MapeamentoService {

    @Autowired
    private MapeamentoRepository mapeamentoRepository;

    @Autowired
    private ComportamentoRepository comportamentoRepository;

    @Autowired
    private UnidadeMapeadaRepository unidadeMapeadaRepository;

    @Autowired
    private UnidadeService unidadeService;

    @Override
    public Mapeamento findMapeamentoByUsuario(Usuario usuario) {
        return unidadeMapeadaRepository.findFirstMapeamentoByUnidade(usuario.getUnidade()).getMapeamento();
    }

    @Override
    public Mapeamento findLastMapeamento(Usuario usuario) {
        return this.findByUnidade(usuario.getUnidade());
    }

    @Override
    public List<Unidade> findAllUnidadesInNormalizacao(Etapa etapa) {
        if (etapa == Etapa.NORMALIZACAO_COMPORTAMENTOS) {
            return mapeamentoRepository.findAllUnidadesInPerComportComissao(LocalDate.now());
        } else if (etapa == Etapa.VALIDACAO_RESPONSABILIDADES) {
            return mapeamentoRepository.findAllUnidadesInPerRespChefiaSuperior(LocalDate.now());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Unidade> findAllSubunidadesInNormalizacao(Unidade unidade, Etapa etapa) {
        List<Unidade> response = new ArrayList<>();

        if (etapa == Etapa.NORMALIZACAO_COMPORTAMENTOS) {
            response = mapeamentoRepository.findAllSubunidadesInPerComportComissao(LocalDate.now(), unidade);
        } else if (etapa == Etapa.NORMALIZACAO_RESPONSABILIDADES) {
            response = mapeamentoRepository.findAllSubunidadesInPerRespCom(LocalDate.now(), unidade);
        } else if (etapa == Etapa.CONSOLIDACAO_RESPONSABILIDADES) {
            response = mapeamentoRepository.findAllSubunidadesInPerRespConsol(LocalDate.now(), unidade);
        }

        return response;
    }

    @Override
    public Mapeamento findByUnidade(Unidade unidade) {
        List<Mapeamento> result = mapeamentoRepository.findLast(PageRequest.of(0, 1), unidade);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<Mapeamento> findAllByUnidade(Unidade unidade) {
        return mapeamentoRepository.findAllByUnidade(unidade);
    }

    @Override
    public List<Mapeamento> findAllSecondary(Unidade unidade) {
        List<Mapeamento> mapeamentosUnidade = mapeamentoRepository.findAllByUnidade(unidade);
        List<Mapeamento> mapeamentosSecondary = new ArrayList<>();
        List<Unidade> subunidades = unidadeService.findByUnidadePai(unidade);

        for (Unidade subunidade : subunidades) {
            List<Mapeamento> mapeamentosSubunidade = mapeamentoRepository.findAllByUnidade(subunidade);

            for (Mapeamento mapeamento : mapeamentosSubunidade) {
                if(!mapeamentosUnidade.contains(mapeamento) && !mapeamentosSecondary.contains(mapeamento)) {
                    mapeamentosSecondary.add(mapeamento);
                }
            }
        }

        return mapeamentosSecondary;
    }

    @Override
    public boolean pertence(Unidade unidade, Mapeamento mapeamento) {
        return mapeamentoRepository.findByUnidadeAndMapeamento(unidade, mapeamento) != null;
    }

    @Override
    public List<Unidade> getUnidadesByMapeamento(Mapeamento mapeamento) {
        List<Unidade> unidades = mapeamentoRepository.findUnidades(mapeamento);
        unidades.addAll(mapeamentoRepository.findParentUnidades(mapeamento));
        return unidades;
    }

    @Override
    public Boolean verifyAccess(Mapeamento mapeamento, Unidade unidade) {
        List<Unidade> unidades = getUnidadesByMapeamento(mapeamento);

        return unidades.contains(unidade);
    }

    @Override
    public Boolean verifySecondaryAccess(Mapeamento mapeamento, Unidade unidade) {
        List<Unidade> unidades = getUnidadesByMapeamento(mapeamento);
        List<Unidade> subunidades = unidadeService.findByUnidadePai(unidade);

        for (Unidade subunidade : subunidades) {
            if(unidades.contains(subunidade)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Mapeamento> getFinalizados(Usuario usuario) {
        return mapeamentoRepository.getFinalizados();
    }

    @Override
    public Mapeamento create(Mapeamento mapeamento) {
        return mapeamentoRepository.save(mapeamento);
    }

    @Override
    public Mapeamento update(Mapeamento mapeamento) {
        List<UnidadeMapeada> uniadesMapeadas = mapeamentoRepository.findAllUnidadesMapeadasByMapeamento(mapeamento);
        uniadesMapeadas.forEach(item -> unidadeMapeadaRepository.delete(item));
        return mapeamentoRepository.save(mapeamento);
    }

    @Override
    public List<Mapeamento> findAll() {
        return mapeamentoRepository.findAll();
    }

    @Override
    public List<UnidadeMapeada> findAllUnidadesMapeadas() {
        return unidadeMapeadaRepository.findAll();
    }

    @Override
    public boolean isEmAndamento(Mapeamento mapeamento) {
        List<Comportamento> comportamentos = comportamentoRepository.findByMapeamento(mapeamento);
        return !comportamentos.isEmpty();
    }

    @Override
    public boolean delete(Mapeamento mapeamento) {
        if (!isEmAndamento(mapeamento)) {
            List<UnidadeMapeada> uniadesMapeadas = mapeamentoRepository.findAllUnidadesMapeadasByMapeamento(mapeamento);
            uniadesMapeadas.forEach(item -> unidadeMapeadaRepository.delete(item));
            mapeamentoRepository.delete(mapeamento);
            return true;
        } else {
            return false;
        }
    }
}
