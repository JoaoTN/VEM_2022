package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_DISCIPLINA;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_SALVAR_DISCIPLINA_EXISTENTE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.repository.DisciplinaRepository;
import ufc.npi.prontuario.service.DisciplinaService;

@Service
public class DisciplinaServiceImpl implements DisciplinaService {

	@Autowired
	private DisciplinaRepository disciplinaRepository;
	
	/////////////////////////////////////////////
	@Override
	public void salvar(Disciplina disciplina) throws ProntuarioException {
		if(verificarDiciplinaNomeECod(disciplina)) {
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}
		if(verificarDisciplinaRepoFindNomeECodigo(disciplina)) {
			throw new ProntuarioException(ERRO_SALVAR_DISCIPLINA_EXISTENTE);
		}
		disciplinaRepository.save(disciplina);
	}
	
	
	public boolean verificarDisciplinaRepoFindNomeECodigo(Disciplina disciplina) {
		
		return disciplinaRepository.findByNome(disciplina.getNome()) != null || disciplinaRepository.findByCodigo(disciplina.getCodigo()) != null;
		
	}
		
	public boolean verificarDiciplinaNomeECod(Disciplina disciplina){
		return (disciplina.getNome().trim().isEmpty() || disciplina.getCodigo().trim().isEmpty());
	}

	//////////////////////////////////////////////
	@Override
	public List<Disciplina> buscarTudo() {
		return disciplinaRepository.findAllByOrderByNome();
	}

	@Override
	public Disciplina buscarPorId(Integer id) {
		return disciplinaRepository.findOne(id);
	}

	@Override
	public void removerDisciplina(Integer id) throws ProntuarioException {
		
		Disciplina disciplina=disciplinaRepository.findOne(id);
		if(disciplina.getTurmas().isEmpty()){
			disciplinaRepository.delete(id);
		}
		else{
			throw new ProntuarioException(ERRO_EXCLUIR_DISCIPLINA);
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void atualizar(Disciplina disciplina) throws ProntuarioException {
		if(disciplina.verificarCamposObrigatorios()) {
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}
		Disciplina discExistente = disciplinaRepository.findByNome(disciplina.getNome());
		if(verificarExistenciaDisciplina(discExistente, disciplina)) {
			throw new ProntuarioException(ERRO_SALVAR_DISCIPLINA_EXISTENTE);
		}
		discExistente = disciplinaRepository.findByCodigo(disciplina.getCodigo());
		if(verificarExistenciaDisciplina(discExistente, disciplina)) {
			throw new ProntuarioException(ERRO_SALVAR_DISCIPLINA_EXISTENTE);
		}
		disciplinaRepository.save(disciplina);
	}
	
	public boolean verificarExistenciaDisciplina(Disciplina discExistente, Disciplina disciplina) {
		return discExistente != null && !discExistente.getId().equals(disciplina.getId());
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////
}
