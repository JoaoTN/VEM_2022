package br.ufc.quixada.npi.gestaocompetencia.repository;

import br.ufc.quixada.npi.gestaocompetencia.model.Idioma;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.PerfilIdioma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilIdiomaRepository extends JpaRepository<PerfilIdioma, Integer> {

    List<PerfilIdioma> findAllByPerfil(Perfil perfil);

    PerfilIdioma findAllByPerfilAndIdioma(Perfil perfil, Idioma idioma);

    List<PerfilIdioma> findAllByIdioma(Idioma idioma);
}
