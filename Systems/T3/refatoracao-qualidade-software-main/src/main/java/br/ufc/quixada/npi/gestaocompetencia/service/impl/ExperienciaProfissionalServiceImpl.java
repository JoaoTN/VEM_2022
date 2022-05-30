package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.ExperienciaProfissional;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.repository.ExperienciaProfissionalRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.ExperienciaProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienciaProfissionalServiceImpl implements ExperienciaProfissionalService {

    @Autowired
    private ExperienciaProfissionalRepository experienciaProfissionalRepository;

    @Override
    public ExperienciaProfissional create(ExperienciaProfissional experienciaProfissional) {
        return experienciaProfissionalRepository.save(experienciaProfissional);
    }

    @Override
    public List<ExperienciaProfissional> findAll() {
        return experienciaProfissionalRepository.findAll();
    }

    @Override
    public List<ExperienciaProfissional> findByPerfil(Perfil perfil) {
        return experienciaProfissionalRepository.findAllByPerfil(perfil);
    }

    @Override
    public ExperienciaProfissional update(ExperienciaProfissional experienciaProfissional) {
        return experienciaProfissionalRepository.save(experienciaProfissional);
    }

    @Override
    public void delete(ExperienciaProfissional experienciaProfissional) {
        experienciaProfissionalRepository.delete(experienciaProfissional);
    }
}
