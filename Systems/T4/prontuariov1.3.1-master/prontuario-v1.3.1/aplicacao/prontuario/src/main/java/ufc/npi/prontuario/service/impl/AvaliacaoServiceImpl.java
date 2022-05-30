package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_AVALIACAO_ATIVA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_AVALIACAO_CAMPOS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_AVALIACAO_COM_ATENDIMENTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Avaliacao;
import ufc.npi.prontuario.model.Avaliacao.Status;
import ufc.npi.prontuario.model.ItemAvaliacao;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.repository.AvaliacaoRepository;
import ufc.npi.prontuario.repository.ItemAvaliacaoRepository;
import ufc.npi.prontuario.service.AvaliacaoService;

@Service
public class AvaliacaoServiceImpl implements AvaliacaoService {

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;
	
	@Autowired
	private ItemAvaliacaoRepository itemRepository;

	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	
	@Override
	public void salvar(Avaliacao avaliacao) {
		avaliacaoRepository.save(avaliacao);
	}
	
	@Override
	public Avaliacao addItem(Avaliacao avaliacao, ItemAvaliacao item) throws ProntuarioException {	
		if (itemAvaliacaoIsValid(item)) {
			item.setNome(item.getNome().toUpperCase());
			avaliacao.addItem(item);
			
			return avaliacaoRepository.saveAndFlush(avaliacao);
		} else {
			throw new ProntuarioException(ERRO_AVALIACAO_CAMPOS);
		}
	}
	
	private boolean itemAvaliacaoIsValid(ItemAvaliacao itemAvaliacao) {
		String nome = itemAvaliacao.getNome();
		Integer peso = itemAvaliacao.getPeso();
		
		return !(nome.isEmpty()) || nome != null || peso != null; 
	}
	
	@Override
	public Avaliacao getAvaliacaoAtiva() {
		return avaliacaoRepository.findAvaliacaoAtiva();
	}

	@Override
	public List<Avaliacao> getTodasAvaliacoes() {
		return avaliacaoRepository.findAllByOrderByAtivaDesc();
	}

	@Override
	public void deleteAvaliacao(Avaliacao avaliacao) throws ProntuarioException {
		if (atendimentoRepository.findAllByAvaliacao_avaliacao(avaliacao).size() > 0) {
			throw new ProntuarioException(ERRO_EXCLUIR_AVALIACAO_COM_ATENDIMENTO);
		}
		avaliacaoRepository.delete(avaliacao);
	}

	@Override
	public void deleteItem(ItemAvaliacao item) {
		itemRepository.delete(item);
	}
	
	@Override
	public void finalizar(Avaliacao avaliacao) {
		avaliacao.setStatus(Status.FINALIZADA);
		avaliacaoRepository.save(avaliacao);
	}

	@Override
	public void ativarDesativar(Avaliacao avaliacao) throws ProntuarioException{
		if (avaliacao.isAtiva()) {
			avaliacao.setAtiva(false);
		} else {
			if (getAvaliacaoAtiva() != null) {
				throw new ProntuarioException(ERRO_AVALIACAO_ATIVA);
			}
			avaliacao.setAtiva(true);
		}
		avaliacaoRepository.save(avaliacao);
		
	}
}

