package br.ufc.quixada.npi.gestaocompetencia.service.impl;

import br.ufc.quixada.npi.gestaocompetencia.config.MinioConfig;
import br.ufc.quixada.npi.gestaocompetencia.config.MinioPorperties;
import br.ufc.quixada.npi.gestaocompetencia.exception.GestaoCompetenciaException;
import br.ufc.quixada.npi.gestaocompetencia.model.Perfil;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.repository.PerfilRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.PerfilService;

import io.minio.errors.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class PerfilServiceImpl implements PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

	@Autowired
	private MinioPorperties minioPorpeties;

	static final Logger LOGGER = LogManager.getLogger(PerfilServiceImpl.class.getName());

    @Override
	public Perfil create(Perfil perfil) {
    	return perfilRepository.save(perfil);
	}

	@Override
	public Perfil findByUsuario(Usuario usuario) {
		return perfilRepository.findByUsuario(usuario);
	}

	/*@Override
	private FormacaoAcademica saveWithCurriculum(FormacaoAcademica formacaoAcademica, MultipartFile curriculum) throws IOException {
		MinioConfig minioConfig = new MinioConfig(minioPorpeties);
		String path = "private/" + formacaoAcademica.getPerfil().getId() + "/curriculum.pdf";
		long size = curriculum.getSize();
		InputStream stream = curriculum.getInputStream();
		String contentType = curriculum.getContentType();

		try {
			minioConfig.uploadPrivateFile(path, stream, size, contentType);
		} catch (Exception e) {
			LOGGER.fatal("An exception occurred.", e);
		}

		formacaoAcademica.setCaminhoCurriculum(path);
		return save(formacaoAcademica);
	}*/

	/*@Override
	public InputStream getCurriculum (Perfil perfil) {
		String path = this.findPathCurriculum(perfil);

		if(path == null) {
			return null;
		} else {
			MinioConfig minioConfig = new MinioConfig(minioPorpeties);
			return minioConfig.getPrivateObject(path);
		}
	}*/

	/*@Override
	public FormacaoAcademica update(FormacaoAcademica formacaoAcademica) {
		String path = formacaoAcademicaRepository.retornarCaminhoCurriculum(formacaoAcademica.getId());
		formacaoAcademica.setCaminhoCurriculum(path);
		return formacaoAcademicaRepository.save(formacaoAcademica);
	}*/

	/*@Override
	public Perfil updateWithCurriculum(Perfil perfil, MultipartFile curriculum) throws IOException {
		String path = perfil.getPathCurriculum();

		if(path == null) {
			throw new GestaoCompetenciaException("Currriculum não cadastrado!");
		} else {
			long size = curriculum.getSize();
			InputStream stream = curriculum.getInputStream();
			String contentType = curriculum.getContentType();
			MinioConfig minioConfig = new MinioConfig(minioPorpeties);

			try {
				minioConfig.uploadPrivateFile(path, stream, size, contentType);
			} catch (Exception e) {
				LOGGER.fatal("An exception occurred.", e);
			}

			return perfilRepository.save(perfil);
		}
	}*/

	@Override
	public Perfil update(Perfil perfil) {
		return perfilRepository.save(perfil);
	}

	@Override
	public void delete(Perfil perfil) throws IOException, InvalidResponseException, RegionConflictException,
			InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
			InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
		this.deleteCurriculum(perfil);
		perfilRepository.delete(perfil);
	}

	private String findPathCurriculum(Perfil perfil) {
		return perfilRepository.findPathCurriculum(perfil);
	}

	private void deleteCurriculum(Perfil perfil) throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
		String path = this.findPathCurriculum(perfil);
		if(path != null) {
			this.deleteCurriculumByPath(path);
			perfil.setPathCurriculum(null);
			perfilRepository.save(perfil);
		}
	}

	private void deleteCurriculumByPath(String path) throws InvalidKeyException, ErrorResponseException,
			IllegalArgumentException, InsufficientDataException, InternalException, InvalidBucketNameException,
			InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException,
			RegionConflictException, IOException {
		MinioConfig minioConfig = new MinioConfig(minioPorpeties);

		try {
			minioConfig.removePrivateFile(path);
		} catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
				| InternalException | InvalidBucketNameException | InvalidResponseException | NoSuchAlgorithmException
				| ServerException | XmlParserException | RegionConflictException | IOException e) {
			LOGGER.fatal("An exception occurred.", e);
		}
	}
}
