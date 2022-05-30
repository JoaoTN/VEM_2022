package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.ExperienciaProfissional;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienciaProfissionalRepository extends JpaRepository<ExperienciaProfissional, Integer> {

    List<ExperienciaProfissional> findAllByPerfil(Perfil perfil);
}
