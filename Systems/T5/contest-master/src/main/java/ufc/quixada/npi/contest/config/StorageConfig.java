package ufc.quixada.npi.contest.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${minio.serverName}")
    private String serverName;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient getMinioClient() {
        try {
            return new MinioClient(this.serverName, this.accessKey, this.secretKey);
        } catch (InvalidEndpointException | InvalidPortException e) {
            return null;
        }
    }
}
