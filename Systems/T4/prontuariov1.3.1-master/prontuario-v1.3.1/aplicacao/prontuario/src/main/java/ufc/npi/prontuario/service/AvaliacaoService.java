package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Avaliacao;
import ufc.npi.prontuario.model.ItemAvaliacao;


public interface AvaliacaoService {

	public void salvar(Avaliacao avaliacao);
	
	public Avaliacao addItem(Avaliacao avaliacao, ItemAvaliacao item) throws ProntuarioException;
	
	public List<Avaliacao> getTodasAvaliacoes();
	
	public void deleteAvaliacao(Avaliacao avaliacao) throws ProntuarioException;
	
	Avaliacao getAvaliacaoAtiva();
	
	public void deleteItem(ItemAvaliacao item);
	
	public void finalizar(Avaliacao avaliacao);

	public void ativarDesativar(Avaliacao avaliacao) throws ProntuarioException;
}
