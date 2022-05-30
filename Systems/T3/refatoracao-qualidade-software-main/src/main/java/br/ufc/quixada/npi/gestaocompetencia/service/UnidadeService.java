package br.ufc.quixada.npi.gestaocompetencia.service;

import java.util.List;

import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

public interface UnidadeService {

	List<Unidade> findAll();

	List<Unidade> findAllUnidadesByGestor(Usuario usuario);

	List<Unidade> findAllSubunidadesByUnidade(Unidade unidade);

	List<Unidade> findAllWithMapeamento();
	
	List<Unidade> findByUnidadePai(Unidade unidade);

	boolean hasPermissionRead(Unidade unidade, Usuario usuario);
}