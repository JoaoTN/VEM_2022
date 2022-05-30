package ufc.npi.prontuario.service;

import java.util.List;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Servidor;

public interface AtendimentoService {

	List<Atendimento> buscarTudoPorAluno(Aluno aluno);
	
	Atendimento buscarPorId(Integer id);

	void salvar(Atendimento atendimento) throws ProntuarioException;
	
	void atualizar(Atendimento atendimento) throws ProntuarioException;

	void finalizarAtendimento(Atendimento atendimento);

	void validarAtendimento(Atendimento atendimento);

	List<Atendimento> buscarAtendimentosNaoFinalizadosPorProfessor(Servidor servidor);

	Boolean existeAtendimentoAbertoAlunoPaciente(Aluno aluno, Paciente paciente);

	List<Atendimento> buscarAtendimentosPorUsuario(Integer idUsuario, Paciente paciente);

	List<Atendimento> buscarAtendimentoPorPaciente(Paciente paciente);

	void remover(Atendimento atendimento);

	Atendimento ultimoAtendimentoAbertoAlunoPaciente(Aluno aluno, Paciente paciente);

	Atendimento adicionarAvaliacaoAtendimento(Atendimento atendimento);

	Atendimento adicionarItemAvaliacaoAtendimento(Integer item, Atendimento atendimento, String nota,
			Integer avaliacao);

	Atendimento adicionarObservacao(Atendimento atendimento, String observacao);

	Atendimento reavaliarItem(Atendimento atendimento, String nota, Integer item);
}
