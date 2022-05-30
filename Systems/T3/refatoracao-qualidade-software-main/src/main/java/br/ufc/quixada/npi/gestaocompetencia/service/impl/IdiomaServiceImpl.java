package br.ufc.quixada.npi.gestaocompetencia.service.impl;


import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.quixada.npi.gestaocompetencia.model.Idioma;

import br.ufc.quixada.npi.gestaocompetencia.repository.IdiomaRepository;

import br.ufc.quixada.npi.gestaocompetencia.service.IdiomaService;

@Service
public class IdiomaServiceImpl implements IdiomaService{

	@Autowired
	private  IdiomaRepository idiomaRepository;

	@Override
	public Idioma create(Idioma idioma) {
		return idiomaRepository.save(idioma);
	}
	
	@Override
	public List<Idioma> findAll() {
		return idiomaRepository.findAll();
	}

	@Override
	public List<Idioma> findByUsuario(Usuario usuario) {
		return idiomaRepository.findAllByUsuario(usuario);
	}

	@Override
	public Idioma update(Idioma idioma) {
		return idiomaRepository.save(idioma);
	}
	
	@Override
	public void delete(Idioma idioma) {
		idiomaRepository.delete(idioma);
	}
}
