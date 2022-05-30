package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.TipoProcedimento;
import ufc.npi.prontuario.repository.TipoProcedimentoRepository;
import ufc.npi.prontuario.service.TipoProcedimentoService;
import ufc.npi.prontuario.util.ExceptionSuccessConstants;

@Service
public class TipoProcedimentoServiceImpl implements TipoProcedimentoService{
	
	@Autowired
	private TipoProcedimentoRepository tipoProcedimentoRepository;

/////////////////////////////////////////////////////////////////	
	@Override
	public void salvar(TipoProcedimento tipoProcedimento) throws ProntuarioException {
		if(verificarTipoProcedimentoNomeEDescricao(tipoProcedimento)) {
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}
		if(tipoProcedimentoRepository.findByNome(tipoProcedimento.getNome()) != null) {
			throw new ProntuarioException(ExceptionSuccessConstants.ERRO_SALVAR_TIPO_PROCEDIMENTO_EXISTENTE);
		}
		tipoProcedimentoRepository.save(tipoProcedimento);
	}
	
	public boolean verificarTipoProcedimentoNomeEDescricao(TipoProcedimento tipoProcedimento) {
		return tipoProcedimento.getNome().trim().isEmpty() || tipoProcedimento.getDescricao().trim().isEmpty();
	}
	
	
///////////////////////////////////////////////////////////////	
///////////////////////////////////////////////////////////////	
	@Override
	public void atualizar(TipoProcedimento tipoProcedimento) throws ProntuarioException {
		if (verificarTipoProcedimentoIDENome(tipoProcedimento)) {
			throw new ProntuarioException(ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS);
		}

		TipoProcedimento tipoProcedimentoExistente = buscarPorNomeTipoProcedimento(tipoProcedimento);
		if (verificarTipoProcedimentoExistente(tipoProcedimento,tipoProcedimentoExistente)) {
			throw new ProntuarioException(ExceptionSuccessConstants.ERRO_SALVAR_TIPO_PATOLOGIA_EXISTENTE);
		}

		tipoProcedimentoRepository.save(tipoProcedimento);
	}
	
	public boolean verificarTipoProcedimentoIDENome(TipoProcedimento tipoProcedimento) {
		return tipoProcedimento.getId() == null || tipoProcedimento.getNome().trim().isEmpty();
	}
	
	public boolean verificarTipoProcedimentoExistente(TipoProcedimento tipoProcedimento,TipoProcedimento tipoProcedimentoExistente) {
		return tipoProcedimentoExistente != null && !tipoProcedimentoExistente.getId().equals(tipoProcedimento.getId());
	}
	
	public TipoProcedimento buscarPorNomeTipoProcedimento(TipoProcedimento tipoProcedimento) {
		return tipoProcedimentoRepository.findByNome(tipoProcedimento.getNome());
	}
	
///////////////////////////////////////////////////////////////	
	@Override
	public List<TipoProcedimento> buscarTudo() {
		return tipoProcedimentoRepository.findAll();
	}

	@Override
	public TipoProcedimento buscarPorId(Integer id) {
		return tipoProcedimentoRepository.findOne(id);
	}

	@Override
	public void remover(Integer id) throws Exception {
		tipoProcedimentoRepository.delete(id);
	}

	@Override
	public List<TipoProcedimento> buscarPorNome(String query) {
		return tipoProcedimentoRepository.findByNomeContainingIgnoreCase(query);
	}

}
