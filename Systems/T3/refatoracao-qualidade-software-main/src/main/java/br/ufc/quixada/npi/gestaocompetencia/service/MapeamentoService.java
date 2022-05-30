package br.ufc.quixada.npi.gestaocompetencia.service;

import java.util.List;
import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.UnidadeMapeada;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.Etapa;

public interface MapeamentoService {

    Mapeamento findMapeamentoByUsuario(Usuario usuario);

    List<Mapeamento> getFinalizados(Usuario usuario);

    Mapeamento findLastMapeamento(Usuario usuario);

    List<Unidade> findAllUnidadesInNormalizacao(Etapa etapa);

    List<Unidade> findAllSubunidadesInNormalizacao(Unidade unidade, Etapa etapa);

    Mapeamento findByUnidade(Unidade unidade);

    List<Mapeamento> findAllByUnidade(Unidade unidade);

    List<Mapeamento> findAllSecondary(Unidade unidade);

    boolean pertence(Unidade unidade, Mapeamento mapeamento);

    List<Unidade> getUnidadesByMapeamento(Mapeamento mapeamento);

    Boolean verifyAccess(Mapeamento mapeamento, Unidade unidade);

    Boolean verifySecondaryAccess(Mapeamento mapeamento, Unidade unidade);

    Mapeamento create(Mapeamento mapeamento);

    Mapeamento update(Mapeamento mapeamento);

    List<Mapeamento> findAll();

    List<UnidadeMapeada> findAllUnidadesMapeadas();

    boolean delete(Mapeamento mapeamento);

    boolean isEmAndamento(Mapeamento mapeamento);
}