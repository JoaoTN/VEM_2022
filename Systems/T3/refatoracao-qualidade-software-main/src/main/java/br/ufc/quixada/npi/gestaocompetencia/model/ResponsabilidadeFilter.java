package br.ufc.quixada.npi.gestaocompetencia.model;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.SituacaoResponsabilidade;

public class ResponsabilidadeFilter {
	
	private Unidade[] unidades;
	private SituacaoResponsabilidade situacao;
	private Boolean subunidades;
	
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
	public Boolean getSubunidades() {
		return subunidades;
	}
	public void setSubunidades(Boolean subunidades) {
		this.subunidades = subunidades;
	}
	
	
}
