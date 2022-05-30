package br.ufc.quixada.npi.gestaocompetencia.model;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.SituacaoResponsabilidade;

public class ResponsabilidadeSearch {

	private Unidade[] unidades;

	private SituacaoResponsabilidade situacao;
	
	private Mapeamento mapeamento;

	public ResponsabilidadeSearch() {
	}

	public ResponsabilidadeSearch(Unidade[] unidades,
			SituacaoResponsabilidade situacao, Mapeamento mapeamento) {
		this.unidades = unidades;
		this.situacao = situacao;
		this.mapeamento = mapeamento;
	}

	public Mapeamento getMapeamento() {
		return mapeamento;
	}

	public void setMapeamento(Mapeamento mapeamento) {
		this.mapeamento = mapeamento;
	}

	public Unidade[] getUnidades() {
		return unidades;
	}

	public void setUnidades(Unidade[] unidades) {
		this.unidades = unidades;
	}

	public SituacaoResponsabilidade getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoResponsabilidade situacao) {
		this.situacao = situacao;
	}



}
