package br.ufc.quixada.npi.gestaocompetencia.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoExperienciaProfissional {

	PRIVADO("PRIVADO", "Cargo fora do serviço público"),
	ESTATUTARIO("ESTATUTARIO", "Cargo efetivo"),
	COMISSAO("COMISSAO", "Cargo em comissão"),
	CONFIANCA("CONFIANCA", "Cargo de confiança"),
	POLITICO("POLITICO", "Cargo político"),
	LIDERANCA("LIDERANCA", "Cargo de liderança fora do serviço público");

	private String id;
	private String descricao;

	private TipoExperienciaProfissional(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
