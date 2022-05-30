package br.ufc.quixada.npi.gestaocompetencia.model.dto;

public class NotaDTO {

	private long notaResultado;
	private long notaAvaliacao;

	public long getNotaResultado() {
		return notaResultado;
	}

	public void setNotaResultado(long notaResultado) {
		this.notaResultado = notaResultado;
	}

	public long getNotaAvaliacao() {
		return notaAvaliacao;
	}

	public void setNotaAvaliacao(long notaAvaliacao) {
		this.notaAvaliacao = notaAvaliacao;
	}

	public void addNotaAvaliacao(long nota) {
		notaAvaliacao +=nota;
	}

	@Override
	public String toString() {
		return "NotaDTO [notaResultado=" + notaResultado + ", notaAvaliacao=" + notaAvaliacao + "]";
	}
	
	

}
