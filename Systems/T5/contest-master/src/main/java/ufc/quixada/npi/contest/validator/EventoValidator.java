package ufc.quixada.npi.contest.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.service.MessageService;

import java.util.Date;

import javax.inject.Named;


@Named
public class EventoValidator implements Validator{
	
	private static final String ERRO_DATA_SUBMISSAO_FINAL = "ERRO_DATA_SUBMISSAO_FINAL";
	private static final String ERRO_DATA_REVISAO_FINAL = "ERRO_DATA_REVISAO_FINAL";
	private static final String ERRO_DATA_REVISAO_INICIAL = "ERRO_DATA_REVISAO_INICIAL";
	private static final String ERRO_DATA_SUBMISSAO_INICIAL = "ERRO_DATA_SUBMISSAO_INICIAL";
	private static final String ERRO_CAMPOS_EVENTO_NULL = "ERRO_CAMPOS_EVENTO_NULL";

	@Autowired
	private MessageService messageService;
	
	private static final String EVENTO_CAMPO_NULL="evento";
	private static final String PRAZO_REVISAO_INICIAL="prazoRevisaoInicial";
	private static final String PRAZO_REVISAO_FINAL="prazoRevisaoFinal";
	private static final String PRAZO_SUBMISSAO_INICIAL="prazoSubmissaoInicial";
	private static final String PRAZO_SUBMISSAO_FINAL="prazoSubmissaoInicial";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Evento.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object object, Errors erros) {
		Evento evento = (Evento) object;
		if(datasVisibilidadeOuventoNull(evento)){
			addError(erros, null, EVENTO_CAMPO_NULL, ERRO_CAMPOS_EVENTO_NULL);
		}else{
			if (haErrosPrazoSubmissaoInicial(evento)) {
				addError(erros, PRAZO_SUBMISSAO_INICIAL, PRAZO_SUBMISSAO_INICIAL, ERRO_DATA_SUBMISSAO_INICIAL);
			}
			if(haErrosPrazoRevisaoInicial(evento)){
				addError(erros, PRAZO_REVISAO_INICIAL, PRAZO_REVISAO_INICIAL, ERRO_DATA_REVISAO_INICIAL);
			}
			if(haErrosPrazoRevisaoFinal(evento)){
				addError(erros, PRAZO_REVISAO_FINAL, PRAZO_REVISAO_FINAL, ERRO_DATA_REVISAO_FINAL);
			}
			if(haErrosPrazoSubmissaoFinal(evento)){
				addError(erros, PRAZO_SUBMISSAO_FINAL, PRAZO_SUBMISSAO_FINAL, ERRO_DATA_SUBMISSAO_FINAL);
			}
		}
	}
	
	private void addError(Errors erros, String field, String errorCode, String defaultMessage) {
		erros.rejectValue(field, errorCode, getMessage(defaultMessage));
	}
	
	private String getMessage(String chave) {
		return messageService.getMessage(chave);
	}

	public boolean datasVisibilidadeOuventoNull(Evento evento){
		return (evento == null || getPrazoNotificacao(evento) == null || getTerminoSubmissao(evento) == null
			|| getCameraReady(evento) == null || getInicioSubmissao(evento) == null
			|| evento.getVisibilidade() == null);
	}
	
	public boolean haErrosPrazoSubmissaoInicial(Evento evento){
		Date inicioSubmissao = getInicioSubmissao(evento); 
		return inicioSubmissao.after(getTerminoSubmissao(evento))
				|| inicioSubmissao.after(getPrazoNotificacao(evento))
				|| inicioSubmissao.after(getCameraReady(evento));
	}
	private boolean haErrosPrazoRevisaoInicial(Evento evento) {
		Date terminoSubmissao = getTerminoSubmissao(evento);
		return terminoSubmissao.after(getPrazoNotificacao(evento))
				|| terminoSubmissao.after(getCameraReady(evento))
				|| terminoSubmissao.before(getInicioSubmissao(evento));
	}
	

	private boolean haErrosPrazoSubmissaoFinal(Evento evento) {
		Date cameraReady = getCameraReady(evento);
		return cameraReady.before(getPrazoNotificacao(evento))
				|| cameraReady.before(getTerminoSubmissao(evento))
				|| cameraReady.before(getInicioSubmissao(evento));
	}

	private boolean haErrosPrazoRevisaoFinal(Evento evento) {
		Date prazoNotificacao = getPrazoNotificacao(evento);
		return prazoNotificacao.after(getCameraReady(evento))
				|| prazoNotificacao.before(getTerminoSubmissao(evento))
				|| prazoNotificacao.before(getInicioSubmissao(evento));
	}
	
	private Date getInicioSubmissao(Evento evento) {
		return evento.getInicioSubmissao();
	}
	
	private Date getTerminoSubmissao(Evento evento) {
		return evento.getTerminoSubmissao();
	}
	
	private Date getPrazoNotificacao(Evento evento) {
		return evento.getPrazoNotificacao();
	}
	
	private Date getCameraReady(Evento evento) {
		return evento.getCameraReady();
	}
}