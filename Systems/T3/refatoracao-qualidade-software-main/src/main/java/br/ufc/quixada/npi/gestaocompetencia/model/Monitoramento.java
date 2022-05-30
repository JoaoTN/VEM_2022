package br.ufc.quixada.npi.gestaocompetencia.model;

public class Monitoramento {

	private String nome;
	private String email;
	private Boolean dinamicaRealizada;
	private Long qtd;
	
	public Monitoramento() {
	}
	
	public Monitoramento(Boolean dinamicaRealizada, Long qtd) {
		this.dinamicaRealizada = dinamicaRealizada;
		this.qtd = qtd;
	}
	
	public Monitoramento(String nome, String email, Boolean dinamicaRealizada) {
		this.nome = nome;
		this.email = email;
		this.dinamicaRealizada = dinamicaRealizada;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getDinamicaRealizada() {
		return dinamicaRealizada;
	}

	public void setDinamicaRealizada(Boolean dinamicaRealizada) {
		this.dinamicaRealizada = dinamicaRealizada;
	}

	public Long getQtd() {
		return qtd;
	}

	public void setQtd(Long qtd) {
		this.qtd = qtd;
	}
	
}
