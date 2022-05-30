package ufc.npi.prontuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProntuarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProntuarioApplication.class, args);
	}
}
