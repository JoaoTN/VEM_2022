package br.ufc.quixada.npi.gestaocompetencia.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Dificuldade {

    UM("UM", 1, "1 - Atividade está organizada ou estruturada "
    		+ "(requer aplicação básica de suas competências para realizar a atividade)"),
	DOIS("DOIS", 2, "2 - Atividade pouco organizada ou estruturada "
			+ "(requer aplicação intermediária de suas competências para realizar a atividade)"),
	TRES("TRES", 3, "3 - Atividade ainda sem organização ou estruturas definidas "
			+ "(requer aplicação máxima de suas competências para realizar a atividade)");

	private String id;
	private int valor;
	private String descricao;
	
	private Dificuldade(String id, int valor, String descricao) {
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

	public String getDescricao() { return descricao; }

	@Override
	public String toString() {
		return getId();
	}
	
}
