package br.ufc.quixada.npi.gestaocompetencia.service;

import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.NivelEscolaridade;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;

public interface NivelEscolaridadeService {

	NivelEscolaridade create(NivelEscolaridade nivelEscolaridade);

	List<NivelEscolaridade> findAll();

	List<NivelEscolaridade> findByPerfil(Perfil perfil);

	NivelEscolaridade update(NivelEscolaridade nivelEscolaridade);

	void delete(NivelEscolaridade nivelEscolaridade);
}
