package br.ufc.quixada.npi.gestaocompetencia.model;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.SituacaoComportamento;

public class ComportamentoSearch {

	private Unidade[] unidades;

	private Mapeamento mapeamento;
	
	private SituacaoComportamento situacao;
	
	private Competencia competencia;
	
	private Boolean incluirSubUnidades;

	public ComportamentoSearch() {
	}
	
	public ComportamentoSearch(Unidade[] unidades, Mapeamento mapeamento, SituacaoComportamento situacao,
							   Competencia competencia, Boolean incluirSubUnidades) {
		this.unidades = unidades;
		this.mapeamento = mapeamento;
		this.situacao = situacao;
		this.competencia = competencia;
		this.incluirSubUnidades = incluirSubUnidades;
	}

	public Unidade[] getUnidades() {
		return unidades;
	}

	public void setUnidades(Unidade[] unidades) {
		this.unidades = unidades;
	}

	public Mapeamento getMapeamento() {
		return mapeamento;
	}

	public void setMapeamento(Mapeamento mapeamento) {
		this.mapeamento = mapeamento;
	}

	public SituacaoComportamento getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoComportamento situacao) {
		this.situacao = situacao;
	}

	public Competencia getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}

	public Boolean getIncluirSubUnidades() {
		return incluirSubUnidades;
	}

	public void setIncluirSubUnidades(Boolean incluirSubUnidades) {
		this.incluirSubUnidades = incluirSubUnidades;
	}
	
}
