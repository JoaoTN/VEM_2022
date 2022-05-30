package ufc.npi.prontuario.model;

import org.springframework.security.core.GrantedAuthority;

public enum Papel implements GrantedAuthority {

	ESTUDANTE("Estudante"), PROFESSOR("Professor"), ATENDENTE("Atendente"), ADMINISTRACAO("Administração");
	
	private String descricao;
	
	private Papel(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String getAuthority() {
		return this.toString();
	}
	
	
}
