package br.ufc.quixada.npi.gestaocompetencia.repository;

import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import br.ufc.quixada.npi.gestaocompetencia.model.NivelEscolaridade;

public interface NivelEscolaridadeRepository extends JpaRepository<NivelEscolaridade, Integer> {

	List<NivelEscolaridade> findAllByPerfil(Perfil perfil);
}
