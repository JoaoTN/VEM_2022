package ufc.npi.prontuario.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Procedimento;

public interface ProcedimentoService {

	public List<Procedimento> salvar(String faceDente, List<Integer> idProcedimentos, String localString,
			Integer idOdontograma, String descricao, Aluno aluno, Boolean preExistente, List<Integer> patologias, Date data) throws ProntuarioException;

	List<Procedimento> buscarProcedimentosOdontograma(Odontograma odontograma, Integer idUsuarioLogado);

	List<Procedimento> buscarProcedimentosExistentesOdontograma(Odontograma odontograma, Integer idUsuarioLogado);
	
	List<Procedimento> tabelaProcedimentosOdontograma(Odontograma odontograma, Authentication auth);
	
	void deletar(Procedimento procedimento);
}
