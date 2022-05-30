package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EDITAR_ATENDIMENTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Atendimento.Status;
import ufc.npi.prontuario.model.Avaliacao;
import ufc.npi.prontuario.model.AvaliacaoAtendimento;
import ufc.npi.prontuario.model.GetAtendimentoId;
import ufc.npi.prontuario.model.ItemAvaliacao;
import ufc.npi.prontuario.model.ItemAvaliacaoAtendimento;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Papel;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.repository.AlunoRepository;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.repository.AvaliacaoAtendimentoRepository;
import ufc.npi.prontuario.repository.AvaliacaoRepository;
import ufc.npi.prontuario.repository.ItemAvaliacaoAtendimentoRepository;
import ufc.npi.prontuario.repository.ItemAvaliacaoRepository;
import ufc.npi.prontuario.repository.ServidorRepository;
import ufc.npi.prontuario.service.AtendimentoService;

@Service
public class AtendimentoServiceImpl implements AtendimentoService {

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private ServidorRepository servidorRepository;

	@Autowired
	private AvaliacaoAtendimentoRepository avaliacaoAtendimentoRepository;

	@Autowired
	private ItemAvaliacaoRepository itemAvaliacaoRepository;

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired
	private ItemAvaliacaoAtendimentoRepository itemAvaliacaoAtendimentoRepository;
	
	@Override
	public Atendimento buscarPorId(Integer id) {
		return atendimentoRepository.findOne(id);
	}
//////////////////////////////////////////////////////
	@Override
	public void salvar(Atendimento atendimento) throws ProntuarioException {
		
		List<Atendimento> atendimentos = buscarResponsavelOrAjustante(atendimento);

		if (atendimentos.size() >= 1) {
			throw new ProntuarioException(ERRO_ADICIONAR_ATENDIMENTO);
		}

		atendimento.setStatus(Status.EM_ANDAMENTO);
		atendimentoRepository.save(atendimento);
	}
	
	public List<Atendimento> buscarResponsavelOrAjustante(Atendimento atendimento){
		return atendimentoRepository.findAllByResponsavelOrAjudanteExist(
				retornaAluno(atendimento), retornaAjudante(atendimento), retornaPaciente(atendimento),
				Status.EM_ANDAMENTO);
	}
	
	public Aluno retornaAluno(Atendimento atendimento) {
		return atendimento.getResponsavel();
	}
	
	public Aluno retornaAjudante(Atendimento atendimento) {
		return atendimento.getAjudante();
	}
	
	public Paciente retornaPaciente(Atendimento atendimento) {
		return atendimento.getPaciente();
	}
	
//////////////////////////////////////////////////////	

	@Override
	public void atualizar(Atendimento atendimento) throws ProntuarioException {
		if (atendimentoRepository.exists(GetAtendimentoId.getIdAtendimento(atendimento))) {
			atendimentoRepository.save(atendimento);
		} else {
			throw new ProntuarioException(ERRO_EDITAR_ATENDIMENTO);
		}
	}

	@Override
	public List<Atendimento> buscarTudoPorAluno(Aluno aluno) {
		return atendimentoRepository.findAllByResponsavelOrAjudanteOrderByDataDesc(aluno, aluno);

	}

	@Override
	public void finalizarAtendimento(Atendimento atendimento) {
		atendimento.setStatus(Status.REALIZADO);
		atendimentoRepository.save(atendimento);
	}

	@Override
	public void validarAtendimento(Atendimento atendimento) {
		atendimento.setStatus(Status.VALIDADO);
		atendimentoRepository.save(atendimento);
	}

	@Override
	public List<Atendimento> buscarAtendimentosNaoFinalizadosPorProfessor(Servidor servidor) {
		List<Status> status = new ArrayList<Status>();
		status.add(Status.VALIDADO);
		return atendimentoRepository.findAllByProfessorAndStatusNotIn(servidor, status);
	}

