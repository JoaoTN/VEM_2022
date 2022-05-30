package ufc.quixada.npi.contest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@SpringBootApplication()
@ComponentScan({"br.ufc.quixada.npi.ldap", "ufc.quixada.npi.contest","br.ufc.quixada.npi.service"})
@EntityScan(basePackages="ufc.quixada.npi.contest.model")
@EnableJpaRepositories("ufc.quixada.npi.contest.repository")
@EnableGlobalMethodSecurity(
		prePostEnabled = true,
		securedEnabled = true,
		jsr250Enabled = true)
public class ContestApplication extends SpringBootServletInitializer {
	private static Class<ContestApplication> applicationClass = ContestApplication.class;

	public static void main(String[] args) {
		SpringApplication.run(applicationClass, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}
		
}
