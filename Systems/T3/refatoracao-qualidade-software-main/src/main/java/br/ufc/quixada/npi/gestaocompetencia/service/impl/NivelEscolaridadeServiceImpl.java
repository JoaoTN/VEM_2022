package br.ufc.quixada.npi.gestaocompetencia.service.impl;


import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.quixada.npi.gestaocompetencia.model.NivelEscolaridade;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.Escolaridade;
import br.ufc.quixada.npi.gestaocompetencia.repository.NivelEscolaridadeRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.NivelEscolaridadeService;
@Service
public class NivelEscolaridadeServiceImpl implements NivelEscolaridadeService {

	@Autowired
	private NivelEscolaridadeRepository nivelEscolaridadeRepository;
	
	@Override
	public NivelEscolaridade create(NivelEscolaridade nivelEscolaridade) {
		Escolaridade escolaridade = nivelEscolaridade.getEscolaridade();

		if(escolaridade != null) {
			if (escolaridade.equals(Escolaridade.ENSINO_MEDIO) || escolaridade.equals(Escolaridade.ENSINO_FUNDAMENTAL)) {
				nivelEscolaridade.setCurso("");
			}

			if (escolaridade.equals(Escolaridade.ESPECIALIZACAO_RESIDENCIA_MEDICA)) {
				nivelEscolaridade.setCurso("");
			}
		}
		
		return nivelEscolaridadeRepository.save(nivelEscolaridade);
	}

	@Override
	public List<NivelEscolaridade> findAll() {
		return nivelEscolaridadeRepository.findAll();
	}

	@Override
	public List<NivelEscolaridade> findByPerfil(Perfil perfil) {
		return nivelEscolaridadeRepository.findAllByPerfil(perfil);
	}

	@Override
	public NivelEscolaridade update(NivelEscolaridade nivelEscolaridade) {
		return this.create(nivelEscolaridade);
	}

	@Override
	public void delete(NivelEscolaridade nivelEscolaridade) {
		nivelEscolaridadeRepository.delete(nivelEscolaridade);
	}
}
