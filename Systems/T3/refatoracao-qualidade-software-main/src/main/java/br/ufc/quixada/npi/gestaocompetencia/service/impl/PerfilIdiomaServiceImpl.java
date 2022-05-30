package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.Idioma;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.PerfilIdioma;
import br.ufc.quixada.npi.gestaocompetencia.repository.PerfilIdiomaRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilIdiomaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilIdiomaServiceImpl implements PerfilIdiomaService {

    @Autowired
    private PerfilIdiomaRepository perfilIdiomaRepository;

    @Override
    public PerfilIdioma create(PerfilIdioma perfilIdioma) {
        return perfilIdiomaRepository.save(perfilIdioma);
    }

    @Override
    public List<PerfilIdioma> findAll() {
        return perfilIdiomaRepository.findAll();
    }

    @Override
    public List<PerfilIdioma> findByPerfil(Perfil perfil) {
        return perfilIdiomaRepository.findAllByPerfil(perfil);
    }

    @Override
    public PerfilIdioma findByPerfilAndIdioma(Perfil perfil, Idioma idioma) {
        return perfilIdiomaRepository.findAllByPerfilAndIdioma(perfil, idioma);
    }

    @Override
    public List<PerfilIdioma> findByIdioma(Idioma idioma) {
        return perfilIdiomaRepository.findAllByIdioma(idioma);
    }

    @Override
    public PerfilIdioma update(PerfilIdioma perfilIdioma) {
        return perfilIdiomaRepository.save(perfilIdioma);
    }

    @Override
    public void delete(PerfilIdioma perfilIdioma) {
        perfilIdiomaRepository.delete(perfilIdioma);
    }
}
