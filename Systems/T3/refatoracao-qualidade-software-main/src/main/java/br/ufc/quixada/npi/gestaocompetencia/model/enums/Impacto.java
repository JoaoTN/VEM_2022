package br.ufc.quixada.npi.gestaocompetencia.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Impacto {
    UM("UM", 1, "1 - Fica limitado ao setor"),
	DOIS("DOIS", 2, "2 - Afeta várias áreas, mas é administrável"),
	TRES("TRES", 3, "3 - Ultrapassa os portões da instituição");
	
	private String id;
	private int valor;
	private String descricao;

	private Impacto(String id, int valor, String descricao) {
		this.id = id;
		this.valor = valor;
		this.descricao = descricao;
	}
	
	public String getId() {
		return id;
	}
	
	public int getValor() {
		return valor;
	}

	public String getDescricao() { return this.descricao; }
	
}
