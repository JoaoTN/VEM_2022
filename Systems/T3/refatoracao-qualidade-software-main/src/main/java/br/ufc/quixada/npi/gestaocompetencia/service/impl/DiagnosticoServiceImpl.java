package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.NotAllowedException;
import br.ufc.quixada.npi.gestaocompetencia.model.*;
import br.ufc.quixada.npi.gestaocompetencia.repository.DiagnosticoRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.DiagnosticoService;
import br.ufc.quixada.npi.gestaocompetencia.service.MapeamentoService;
import br.ufc.quixada.npi.gestaocompetencia.service.UnidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoService {
    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Autowired
    private MapeamentoService mapeamentoService;

    @Autowired
    private UnidadeService unidadeService;

    @Override
    public Diagnostico findLast(Unidade unidade) {
        List<Diagnostico> result = diagnosticoRepository.findLast(PageRequest.of(0, 1), unidade);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<Diagnostico> findAll() {
        return diagnosticoRepository.findAll();
    }

    @Override
    public List<Diagnostico> findByUsuario(Usuario usuario) {
        return diagnosticoRepository.findAllByUnidade(usuario.getUnidade());
    }

    @Override
    public List<Diagnostico> findbyUnidade(Usuario usuario) {
        return diagnosticoRepository.findAllByUnidade(usuario.getUnidade());
    }

    @Override
    public List<Diagnostico> findAllSecondary(Unidade unidade) {
        List<Diagnostico> diagnosticosUnidade = diagnosticoRepository.findAllByUnidade(unidade);
        List<Diagnostico> diagnosticosSecondary = new ArrayList<>();
        List<Unidade> subunidades = unidadeService.findByUnidadePai(unidade);

        for (Unidade subunidade : subunidades) {
            List<Diagnostico> diagnosticosSubunidade = diagnosticoRepository.findAllByUnidade(subunidade);

            for (Diagnostico diagnostico : diagnosticosSubunidade) {
                if(!diagnosticosUnidade.contains(diagnostico) && !diagnosticosSecondary.contains(diagnostico)) {
                    diagnosticosSecondary.add(diagnostico);
                }
            }
        }

        return diagnosticosSecondary;
    }

    @Override
    public Boolean verifyAccess(Diagnostico diagnostico, Unidade unidade) {
        List<Unidade> unidades = mapeamentoService.getUnidadesByMapeamento(diagnostico.getMapeamento());

        return unidades.contains(unidade);
    }

    @Override
    public Boolean verifySecondaryAccess(Diagnostico diagnostico, Unidade unidade) {
        List<Unidade> unidades = mapeamentoService.getUnidadesByMapeamento(diagnostico.getMapeamento());
        List<Unidade> subunidades = unidadeService.findByUnidadePai(unidade);

        for (Unidade subunidade : subunidades) {
            if(unidades.contains(subunidade)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Diagnostico create(Diagnostico diagnostico) {
        if(!validarDatas(diagnostico) || !validarPrazos(diagnostico)) {
            throw new NotAllowedException("Não foi possível cadastrar o diagnóstico porque as datas informadas estão inconsistentes");
        } else {
            diagnostico.setCriadoEm(LocalDate.now());
            return diagnosticoRepository.save(diagnostico);
        }
    }

    @Override
    public Diagnostico update(Diagnostico diagnostico) {
        if(!validarPrazos(diagnostico)) {
            throw new NotAllowedException("Não foi possível alterar o diagnóstico porque as datas informadas estão inconsistentes");
        } else {
            return diagnosticoRepository.save(diagnostico);
        }
    }

    @Override
    public void delete(Diagnostico diagnostico) {
        diagnosticoRepository.delete(diagnostico);
    }

    @Override
    public Boolean validarDatas(Diagnostico diagnostico){
        ArrayList<LocalDate> datas = new ArrayList<>();

        datas.add(diagnostico.getInicioComportamental());
        datas.add(diagnostico.getFimComportamental());
        datas.add(diagnostico.getInicioResponsabilidadeChefia());
        datas.add(diagnostico.getFimResponsabilidadeChefia());
        datas.add(diagnostico.getInicioResponsabilidadeServidor());
        datas.add(diagnostico.getFimResponsabilidadeServidor());

        for(LocalDate data: datas){
            if(data.isBefore(LocalDate.now())){
                return false;
            }
        }

        return true;
    }

    @Override
    public Boolean validarPrazos(Diagnostico diagnostico){
        return (
            diagnostico.getFimComportamental().isAfter(diagnostico.getInicioComportamental()) ||
            diagnostico.getFimComportamental().isEqual(diagnostico.getInicioComportamental())
        ) &&
        (
            diagnostico.getFimResponsabilidadeChefia().isAfter(diagnostico.getInicioResponsabilidadeChefia()) ||
            diagnostico.getFimResponsabilidadeChefia().isEqual(diagnostico.getInicioResponsabilidadeChefia())
        ) &&
        (
            diagnostico.getFimResponsabilidadeServidor().isAfter(diagnostico.getInicioResponsabilidadeServidor()) ||
            diagnostico.getFimResponsabilidadeServidor().isEqual(diagnostico.getInicioResponsabilidadeServidor())
        ) &&
        (
            diagnostico.getInicioResponsabilidadeServidor().isAfter(diagnostico.getFimResponsabilidadeChefia())
        );
    }
}
