package ufc.quixada.npi.contest.util;

import ufc.quixada.npi.contest.model.Pessoa;

public class GetPessoa {
	public static Long getId(Pessoa pessoa) {
		return pessoa.getId();
	}
	
	public static String getEmail(Pessoa pessoa) {
		return pessoa.getEmail();
	}
	
}
