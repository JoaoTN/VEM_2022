package ufc.npi.prontuario.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.GetEmailUsuario;
import ufc.npi.prontuario.model.GetMatriculaUsuario;
import ufc.npi.prontuario.model.GetUsuarioId;
import ufc.npi.prontuario.model.Papel;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.model.SetSenhaUsuario;
import ufc.npi.prontuario.repository.AlunoRepository;
import ufc.npi.prontuario.service.AlunoService;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.*;


@Service
public class AlunoServiceImpl implements AlunoService {

	@Autowired
	private AlunoRepository alunoRepository;

	public Aluno buscarPorId(Integer id) {
		return alunoRepository.findOne(id);
	}

	@Override
	public void salvar(Aluno aluno) throws ProntuarioException {
		if(aluno.getNome().trim().isEmpty() || GetEmailUsuario.getUsuarioEmail(aluno).trim().isEmpty() || GetMatriculaUsuario.getUsuarioMatricula(aluno).trim().isEmpty()
				|| aluno.getAnoIngresso() == null){
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}
		if(buscarPorMatricula(GetMatriculaUsuario.getUsuarioMatricula(aluno)) != null){
			throw new ProntuarioException(ERRO_ADICIONAR_ALUNO);
		}
		aluno.addPapel(Papel.ESTUDANTE);
		SetSenhaUsuario.setUsuarioSenha(aluno,GetMatriculaUsuario.getUsuarioMatricula(aluno));
		aluno.encodePassword();
		alunoRepository.save(aluno);
	}
////////////////////////////////////////////////////////////////////////////////////////////	
	public void atualizar(Aluno aluno) throws ProntuarioException {
		if(verificarAluno(aluno)){
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}
			
		Aluno alunoExistente = buscarPorMatricula(GetMatriculaUsuario.getUsuarioMatricula(aluno));
		if(alunoExiste(alunoExistente, aluno)) {
			throw new ProntuarioException(ERRO_ATUALIZAR_ALUNO);
		}
		
		SetSenhaUsuario.setUsuarioSenha(aluno,getSenhaAluno(aluno));
		alunoRepository.save(aluno);
	}
	
	public String getSenhaAluno(Aluno aluno) {
		return alunoRepository.findOne(GetUsuarioId.mostrarIdUsuario(aluno)).getSenha();
	}
	
	public boolean alunoExiste(Aluno alunoExistente, Aluno aluno) {
		return alunoExistente != null && !GetUsuarioId.mostrarIdUsuario(alunoExistente).equals(GetUsuarioId.mostrarIdUsuario(aluno));
	}
	
	public boolean verificarAluno(Aluno aluno) {
		boolean nomeAlunoVazio = aluno.getNome().trim().isEmpty();
		boolean emailAlunoVazio = GetEmailUsuario.getUsuarioEmail(aluno).trim().isEmpty();
		boolean matriculaAlunoVazio = verificarMatriculaAluno(aluno);
		boolean anoIngressoAluno = verificarAluno(aluno);
		return nomeAlunoVazio || emailAlunoVazio || matriculaAlunoVazio || anoIngressoAluno;
	}
	
	public boolean verificarMatriculaAluno(Aluno aluno) {
		return GetMatriculaUsuario.getUsuarioMatricula(aluno).trim().isEmpty();	
	}
	
	public boolean verificarIngressoAluno(Aluno aluno) {
		return aluno.getAnoIngresso() == null;	
	}
//////////////////////////////////////////////////////////////////////////////////
	@Override
	public void removerAluno(Integer id) throws ProntuarioException {
		Aluno aluno=alunoRepository.findOne(id);
		if(aluno.getAlunoTurmas().isEmpty() && aluno.getAtendimentos().isEmpty()){
			alunoRepository.delete(id);
		} else {
			throw new ProntuarioException(ERRO_EXCLUIR_ALUNO);
		}
	}

	@Override
	public List<Aluno> buscarTudo() {
		return alunoRepository.findAll();
	}

	@Override
	public Aluno buscarPorMatricula(String matricula) {
		return alunoRepository.findByMatricula(matricula);
	}

	@Override
	public List<Aluno> buscarAjudantes(Integer idTurma, Aluno responsavel) {
		if(alunoRepository.exists(GetUsuarioId.mostrarIdUsuario(responsavel))){
			List<Aluno> ajudantes = alunoRepository.findAllByAlunoTurmasTurmaIdEqualsAndAlunoTurmasAtivoIsTrue(idTurma);
			ajudantes.remove(responsavel);
			return ajudantes;
		}
		
		return null;
	}
	
}
