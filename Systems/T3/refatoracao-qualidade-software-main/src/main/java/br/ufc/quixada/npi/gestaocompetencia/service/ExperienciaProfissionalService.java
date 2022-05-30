package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.ExperienciaProfissional;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;

import java.util.List;

public interface ExperienciaProfissionalService {

    ExperienciaProfissional create(ExperienciaProfissional experienciaProfissional);

    List<ExperienciaProfissional> findAll();

    List<ExperienciaProfissional> findByPerfil(Perfil perfil);

    ExperienciaProfissional update(ExperienciaProfissional experienciaProfissional);

    void delete(ExperienciaProfissional experienciaProfissional);
}
