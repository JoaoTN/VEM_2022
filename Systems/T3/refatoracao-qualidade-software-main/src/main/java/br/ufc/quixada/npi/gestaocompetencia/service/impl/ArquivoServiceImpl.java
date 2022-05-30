package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.ufc.quixada.npi.gestaocompetencia.config.MinioConfig;
import br.ufc.quixada.npi.gestaocompetencia.config.MinioPorperties;
import br.ufc.quixada.npi.gestaocompetencia.service.ArquivoService;

@Service
public class ArquivoServiceImpl implements ArquivoService {
	
	@Autowired
	private MinioPorperties minioPorpeties;

	@Override
	public String uploadProfileImage(String userID, String base64, String contentType) throws Exception {
		MinioConfig minioConfig = new MinioConfig(minioPorpeties);
		
		String path = "public/"+userID+"/profile_image";
		long size = this.getOriginalLengthInBytes(base64);
		InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(base64)); 
		
		//Aplicando extenção para a imagem
		switch(contentType) {
			case "data:image/jpeg;base64":
				path += ".jpeg";
				break;
			case "data:image/png;base64":
				path += ".png";
				break;
			default:
				path += ".jpg";
				break;
		}
		
		return minioConfig.uploadPublicFile(path, stream, size, contentType);
	}
	
	private long getOriginalLengthInBytes(String base64) {
	    if (base64 == null || base64.length() == 0) { 
	    	return 0; 
	    }

	    int characterCount = base64.length();
	    int paddingCount =  StringUtils.countOccurrencesOf(base64, "=");
	    return (3L * (characterCount / 4)) - paddingCount;
	}

}

