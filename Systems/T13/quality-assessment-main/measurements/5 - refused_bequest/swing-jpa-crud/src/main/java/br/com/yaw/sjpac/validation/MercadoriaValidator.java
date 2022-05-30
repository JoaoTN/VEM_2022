package br.com.yaw.sjpac.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import br.com.yaw.sjpac.model.Mercadoria;

/**
 * Implementa componente para validar os dados da entidade <code>Mercadoria</code>.
 * 
 * <p>A validação ocorre através do Bean Validations, mecanismo de validação padrão do Java baseado em anotações.</p>
 * 
 * @author YaW Tecnologia
 */
public class MercadoriaValidator implements Validator<Mercadoria> {
	
	private static ValidatorFactory factory;
	
	static {
		factory = buildDefaultValidatorFactory();
	}

	@Override
	public String validate(Mercadoria mercadoria) {
		StringBuilder sb = new StringBuilder();
		if (mercadoria != null) {
			Set<ConstraintViolation<Mercadoria>> constraintViolations = useValidator(mercadoria);
			if (!constraintViolations.isEmpty()) {
				sb.append("Validação da entidade Mercadoria\n");
				for (ConstraintViolation<Mercadoria> constraint: constraintViolations) {
					sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
				}
			}
		}
		return sb.toString();
	}
	
	private Set<ConstraintViolation<Mercadoria>> useValidator(Mercadoria mercadoria){
		javax.validation.Validator validator = factory.getValidator();
		Set<ConstraintViolation<Mercadoria>> constraintViolations = validator.validate(mercadoria);
		return constraintViolations;
	}
}


