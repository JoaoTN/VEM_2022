package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.NENHUM_ATENDIMENTO_ABERTO_EXCEPTION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import ufc.npi.prontuario.model.Papel;
import ufc.npi.prontuario.model.Patologia;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.TipoPatologia;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.repository.AlunoRepository;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.repository.OdontogramaRepository;
import ufc.npi.prontuario.repository.PatologiaRepository;
import ufc.npi.prontuario.repository.ServidorRepository;
import ufc.npi.prontuario.service.PatologiaService;
import ufc.npi.prontuario.service.TipoPatologiaService;

@Service
public class PatologiaServiceImpl implements PatologiaService {

	@Autowired
	private OdontogramaRepository odontogramaPatologiaRepository;

	@Autowired
	private PatologiaRepository patologiaRepository;

	@Autowired
	private TipoPatologiaService tipoPatologiaService;

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private ServidorRepository servidorRepository;
////////////////////////////////////////////////////////////////////////////////////
	public List<Patologia> salvar(String faceDente, List<Integer> idPatologias, String localString,
			Integer idOdontograma, String descricao, Aluno aluno) throws ProntuarioException {
		Odontograma odontograma = odontogramaPatologiaRepository.findOne(idOdontograma);

		List<Atendimento> atendimentos = atendimentoRepository.findAllByResponsavelOrAjudanteExist(aluno,
				odontograma.getPaciente(), Status.EM_ANDAMENTO);

		if (atendimentos.size() != 1) {
			throw new ProntuarioException(NENHUM_ATENDIMENTO_ABERTO_EXCEPTION);
		}

		FaceDente face = null;
		Dente dente = null;
		Local local = Local.valueOf(localString);

		condicionalLocalFaceDente(face, local, dente, faceDente);

		Patologia patologia = new Patologia(dente, face, local, descricao, new Date(), odontograma,
				atendimentos.get(0));
		List<Patologia> patologias = setarTiposPatologias(idPatologias, patologia);
		salvarPatologias(patologias);

		return patologias;
	}
	
	public void condicionalLocalFaceDente(FaceDente face, Local local, Dente dente, String faceDente) {
		if (local == Local.FACE) {
			face = FaceDente.valueOf(faceDente.substring(3));
			dente = Dente.valueOf("D" + faceDente.substring(0, 2));
		}else if (local == Local.DENTE) {
			dente = Dente.valueOf("D" + faceDente);
		}
	}
	///////////////////////////////////////////////////////////////////////

	private List<Patologia> setarTiposPatologias(List<Integer> idPatologias, Patologia patologia) {
		List<Patologia> patologias = new ArrayList<Patologia>();
		List<TipoPatologia> tipos = tipoPatologiaService.buscarPorIds(idPatologias);

		for (TipoPatologia tipo : tipos) {
			patologia.setTipo(tipo);
			patologias.add(patologia);
		}

		return patologias;
	}

	private void salvarPatologias(List<Patologia> patologias) {

		for (Patologia patologia : patologias) {
			patologiaRepository.save(patologia);
		}
	}

