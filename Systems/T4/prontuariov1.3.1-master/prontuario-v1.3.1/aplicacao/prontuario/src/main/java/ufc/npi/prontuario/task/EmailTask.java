package ufc.npi.prontuario.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ufc.npi.prontuario.service.EmailService;

@Component
public class EmailTask {
	
	@Autowired
	private EmailService emailService;

	@Scheduled(cron = "0 0 8 * * ?")
    public void executarAs8DaManha() {
		emailService.notificarAtendimentoAndamento();
		emailService.notificarAtendimentoAvaliacao();
    }
}
