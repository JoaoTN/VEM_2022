package br.ufc.quixada.npi.gestaocompetencia.model.dto;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.EscalaAvaliacao;

public class CompetenciaAutoAvaliacaoDTO {

	private String competencia;
	private String comportamento;
	private EscalaAvaliacao avaliacao;

	public CompetenciaAutoAvaliacaoDTO(String competencia, String comportamento, EscalaAvaliacao avaliacao) {
		super();
		this.competencia = competencia;
		this.comportamento = comportamento;
		this.avaliacao = avaliacao;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public String getComportamento() {
		return comportamento;
	}

	public void setComportamento(String comportamento) {
		this.comportamento = comportamento;
	}

	public EscalaAvaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(EscalaAvaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

}
