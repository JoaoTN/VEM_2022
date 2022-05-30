package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.PerfilPreferencia;
import br.ufc.quixada.npi.gestaocompetencia.model.Preferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilPreferenciaRepository extends JpaRepository<PerfilPreferencia, Integer> {

    List<PerfilPreferencia> findByPerfil(Perfil perfil);

    PerfilPreferencia findByPerfilAndPreferencia(Perfil perfil, Preferencia preferencia);

    List<PerfilPreferencia> findByPreferencia(Preferencia preferencia);
}
