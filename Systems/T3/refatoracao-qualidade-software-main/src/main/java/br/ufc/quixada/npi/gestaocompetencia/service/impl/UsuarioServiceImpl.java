package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.repository.UsuarioRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> findByUnidade(Unidade unidade) {
		return usuarioRepository.findByUnidade(unidade);
	}

	@Override
	public Integer countByUnidade(Unidade unidade) {
		Integer qtd = usuarioRepository.countByUnidade(unidade);
		return qtd != null ? qtd : 0;
	}

	@Override
	public Integer countByUsuariosConcluidosUnidade(Unidade unidade) {
		Integer qtd = usuarioRepository.countByUsuariosConcluidosUnidade(unidade);
		return qtd != null ? qtd : 0;
	}

	@Override
	public Boolean isChefia(Usuario usuario) {
		return usuarioRepository.isChefia(usuario);
	}

	@Override
	public Boolean isViceChefia(Usuario usuario) {
		return usuarioRepository.isViceChefia(usuario);
	}

	@Override
	public Boolean hasUnidadesFilhas(Usuario usuario) {
		return usuarioRepository.hasUnidadesFilhas(usuario);
	}

	@Override
	public Usuario update(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
}
