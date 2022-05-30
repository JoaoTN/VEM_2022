package br.ufc.quixada.npi.gestaocompetencia.service;

import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface PerfilService {

	Perfil create(Perfil perfil);

	Perfil findByUsuario(Usuario usuario);

	Perfil update(Perfil perfil);

	void delete(Perfil perfil) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException;
}
