package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.Idioma;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.PerfilIdioma;

import java.util.List;

public interface PerfilIdiomaService {

    PerfilIdioma create(PerfilIdioma perfilIdioma);

    List<PerfilIdioma> findAll();

    List<PerfilIdioma> findByPerfil(Perfil perfil);

    PerfilIdioma findByPerfilAndIdioma(Perfil perfil, Idioma idioma);

    List<PerfilIdioma> findByIdioma(Idioma idioma);

    PerfilIdioma update(PerfilIdioma perfilIdioma);

    void delete(PerfilIdioma perfilIdioma);
}
