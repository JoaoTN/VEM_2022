package ufc.quixada.npi.contest.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.service.MessageService;
import ufc.quixada.npi.contest.util.GetPessoa;

import javax.inject.Named;

@Named
public class PessoaValidator implements Validator{
	
	private static final String NOME = "nome";
	private static final String NOME_NULL = "NOME_NULL";
	private static final String EMAIL_NULL = "EMAIL_NULL";
	private static final String EMAIL = "email";
	private static final String PESSOA_NULL = "PESSOA_NULL";
	private static final String PESSOA_INVALIDA = "PESSOA_INVALIDA";
	@Autowired
	private MessageService messageService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Pessoa.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Pessoa pessoa = (Pessoa) target;
		if(pessoa != null){
			if(GetPessoa.getEmail(pessoa) == null){
				errors.rejectValue(EMAIL, EMAIL_NULL , messageService.getMessage(EMAIL_NULL));
			}
			if(getNome(pessoa) == null){
				errors.rejectValue(NOME, NOME_NULL , messageService.getMessage(NOME_NULL));
			}
		}else{
			errors.rejectValue(null, PESSOA_NULL , messageService.getMessage(PESSOA_INVALIDA));
		}
	}
	
	private String getNome(Pessoa pessoa) {
		return pessoa.getNome();
	}

}