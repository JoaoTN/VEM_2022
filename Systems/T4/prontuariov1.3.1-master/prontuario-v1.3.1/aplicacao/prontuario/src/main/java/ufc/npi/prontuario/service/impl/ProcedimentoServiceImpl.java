package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.NENHUM_ATENDIMENTO_ABERTO_EXCEPTION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Atendimento.Status;
import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.FaceDente;
import ufc.npi.prontuario.model.GetUsuarioId;
import ufc.npi.prontuario.model.Local;
import ufc.npi.prontuario.model.Odontograma;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Procedimento;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.TipoProcedimento;
import ufc.npi.prontuario.model.Tratamento;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.repository.AlunoRepository;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.repository.OdontogramaRepository;
import ufc.npi.prontuario.repository.PatologiaRepository;
import ufc.npi.prontuario.repository.ProcedimentoRepository;
import ufc.npi.prontuario.repository.ServidorRepository;
import ufc.npi.prontuario.repository.TipoProcedimentoRepository;
import ufc.npi.prontuario.service.ProcedimentoService;

@Service
public class ProcedimentoServiceImpl implements ProcedimentoService {

	@Autowired
	private OdontogramaRepository odontogramaPatologiaRepository;

	@Autowired
	private ProcedimentoRepository procedimentoRepository;

	@Autowired
	private TipoProcedimentoRepository tipoProcedimentoRepository;

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private ServidorRepository servidorRepository;

	@Autowired
	private PatologiaRepository patologiaRepository;



	public List<Procedimento> salvar(String faceDente, List<Integer> idProcedimentos, String localString,
			Integer idOdontograma, String descricao, Aluno aluno, Boolean preExistente, List<Integer> patologias,
			Date data) throws ProntuarioException {
		Odontograma odontograma = odontogramaPatologiaRepository.findOne(idOdontograma);

		// Verificação de restrições
		List<Atendimento> atendimentos = buscarAtendimentos(aluno, odontograma);

		verificarSeAtendimentoEstaAberto(atendimentos);
		// Fim da verificação de restrições

		List<Procedimento> procedimentos = new ArrayList<>();
		List<TipoProcedimento> tipoProcedimentos = getListTipoProcedimento(idProcedimentos);

		Procedimento procedimento = configurarProcedimentos(localString, faceDente, descricao, preExistente, atendimentos, odontograma);
		
		procedimentos = salvarProcedimentos(tipoProcedimentos, procedimento);
		
		salvarPatologias(patologias, data, descricao, aluno);

		return procedimentos;
	}

	private List<Atendimento> buscarAtendimentos(Aluno aluno, Odontograma odontograma) {
		return atendimentoRepository.findAllByResponsavelOrAjudanteExist(aluno,
				odontograma.getPaciente(), Status.EM_ANDAMENTO);
	}

	private void verificarSeAtendimentoEstaAberto(List<Atendimento> atendimentos) throws ProntuarioException  {
		if (atendimentos.size() != 1) {
			throw new ProntuarioException(NENHUM_ATENDIMENTO_ABERTO_EXCEPTION);
		}
	}

	private Procedimento configurarProcedimentos(String localString, String faceDente, String descricao, Boolean preExistente,
												 List<Atendimento> atendimentos, Odontograma odontograma) {
		Local local = Local.valueOf(localString);

		FaceDente face = null;
		Dente dente = null;

		if (local == Local.FACE) {
			face = FaceDente.valueOf(faceDente.substring(3));
			dente = Dente.valueOf("D" + faceDente.substring(0, 2));

		} else if (local == Local.DENTE) {
			dente = Dente.valueOf("D" + faceDente);
		}

		return new Procedimento(dente, face, local, null,
				descricao, atendimentos.get(0), odontograma, preExistente);
	}

