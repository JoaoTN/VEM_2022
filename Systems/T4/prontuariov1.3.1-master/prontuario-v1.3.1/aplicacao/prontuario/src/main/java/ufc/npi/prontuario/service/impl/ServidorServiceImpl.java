package ufc.npi.prontuario.service.impl;


import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_SERVIDOR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_SERVIDOR_PAPEL;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ADICIONAR_SERVIDOR_VAZIO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_SERVIDOR;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.GetEmailUsuario;
import ufc.npi.prontuario.model.GetMatriculaUsuario;
import ufc.npi.prontuario.model.GetUsuarioId;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.model.SetSenhaUsuario;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.repository.ServidorRepository;
import ufc.npi.prontuario.repository.TurmaRepository;
import ufc.npi.prontuario.repository.UsuarioRepository;
import ufc.npi.prontuario.service.ServidorService;

@Service
public class ServidorServiceImpl implements ServidorService {

	@Autowired
	private ServidorRepository servidorRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	@Autowired
	private TurmaRepository turmaRepository;

	@Override
	public void salvar(Servidor servidor) throws ProntuarioException {
		if(verificarValidadeDoServidor(servidor)){
			throw new ProntuarioException(ERRO_ADICIONAR_SERVIDOR_VAZIO);
		}
		
		if (verificarEmailMatricula(servidor)) {
			throw new ProntuarioException(ERRO_ADICIONAR_SERVIDOR);
		}
				
		if(verificarServidorTemPapeis(servidor)){
			throw new ProntuarioException(ERRO_ADICIONAR_SERVIDOR_PAPEL);
		}

		SetSenhaUsuario.setUsuarioSenha(servidor,GetMatriculaUsuario.getUsuarioMatricula(servidor));
		servidor.encodePassword();
		servidorRepository.save(servidor);
	}

	private boolean verificarValidadeDoServidor(Servidor servidor) {
		return servidor.getNome().replaceAll(" ", "").length() == 0;
	}

	private boolean verificarEmailMatricula(Servidor servidor) {
		return usuarioRepository.findByEmail(GetEmailUsuario.getUsuarioEmail(servidor)) != null || usuarioRepository.findByMatricula(GetMatriculaUsuario.getUsuarioMatricula(servidor)) != null;
	}

	private boolean verificarServidorTemPapeis(Servidor servidor) {
		return servidor.getPapeis().size() == 0;
	}

	public void atualizar(Servidor servidor) throws ProntuarioException {
		if(GetUsuarioId.mostrarIdUsuario(servidor) == null || servidor.getNome().trim().isEmpty() || GetEmailUsuario.getUsuarioEmail(servidor).trim().isEmpty() || GetMatriculaUsuario.getUsuarioMatricula(servidor).trim().isEmpty()) {
			throw new ProntuarioException(ERRO_CAMPOS_OBRIGATORIOS);
		}
		Usuario aux = usuarioRepository.findByEmail(GetEmailUsuario.getUsuarioEmail(servidor));
		if(aux != null && !GetUsuarioId.mostrarIdUsuario(aux).equals(GetUsuarioId.mostrarIdUsuario(servidor))) {
			throw new ProntuarioException(ERRO_ADICIONAR_SERVIDOR);
		}
		aux = usuarioRepository.findByMatricula(GetMatriculaUsuario.getUsuarioMatricula(servidor));
		if(aux != null && !GetUsuarioId.mostrarIdUsuario(aux).equals(GetUsuarioId.mostrarIdUsuario(servidor))) {
			throw new ProntuarioException(ERRO_ADICIONAR_SERVIDOR);
		}
		
		if(servidor.getPapeis().size() == 0){
			throw new ProntuarioException(ERRO_ADICIONAR_SERVIDOR_PAPEL);
		}
		
		if(servidor.getNome().replaceAll(" ", "").length() == 0){
			throw new ProntuarioException(ERRO_ADICIONAR_SERVIDOR_VAZIO);
		}
		
		

		SetSenhaUsuario.setUsuarioSenha(servidor,servidorRepository.findOne(GetUsuarioId.mostrarIdUsuario(servidor)).getSenha());
		servidorRepository.save(servidor);
	}

	/*@Override
	public List<Servidor> buscarTudo() {
		return servidorRepository.findAllByOrderByNome();
	}*/

	@Override
	public List<Servidor> buscarProfessores() {
		return servidorRepository.findAll();
	}

	@Override
	public Servidor buscarPorId(Integer id) {
		return servidorRepository.findOne(id);
	}

	@Override
	public void removerServidor(Integer id) throws ProntuarioException {
		Servidor professor = servidorRepository.findOne(id);
		if (turmaRepository.findAllByProfessores(professor).isEmpty()
				&& atendimentoRepository.findAllByProfessor(professor).isEmpty()) {
			servidorRepository.delete(id);
		} else {
			throw new ProntuarioException(ERRO_EXCLUIR_SERVIDOR);
		}
	}

}
