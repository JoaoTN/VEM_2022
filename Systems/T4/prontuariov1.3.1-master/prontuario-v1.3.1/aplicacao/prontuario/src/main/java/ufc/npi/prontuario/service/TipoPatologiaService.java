package ufc.npi.prontuario.service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.TipoPatologia;

import java.util.List;

public interface TipoPatologiaService {

    void salvar(TipoPatologia tipoPatologia) throws ProntuarioException;

    void atualizar(TipoPatologia tipoPatologia) throws ProntuarioException;

    List<TipoPatologia> buscarTudo();

    List<TipoPatologia> buscarPorNome(String nome);

    List<TipoPatologia> buscarPorIds(List<Integer> idTipos);
    
    TipoPatologia buscarPorId(Integer id);

    void remover(Integer id) throws Exception;
    
}
