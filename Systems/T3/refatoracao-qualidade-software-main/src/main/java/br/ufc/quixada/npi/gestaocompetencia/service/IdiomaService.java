package br.ufc.quixada.npi.gestaocompetencia.service;

import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.Idioma;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

public interface IdiomaService {

	Idioma create(Idioma idioma);

	List<Idioma> findAll();

	List<Idioma> findByUsuario(Usuario usuario);

	Idioma update(Idioma idioma);

	void delete(Idioma idioma);
}
