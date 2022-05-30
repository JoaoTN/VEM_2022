package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.service.ArquivoService;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("arquivo")
public class ArquivoController {
	
	@Autowired
	private ArquivoService arquivoService;
	
	static final Logger LOGGER = LogManager.getLogger(ArquivoController.class.getName());

	@PutMapping("/imagemPerfil")
    public ResponseEntity<String> find(@AuthenticationPrincipal Usuario usuario, @RequestBody Map<String, String> body) {
    	
    	String imageValue = body.get("imageValue");
		
		//Separando tipo do conteúdo. Ex: data:image/jpeg;base64
    	String[] base64Parts = imageValue.split(",");
    	String contentBase64 = base64Parts[1];
		String contentType = base64Parts[0];

		try {
			String url = arquivoService.uploadProfileImage(usuario.getId().toString(), contentBase64, contentType);
			return ResponseEntity.ok(url);
		} catch(Exception e) {
			LOGGER.fatal("An exception occurred.", e);
			return ResponseEntity.status(500).build();
		}
    }
}
