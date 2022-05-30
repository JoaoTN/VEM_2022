package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.quixada.npi.gestaocompetencia.model.Comissao;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.repository.ComissaoRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.ComissaoService;

@Service
public class ComissaoServiceImpl implements ComissaoService{

	@Autowired
	ComissaoRepository comissaoRepository;
	
	@Override
	public boolean isMembroComissao(Usuario usuario) {
		Optional<Comissao> comissao = comissaoRepository.findByUsuario(usuario);
		
		return comissao.isPresent();
	}

}