	@Override
	public void tratar(Patologia patologia) {
		patologiaRepository.saveAndFlush(patologia);
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<Patologia> buscarPatologiasOdontograma(Odontograma odontograma, Usuario usuario) {
		Aluno aluno = findOneAluno(alunoRepository, usuario);
		Servidor servidor = findOneServidor(servidorRepository, usuario);

		List<Patologia> patologias = new ArrayList<>();

		// Adicionando as patologias que o usuário logado é o responsável,
		// ajudante ou professor

		if (aluno != null) {
			patologias = patologiaRepository
					.findAllByOdontogramaAndAtendimentoResponsavelOrOdontogramaAndAtendimentoAjudante(odontograma,
							aluno, odontograma, aluno);
		} else if (servidor != null) {
			patologias = findAllByOdontogramaAndAtendimentoProfessor(patologiaRepository, odontograma, servidor);
		}
		if (usuarioGetPapeis(usuario, Papel.ADMINISTRACAO)) {
			patologias = findAllByOdontograma(patologiaRepository, odontograma);
		}

		// Se o usuário logado não fez parte do atendimento a lista está vazia,
		// e são carregadas apenas as patologias validadas
		if (patologiaIsEmpty(patologias)) {
			patologias = findAllByOdontogramaAndAtendimentoStatus(patologiaRepository, odontograma, Status.VALIDADO);

			// Se o usuário logado faz parte do atendimento então a lista não
			// está vazia e são adicionadas as patologias adicionadas
			// por outras pessoas que estão validadas
		} else {

			List<Integer> idPatologias = new ArrayList<>();
			for (Patologia patologia : patologias) {
				idPatologias.add(patologia.getId());
			}

			patologias.addAll(patologiaRepository.findAllByOdontogramaAndIdIsNotInAndAtendimentoStatus(odontograma,
					idPatologias, Status.VALIDADO));
		}

		Collections.sort(patologias);

		return patologias;
	}
	
	public Aluno findOneAluno(AlunoRepository alunoRepository, Usuario usuario) {
		return alunoRepository.findOne(mostrarIdUsuario(usuario));
	}
	
	public Integer mostrarIdUsuario(Usuario usuario) {
		return GetUsuarioId.mostrarIdUsuario(usuario);
	}
	
	public Servidor findOneServidor(ServidorRepository servidorRepository, Usuario usuario) {
		return servidorRepository.findOne(mostrarIdUsuario(usuario));
	}
	public boolean usuarioGetPapeis(Usuario usuario, Papel papel) {
		return usuario.getPapeis().contains(papel);
	}
	
	public boolean patologiaIsEmpty(List<Patologia> patologias) {
		return patologias.isEmpty();
	}
	
	public List<Patologia> findAllByOdontograma(PatologiaRepository patologiaRepository, Odontograma odontograma){
		return patologiaRepository.findAllByOdontograma(odontograma);
	}
	
	public List<Patologia>findAllByOdontogramaAndAtendimentoProfessor(PatologiaRepository patologiaRepository, Odontograma odontograma, Servidor servidor){
		return patologiaRepository.findAllByOdontogramaAndAtendimentoProfessor(odontograma, servidor);
	}
	
	public List<Patologia>findAllByOdontogramaAndAtendimentoStatus(PatologiaRepository patologiaRepository, Odontograma odontograma, Status status){
		return patologiaRepository.findAllByOdontogramaAndAtendimentoStatus(odontograma, status);
	}
////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<Patologia> buscarPatologiasTratadas(Odontograma odontograma) {
		return patologiaRepository.findByOdontogramaAndTratamentoIsNotNull(odontograma);
	}

	@Override
	public List<Patologia> buscarPatologiasDentePaciente(Odontograma odontograma, String faceDenteString,
			String denteString, Aluno aluno) {
		List<Patologia> patologias = new ArrayList<>();

		Dente dente = obterDentePorString(faceDenteString, denteString);
		FaceDente faceDente = obterFaceDentePorString(faceDenteString);

		for (Patologia patologia : odontograma.getPatologias()) {
			if (atendimentoEValidoOuPertenceAoAluno(patologia.getAtendimento(), aluno)
					&& patologiaEValida(patologia, dente, faceDente)) {
				patologias.add(patologia);
			}
		}

		return patologias;
	}

	private boolean atendimentoEValidoOuPertenceAoAluno(Atendimento atendimento, Aluno aluno) {
		return atendimento.isValidado() || atendimento.getResponsavel().equals(aluno);
	}

	private boolean patologiaEValida(Patologia patologia, Dente dente, FaceDente faceDente) {
		return patologia.getTratamento() == null && (patologia.getLocal().equals(Local.GERAL)
				|| (patologia.getDente() != null && patologia.getDente().equals(dente)
						&& (patologia.getFace() == null || patologia.getFace().equals(faceDente))));
	}

	private Dente obterDentePorString(String faceDenteString, String denteString) {
		Dente dente = null;

		if (faceDenteString != null) {
			dente = Dente.valueOf("D" + faceDenteString.substring(0, 2));
		} else if (denteString != null) {
			dente = Dente.valueOf("D" + denteString);
		}

		return dente;
	}

	private FaceDente obterFaceDentePorString(String faceDenteString) {
		FaceDente faceDente = null;

		if (faceDenteString != null) {
			faceDente = FaceDente.valueOf(faceDenteString.substring(3, 4));
		}

		return faceDente;
	}

	public void deletar(Patologia patologia) {
		patologia.getOdontograma().getPatologias().remove(patologia);
		patologia.getAtendimento().getPatologias().remove(patologia);
		patologiaRepository.delete(patologia);
	}
}
