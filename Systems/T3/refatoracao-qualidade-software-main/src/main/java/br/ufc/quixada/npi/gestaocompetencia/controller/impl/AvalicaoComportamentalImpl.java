package br.ufc.quixada.npi.gestaocompetencia.controller.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufc.quixada.npi.gestaocompetencia.service.impl.AvaliacaoComportamentalService;

@RestController
@RequestMapping("avcomportamental")
public class AvalicaoComportamentalImpl {
	
	@Autowired
	AvaliacaoComportamentalService comportamentalService;

}
