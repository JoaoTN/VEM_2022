package br.ufc.quixada.npi.gestaocompetencia.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import br.ufc.quixada.npi.gestaocompetencia.exception.FileUploadException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.PropertySource;
import io.minio.BucketExistsArgs;

import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.RegionConflictException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@PropertySource("classpath:application.properties")
public class MinioConfig {

	private String bucketName;
	private String accessKey;
	private String secretKey;
	private String endpoint;
	private String port;
	
	private MinioClient minioClient;

	static final Logger LOGGER = LogManager.getLogger(MinioConfig.class.getName());

	public MinioConfig(MinioPorperties minioPorpeties) {
		this.bucketName = minioPorpeties.getBucketName();
		this.accessKey = minioPorpeties.getAccesskey();
		this.secretKey = minioPorpeties.getSecretkey();
		this.endpoint = minioPorpeties.getEndpoint();
		this.port = minioPorpeties.getPort();
		
		this.minioClient = this.initClient();
	}
	
	private MinioClient initClient() {
		return MinioClient.builder().endpoint(this.endpoint,Integer.parseInt(this.port), false).credentials(this.accessKey, this.secretKey).build();
	}
	
	//Para dar acesso de download a pasta de destino das imagens de perfil é preciso setar a pasta como download
	//Se não tiver instanciando o mvnio, segue exempo: mc config host add myminio http://192.168.0.103:9000 <access_key> <secret_key>
	//e então: mc policy set download myminio/<bucket>/<pasta>

	public String uploadPublicFile(String path, InputStream stream, long size, String contentType) throws Exception {
		try {
			
			if (!this.checkBucketExistence()) {
				this.createBucket();
			}
			
			PutObjectArgs objArgs = PutObjectArgs.builder()
					.bucket(this.bucketName)
					.object(path)
					.stream(stream, size, -1)
					.contentType(contentType)
					.build();
			
			this.minioClient.putObject(objArgs);
				    
		    return "http://"+this.endpoint+":"+this.port+"/"+this.bucketName+"/"+path;

		} catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
				| InternalException | InvalidBucketNameException | InvalidResponseException | NoSuchAlgorithmException
				| ServerException | XmlParserException | RegionConflictException | IOException e) {
			LOGGER.fatal("An exception occurred.", e);
			throw new FileUploadException(e.getMessage());
		}
	}
	
	public boolean uploadPrivateFile(String path,
						 InputStream stream, long size, String contentType) throws Exception {
		try {
			if (!this.checkBucketExistence()) {
				this.createBucket();
			}

			PutObjectArgs objArgs = PutObjectArgs.builder()
					.bucket(this.bucketName)
					.object(path)
					.stream(stream, size, -1)
					.contentType(contentType)
					.build();
			this.minioClient.putObject(objArgs);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}




	public void removePrivateFile(String path) throws InvalidKeyException, ErrorResponseException, IllegalArgumentException, InsufficientDataException, InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, RegionConflictException, IOException {

		try {

			if (!this.checkBucketExistence()) {
				this.createBucket();
			}

			RemoveObjectArgs rmObjArgs = RemoveObjectArgs.builder()
					.bucket(this.bucketName)
					.object(path)
					.build();
			this.minioClient.removeObject(rmObjArgs);

		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	public InputStream getPrivateObject(String path) {

		try {
			if (!this.checkBucketExistence()) {
				this.createBucket();
			}
			GetObjectArgs getObjArgs = GetObjectArgs.builder()
			.bucket(bucketName)
			.object(path)
			.build();

			InputStream stream = this.minioClient.getObject(getObjArgs);
			return stream;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;


	}

	private boolean checkBucketExistence() throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IOException {
		BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(this.bucketName).build();
		return  minioClient.bucketExists(bucketExistsArgs);
	}
	
	private void createBucket() throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidBucketNameException, InvalidResponseException, NoSuchAlgorithmException, RegionConflictException, ServerException, XmlParserException, IOException {
		minioClient.makeBucket(MakeBucketArgs.builder().bucket(this.bucketName).build());
	}
	
}
