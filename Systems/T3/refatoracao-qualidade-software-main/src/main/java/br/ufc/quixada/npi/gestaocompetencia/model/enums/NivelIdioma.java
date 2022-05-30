package br.ufc.quixada.npi.gestaocompetencia.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NivelIdioma {
	
	POUCO("POUCO", "Pouco", 33),
	RAZOAVEL("RAZOAVEL", "Razoável", 66),
	BOM("BOM", "Bom", 100);

	private String id;
	private String descricao;
	private int valor;

	private NivelIdioma(String id, String descricao, int valor) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
	}

	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getValor() { return this.valor; }
}