	private void salvarPatologias(List<Integer> patologias, Date data, String descricao, Aluno aluno) {

		if (patologias != null && !patologias.isEmpty()) {
//
			Tratamento tratamento = new Tratamento(data,aluno,descricao);
			for (Integer p : patologias) {
				Patologia patologia = patologiaRepository.findOne(p);
				tratamento.setPatologia(patologia);
				patologia.setTratamento(tratamento);
				patologiaRepository.saveAndFlush(patologia);
			}
		}
	}

	private List<Procedimento> salvarProcedimentos(List<TipoProcedimento> tipoProcedimentos, Procedimento procedimento) {
		
		List<Procedimento> procedimentos = new ArrayList<>();
		
		for (TipoProcedimento tipo : tipoProcedimentos) {
			procedimento.setTipoProcedimento(tipo);
			procedimentos.add(procedimento);
			procedimentoRepository.save(procedimento);
		}
		
		return procedimentos;
	}
	
	private List<TipoProcedimento> getListTipoProcedimento(List<Integer> idProcedimentos){
		List<TipoProcedimento> tipoProcedimentos = new ArrayList<>();
		
		for (Integer idTipo : idProcedimentos) {
			TipoProcedimento tipo = tipoProcedimentoRepository.findOne(idTipo);
			tipoProcedimentos.add(tipo);
		}
		
		return tipoProcedimentos;
	}

	@Override
	public List<Procedimento> buscarProcedimentosOdontograma(Odontograma odontograma, Integer idUsuarioLogado) {

		List<Procedimento> procedimentos = new ArrayList<>();
		procedimentos.addAll(findProcedimentosByUsuario(odontograma, idUsuarioLogado));
		procedimentos.addAll(findProcedimentosByAtendimento(odontograma, procedimentos));

		Collections.sort(procedimentos);
		return procedimentos;
	}

	@Override
	public List<Procedimento> buscarProcedimentosExistentesOdontograma(Odontograma odontograma,
			Integer idUsuarioLogado) {
		
		List<Procedimento> procedimentos = new ArrayList<>(); 
		procedimentos.addAll(findProcedimentosExistenteByUsuario(odontograma, idUsuarioLogado));
		procedimentos.addAll(findProcedimentosByAtendimentoExistente(odontograma, procedimentos));
		
		procedimentos.sort((p1, p2) -> p1.getAtendimento().getData().compareTo(p1.getAtendimento().getData()));
		return procedimentos;
	}

	@Override
	public void deletar(Procedimento procedimento) {
		procedimento.getOdontograma().getProcedimentos().remove(procedimento);
		procedimento.getAtendimento().getProcedimentos().remove(procedimento);
		procedimentoRepository.delete(procedimento);
	}
	
	public Integer mostrarIdUsuario(Usuario usuario) {
		return GetUsuarioId.mostrarIdUsuario(usuario);
	}

	@Override
	public List<Procedimento> tabelaProcedimentosOdontograma(Odontograma odontograma, Authentication auth) {
		Usuario usuario = (Usuario) auth.getPrincipal();
		
		int idUsuario = mostrarIdUsuario(usuario);
		
		List<Procedimento> procedimentos = buscarProcedimentosOdontograma(odontograma, idUsuario);
		procedimentos.addAll(buscarProcedimentosExistentesOdontograma(odontograma, idUsuario));
		procedimentos.sort((p1, p2) -> p1.getDataAtendimento().compareTo(p2.getDataAtendimento()));
		return procedimentos;
	}

	
	private List<Procedimento> findProcedimentosByUsuario(Odontograma odontograma,	Integer idUsuarioLogado){
		List<Procedimento> procedimentos = findProcedimentosByAluno(odontograma, idUsuarioLogado);
		if (procedimentos.isEmpty()) {
			procedimentos = findProcedimentosByServidor(odontograma, idUsuarioLogado);
		}
		return procedimentos;
	}
	