	@Override
	public Boolean existeAtendimentoAbertoAlunoPaciente(Aluno aluno, Paciente paciente) {
		List<Atendimento> atendimentos = atendimentoRepository.findAllByResponsavelOrAjudanteExist(aluno, paciente,
				Status.EM_ANDAMENTO);
		return !atendimentos.isEmpty();
	}

	@Override
	public void remover(Atendimento atendimento) {
		atendimentoRepository.delete(atendimento);
	}

	@Override
	public Atendimento ultimoAtendimentoAbertoAlunoPaciente(Aluno aluno, Paciente paciente) {
		List<Atendimento> atendimentos = atendimentoRepository.findAllByResponsavelOrAjudanteExist(aluno, paciente,
				Status.EM_ANDAMENTO);
		if (atendimentos.isEmpty()) {
			return null;
		} else {
			return atendimentos.get(0);
		}
	}

	@Override
	public Atendimento adicionarAvaliacaoAtendimento(Atendimento atendimento) {
		if (verificarAvaliacaoAtendimento(atendimento)) {
			atendimento.setAvaliacao(getAvaliacaoAtendimento());
			atendimentoRepository.saveAndFlush(atendimento);
		}
		return atendimento;
	}

	private boolean verificarAvaliacaoAtendimento(Atendimento atendimento) {
		return Objects.isNull(atendimento.getAvaliacao());
	}
	
	private AvaliacaoAtendimento getAvaliacaoAtendimento() {
		AvaliacaoAtendimento avaliacaoAtendimento = new AvaliacaoAtendimento();
		Avaliacao avaliacaoAtiva = avaliacaoRepository.findAvaliacaoAtiva();
		for (ItemAvaliacao item : avaliacaoAtiva.getItens()) {
			avaliacaoAtendimento.addItem(new ItemAvaliacaoAtendimento(item, avaliacaoAtendimento));
		}
		avaliacaoAtendimento.setAvaliacao(avaliacaoAtiva);
		avaliacaoAtendimentoRepository.saveAndFlush(avaliacaoAtendimento);
		return avaliacaoAtendimento;
	}

	@Override
	public Atendimento adicionarItemAvaliacaoAtendimento(Integer item, Atendimento atendimento, String nota,
			Integer avaliacao) {
		Atendimento old = getFindOneAtendimentoRepos(atendimento);
		ItemAvaliacao itemOld = itemAvaliacaoRepository.findOne(item);
		Avaliacao avaliacaoOld = getFindOneAvaliacaoRepository(avaliacao);

		old.getAvaliacao().setAvaliacao(avaliacaoOld);

		ItemAvaliacaoAtendimento avaliacaoAtendimento = new ItemAvaliacaoAtendimento(Double.valueOf(nota), itemOld);
		addItemAvaliacao(old,avaliacaoAtendimento);
		addDataAvaliacao(old);
		atendimentoRepository.save(old);

		return old;
	}
	
	public Atendimento getFindOneAtendimentoRepos(Atendimento atendimento) {
		return atendimentoRepository.findOne(GetAtendimentoId.getIdAtendimento(atendimento));
	}
	
	public ItemAvaliacao getFindOneItemAvaliacaoRepository(Integer item) {
		return itemAvaliacaoRepository.findOne(item);
	}
	
	public Avaliacao getFindOneAvaliacaoRepository(Integer avaliacao) {
		return avaliacaoRepository.findOne(avaliacao);
	}
	
	public void addItemAvaliacao(Atendimento old,ItemAvaliacaoAtendimento avaliacaoAtendimento) {
		old.getAvaliacao().addItem(avaliacaoAtendimento);
	}
	public void addDataAvaliacao(Atendimento old) {
		old.getAvaliacao().setData(new Date());
	}

//////////////////////////////////////////////////////////////////
	@Override
	public Atendimento adicionarObservacao(Atendimento atendimento, String observacao) {
		Atendimento old = atendimentoRepository.findOne(GetAtendimentoId.getIdAtendimento(atendimento));
		if (verificarObservacao(observacao)) {
			inserirObservacao(old,observacao);
		}
		atendimentoRepository.save(old);

		return old;
	}
	
