package ufc.npi.prontuario.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.AlunoTurma;
import ufc.npi.prontuario.model.AlunoTurmaId;
import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.GetProfessorTurma;
import ufc.npi.prontuario.model.GetUsuarioId;
import ufc.npi.prontuario.model.Papel;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.Turma;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.repository.AlunoRepository;
import ufc.npi.prontuario.repository.AlunoTurmaRepository;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.repository.TurmaRepository;
import ufc.npi.prontuario.repository.UsuarioRepository;
import ufc.npi.prontuario.service.TurmaService;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.*;


@Service
public class TurmaServiceImpl implements TurmaService {

	@Autowired
	private TurmaRepository turmaRepository;
	@Autowired
	private AlunoRepository alunoRepository;
	@Autowired
	private AlunoTurmaRepository alunoTurmaRepository;
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;

///////////////////////////////////////////////////////////////////
	public void salvar(Turma turma) throws ProntuarioException {
		boolean turmaExiste = turma.turmaExisteNoRepositorio(turmaRepository);
		if(turmaExiste){
			throw new ProntuarioException(ERRO_SALVAR_TURMA_EXISTENTE);
		}
		turma.setAtivo(true);
		turmaRepository.save(turma);
	}
/////////////////////////////////////////////////////////////////
	public Turma buscarPorId(Integer id) {
		return turmaRepository.findOne(id);
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
    public void inscreverAluno(Turma turma, String matricula) throws ProntuarioException {
		Aluno aluno = alunoRepository.findByMatricula(matricula);
		if (alunoNull(aluno)) {
			throw new ProntuarioException(ERROR_MATRICULA_INEXISTENTE);
		}
		if (turmaNotNull(turma)) {
			AlunoTurma inscricao = new AlunoTurma(); 
			inscricao.setInscricao(aluno, turma, true);

			if(turma.getAlunoTurmas().contains(inscricao)){
				throw new ProntuarioException(ERROR_ALUNO_JA_CADASTRADO);
			}

			turma.getAlunoTurmas().add(inscricao);
			turmaRepository.save(turma);
		}
	}
	
	public boolean alunoNull(Aluno aluno) {
		return aluno == null;
	}
	
	public boolean turmaNotNull(Turma turma) {
		return turma != null;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////

	public void removerInscricao(Turma turma, Aluno aluno) throws ProntuarioException {
		List<Atendimento> atendimentosRealizados = atendimentoRepository.findAllByResponsavelAndTurmaOrAjudanteAndTurma(aluno,turma,aluno,turma);
		if(!atendimentosRealizados.isEmpty()) {
			throw new ProntuarioException(ERROR_ALUNO_POSSUI_ATENDIMENTO);
		}
		
		AlunoTurmaId idInscricao = new AlunoTurmaId();
		idInscricao.setAluno(aluno);
		idInscricao.setTurma(turma);
		AlunoTurma inscricao = alunoTurmaRepository.findOne(idInscricao);
		if (inscricao == null) {
			throw new ProntuarioException(ERROR_INSCRICAO_ALUNO_NAO_ENCONTRADA);
		}
 
		alunoTurmaRepository.delete(inscricao);
		alunoTurmaRepository.flush();
	}

	@Override
	public List<Turma> buscarTudo() {
		return turmaRepository.findAll();
	}

	@Override
	public void alterarStatus(Turma turma) {
		turma.setAtivo(!turma.getAtivo());
		turmaRepository.save(turma);
	}

	public List<Servidor> mostrarTurmaProfessores(Turma turma){
		return GetProfessorTurma.mostrarTurmaProfessores(turma);
	}

	@Override
	public void adicionarProfessorTurma(Turma turma, List<Servidor> professores) {
		if (!professores.isEmpty()) {
			for (Servidor professor : professores) {
				mostrarTurmaProfessores(turma).add(professor);
			}
			turmaRepository.save(turma);
		}
	}

	public List<Turma> buscarAtivasPorAluno(Aluno aluno) {
		return turmaRepository.findByAlunoTurmasAlunoAndAtivoIsTrueOrderByNome(aluno);
	}

	@Override
	public List<Usuario> buscarProfessores(List<Servidor> professores) {
		if (professores.isEmpty()) {
			return usuarioRepository.findByPapeisOrderByNome(Papel.PROFESSOR);
		}

		return buscarProfessoresPeloId(professores);
	}

	public Integer mostrarIdUsuario(Usuario usuario) {
		return GetUsuarioId.mostrarIdUsuario(usuario);
	}

	 private List<Usuario> buscarProfessoresPeloId(List<Servidor> professores) {
	 	List<Integer> professoresId = new ArrayList<Integer>();
	 	for (Usuario professor : professores) {
	 		professoresId.add(mostrarIdUsuario(professor));
	 	}
	 	return usuarioRepository.findByIdNotInAndPapeisOrderByNome(professoresId, Papel.PROFESSOR);
	 }

	@Override
	public List<Turma> buscarTurmas(Servidor servidor) {
		return servidor.getPapeis().contains(Papel.ADMINISTRACAO) ? turmaRepository.findAll() : turmaRepository.findAllByProfessores(servidor);
	}
	
	@Override
	public List<Turma> buscarTurmasProfessor(Servidor servidor) {
		return turmaRepository.findAllByProfessores(servidor);
	}
	
	@Override
	public void removerTurma(Integer id) throws ProntuarioException {
		Turma turma = turmaRepository.findOne(id);
		boolean hasAlunosMatriculados = !turma.getAlunoTurmas().isEmpty();
		boolean hasAtendimentosRealizados = !turma.getAtendimentos().isEmpty();
		
		if(hasAlunosMatriculados || hasAtendimentosRealizados) {
			throw new ProntuarioException(ERRO_EXCLUIR_TURMA_EXCEPTION);
		}
		turmaRepository.delete(id);
	}

	public void removerProfessor(Turma turma, Servidor professor) throws ProntuarioException {
		List<Atendimento> atendimentosRealizados = atendimentoRepository.findAllByProfessorAndTurma(professor, turma);
		if (!atendimentosRealizados.isEmpty()) {
			throw new ProntuarioException(ERROR_PROFESSOR_POSSUI_ATENDIMENTO);
		}
		
		if (!mostrarTurmaProfessores(turma).remove(professor))
			return;

		turmaRepository.save(turma);
		turmaRepository.flush();

	}
}
