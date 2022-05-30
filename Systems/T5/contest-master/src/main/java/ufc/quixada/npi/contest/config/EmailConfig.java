package ufc.quixada.npi.contest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.naming.InitialContext;
import javax.naming.NamingException;

@Configuration
public class EmailConfig {

    @Autowired
    private Environment environment;

    @Bean
    @Qualifier("emailFrom")
    @Profile("dev")
    public String getDevEmailFrom() {
        return environment.getProperty("email.from");
    }

    @Bean
    @Qualifier("emailFrom")
    @Profile("prod")
    public String getProdEmailFrom() {
        try {
            return new InitialContext().lookup("java:comp/env/email/from").toString();
        } catch (NamingException e) {
            return null;
        }
    }

}
