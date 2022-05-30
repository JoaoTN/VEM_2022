package ufc.npi.prontuario.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.repository.TipoPatologiaRepository;
import ufc.npi.prontuario.service.TipoPatologiaService;
import ufc.npi.prontuario.util.ExceptionSuccessConstants;

import java.util.ArrayList;
import java.util.List;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;

@Service
public class TipoPatologiaServiceImpl implements TipoPatologiaService {

    @Autowired
    private TipoPatologiaRepository tipoPatologiaRepository;
///////////////////////////////////////
    @Override
    public void salvar(TipoPatologia tipoPatologia) throws ProntuarioException {
        if (verificarTipoPatologiaNomeEDescricao(tipoPatologia)) {
            throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
        }
        if (tipoPatologiaRepository.findByNome(tipoPatologia.getNome()) != null) {
            throw new ProntuarioException(ExceptionSuccessConstants.ERRO_SALVAR_TIPO_PATOLOGIA_EXISTENTE);
        }
        tipoPatologiaRepository.save(tipoPatologia);
    }
    
    public boolean verificarTipoPatologiaNomeEDescricao(TipoPatologia tipoPatologia) {
    	return (tipoPatologia.getNome().trim().isEmpty() || tipoPatologia.getDescricao().trim().isEmpty());
    }
////////////////////////////////////////////
    @Override
    public List<TipoPatologia> buscarTudo() {
        return tipoPatologiaRepository.findAll();
    }

    @Override
    public TipoPatologia buscarPorId(Integer id) {
        return tipoPatologiaRepository.findOne(id);
    }

    @Override
    public void remover(Integer id) throws Exception {
        tipoPatologiaRepository.delete(id);
    }

    @Override
    public List<TipoPatologia> buscarPorNome(String nome) {
        return tipoPatologiaRepository.findByNomeContainingIgnoreCase(nome);
    }

    /////////////////////////////////////////////////////////////////////
    @Override
    public void atualizar(TipoPatologia tipoPatologia) throws ProntuarioException {
        if (verificarTipoPatologia(tipoPatologia)) {
            throw new ProntuarioException(ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS);
        }

        TipoPatologia tipoPatologiaExistente = buscarPorNomeTipoPatologia(tipoPatologia);
        if (verificarTipoPatologiaExistente(tipoPatologiaExistente,tipoPatologia)) {
            throw new ProntuarioException(ExceptionSuccessConstants.ERRO_SALVAR_TIPO_PATOLOGIA_EXISTENTE);
        }

        tipoPatologiaRepository.save(tipoPatologia);
    }
    
    public TipoPatologia buscarPorNomeTipoPatologia(TipoPatologia tipoPatologia) {
    	return tipoPatologiaRepository.findByNome(tipoPatologia.getNome());
    }
    
    
    public boolean verificarTipoPatologia(TipoPatologia tipoPatologia) {
    	return tipoPatologia.getId() == null || tipoPatologia.getNome().trim().isEmpty();
    }
    
    public boolean verificarTipoPatologiaExistente(TipoPatologia tipoPatologiaExistente,TipoPatologia tipoPatologia) {
    	return tipoPatologiaExistente != null && !tipoPatologiaExistente.getId().equals(tipoPatologia.getId());
    }
    /////////////////////////////////////////////////////////////////////
    
    
	@Override
	public List<TipoPatologia> buscarPorIds(List<Integer> idTipos) {
		List<TipoPatologia> tipos = new ArrayList<>();
		for (Integer idTipo : idTipos) {
			tipos.add(tipoPatologiaRepository.getOne(idTipo));
		}
		
		return tipos;
	}
}