	public boolean verificarObservacao(String observacao) {
		return observacao != null && observacao.replace(" ", "").length() > 0;	
	}
	
	public void inserirObservacao(Atendimento old,String observacao) {
		old.getAvaliacao().setObservacao(observacao);
	}
	
	public void inserirData(Atendimento old) {
		old.getAvaliacao().setData(new Date());
	}
/////////////////////////////////////////////////////////////////////	

	public List<Atendimento> buscarAtendimentoPorPaciente(Paciente paciente) {
		List<Atendimento> atendimentos = new ArrayList<>();
		atendimentos.addAll(atendimentoRepository.findAllByPaciente(paciente));
		return atendimentos;
	}

	@Override
	public Atendimento reavaliarItem(Atendimento atendimento, String nota, Integer item) {

		Atendimento old = atendimentoRepository.findOne(GetAtendimentoId.getIdAtendimento(atendimento));
		ItemAvaliacaoAtendimento itemOld = itemAvaliacaoAtendimentoRepository.findOne(item);

		itemOld.setNota(Double.valueOf(nota));
		old.getAvaliacao().setData(new Date());
		itemAvaliacaoAtendimentoRepository.save(itemOld);

		return old;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public List<Atendimento> buscarAtendimentosPorUsuario(Integer idUsuario, Paciente paciente) {
		Aluno aluno = findOneAluno(idUsuario);
		Servidor servidor = findOneServidor(idUsuario);

		List<Atendimento> atendimentos = new ArrayList<>();
		if (aluno != null) {
			atendimentos = findAllByResponsavelAndPacienteOrAjudanteAndPaciente(aluno, paciente);
		} else if (servidor != null) {
			atendimentos = findAllByProfessorAndPaciente(servidor, paciente);
		}
		if (servidor != null && servidor.getPapeis().contains(Papel.ADMINISTRACAO)) {
			atendimentos = this.buscarAtendimentoPorPaciente(paciente);
		}
		if (atendimentos.isEmpty()) {
			atendimentos = findAllByStatusAndPaciente(Status.VALIDADO, paciente);

		} else {

			List<Integer> idAtendimentos = new ArrayList<>();
			for (Atendimento atendimento : atendimentos) {
				idAtendimentos.add(GetAtendimentoId.getIdAtendimento(atendimento));
			}

			atendimentos.addAll(findAllByStatusAndPacienteAndIdIsNotIn(Status.VALIDADO, paciente,
					idAtendimentos));
		}
		Collections.sort(atendimentos);

		return atendimentos;
	}
	
	public Aluno findOneAluno(Integer idUsuario) {
		return alunoRepository.findOne(idUsuario);
	}
	
	public Servidor findOneServidor(Integer idUsuario) {
		return servidorRepository.findOne(idUsuario);
	}
	
	public List<Atendimento>findAllByResponsavelAndPacienteOrAjudanteAndPaciente(Aluno aluno, Paciente paciente){
		return atendimentoRepository.findAllByResponsavelAndPacienteOrAjudanteAndPaciente(aluno, paciente, aluno, paciente);
	}
	
	public List<Atendimento>findAllByProfessorAndPaciente(Servidor servidor, Paciente paciente){
		return atendimentoRepository.findAllByProfessorAndPaciente(servidor, paciente);
	}
	
	public List<Atendimento>findAllByStatusAndPaciente(Status status, Paciente paciente){
		return atendimentoRepository.findAllByStatusAndPaciente(status, paciente);
	}
	
	public List<Atendimento>findAllByStatusAndPacienteAndIdIsNotIn(Status status, Paciente paciente, List<Integer> idAtendimentos){
		return atendimentoRepository.findAllByStatusAndPacienteAndIdIsNotIn(Status.VALIDADO, paciente, idAtendimentos);
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////

}
