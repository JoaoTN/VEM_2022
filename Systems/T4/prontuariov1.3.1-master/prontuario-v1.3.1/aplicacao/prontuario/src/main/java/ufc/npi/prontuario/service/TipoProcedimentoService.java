package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.TipoProcedimento;

public interface TipoProcedimentoService {

	void salvar(TipoProcedimento tipoProcedimento) throws ProntuarioException;

	void atualizar(TipoProcedimento tipoProcedimento) throws ProntuarioException;

	List<TipoProcedimento> buscarTudo();

	TipoProcedimento buscarPorId(Integer id);

	void remover(Integer id) throws Exception;
	
	List<TipoProcedimento> buscarPorNome(String query);
}
