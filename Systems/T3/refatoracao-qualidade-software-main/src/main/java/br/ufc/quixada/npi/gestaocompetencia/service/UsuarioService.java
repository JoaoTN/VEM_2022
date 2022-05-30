package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;

import java.util.List;

public interface UsuarioService {
	
	Usuario update(Usuario usuario);

    Usuario findByEmail(String email);

    List<Usuario> findByUnidade(Unidade unidade);

    Integer countByUnidade(Unidade unidade);
    
    Integer countByUsuariosConcluidosUnidade(Unidade unidade);

    Boolean isChefia(Usuario usuario);

    Boolean isViceChefia(Usuario usuario);

    Boolean hasUnidadesFilhas(Usuario usuario);
}