	private List<Procedimento> findProcedimentosByAluno(Odontograma odontograma, Integer idUsuarioLogado){
		Aluno aluno = alunoRepository.findOne(idUsuarioLogado);
		return procedimentoRepository
				.findAllByOdontogramaAndAtendimentoResponsavelAndPreExistenteIsFalseOrOdontogramaAndAtendimentoAjudanteAndPreExistenteIsFalse(
						odontograma, aluno, odontograma, aluno);
	}
	
	private List<Procedimento> findProcedimentosByServidor(Odontograma odontograma, Integer idUsuarioLogado){
		Servidor servidor = servidorRepository.findOne(idUsuarioLogado);
		return procedimentoRepository
				.findAllByOdontogramaAndAtendimentoProfessorAndPreExistenteIsFalse(odontograma, servidor);
	}
	
	private List<Procedimento> findProcedimentosExistenteByUsuario(Odontograma odontograma,	Integer idUsuarioLogado){
		List<Procedimento> procedimentos = findProcedimentosExistenteByAluno(odontograma, idUsuarioLogado);
		if (procedimentos.isEmpty()) {
			procedimentos = findProcedimentosExistenteByServidor(odontograma, idUsuarioLogado);
		}
		return procedimentos;
	}
	
	private List<Procedimento> findProcedimentosExistenteByAluno(Odontograma odontograma, Integer idUsuarioLogado){
		Aluno aluno = alunoRepository.findOne(idUsuarioLogado);
		return procedimentoRepository
				.findAllByOdontogramaAndAtendimentoResponsavelAndPreExistenteIsTrueOrOdontogramaAndAtendimentoAjudanteAndPreExistenteIsTrue(
						odontograma, aluno, odontograma, aluno);
	}
	
	private List<Procedimento> findProcedimentosExistenteByServidor(Odontograma odontograma, Integer idUsuarioLogado){
		Servidor servidor = servidorRepository.findOne(idUsuarioLogado);
		return procedimentoRepository
				.findAllByOdontogramaAndAtendimentoProfessorAndPreExistenteIsTrue(odontograma, servidor);
	}
	
	private List<Procedimento> findProcedimentosByAtendimento(Odontograma odontograma, List<Procedimento> procedimentos){
		
		if(procedimentos.isEmpty()) {
			return procedimentos = procedimentoRepository
					.findAllByOdontogramaAndAtendimentoStatusAndPreExistenteIsTrue(odontograma, Status.VALIDADO);
		}
		
		return findProcedimentosNaoValidados(odontograma, procedimentos);
	}
	
	private List<Procedimento> findProcedimentosNaoValidados(Odontograma odontograma, List<Procedimento> procedimentos){
		
		List<Integer> idProcedimentos = new ArrayList<>();
		for (Procedimento procedimento : procedimentos) {
			idProcedimentos.add(procedimento.getId());
		}
		
		return procedimentoRepository.findAllByOdontogramaAndIdIsNotInAndAtendimentoStatusAndPreExistenteIsTrue(
						odontograma, idProcedimentos, Status.VALIDADO);
	}
	
	private List<Procedimento> findProcedimentosByAtendimentoExistente(Odontograma odontograma, List<Procedimento> procedimentos){
		
		if(procedimentos.isEmpty()) {
			return procedimentos = procedimentoRepository
					.findAllByOdontogramaAndAtendimentoStatusAndPreExistenteIsTrue(odontograma, Status.VALIDADO);
		}
		
		return findProcedimentosValidados(odontograma, procedimentos);
	}
	
	private List<Procedimento> findProcedimentosValidados(Odontograma odontograma, List<Procedimento> procedimentos){
		
		List<Integer> idProcedimentos = new ArrayList<>();
		for (Procedimento procedimento : procedimentos) {
			idProcedimentos.add(procedimento.getId());
		}
		
		return procedimentoRepository.findAllByOdontogramaAndIdIsNotInAndAtendimentoStatusAndPreExistenteIsTrue(
						odontograma, idProcedimentos, Status.VALIDADO);
	}
	
}
