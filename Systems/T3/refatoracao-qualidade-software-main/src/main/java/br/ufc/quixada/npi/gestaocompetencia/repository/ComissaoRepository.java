package br.ufc.quixada.npi.gestaocompetencia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.quixada.npi.gestaocompetencia.model.Comissao;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

@Repository
public interface ComissaoRepository extends JpaRepository<Comissao, Integer>{

	Optional<Comissao> findByUsuario(Usuario usuario);
	
}
