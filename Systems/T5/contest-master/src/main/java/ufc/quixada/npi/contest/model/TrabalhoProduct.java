package ufc.quixada.npi.contest.model;


import java.util.List;

public class TrabalhoProduct {
	private Avaliacao APROVADO = Avaliacao.APROVADO;
	private Avaliacao RESSALVAS = Avaliacao.RESSALVAS;
	private Avaliacao REPROVADO = Avaliacao.REPROVADO;
	private Avaliacao NAO_REVISADO = Avaliacao.NAO_REVISADO;
	
	private List<Revisao> revisoes;

	public List<Revisao> getRevisoes() {
		return revisoes;
	}
	
	public Revisao getRevisao(Pessoa revisor) {
		for (Revisao revisao : this.revisoes) {
			if (revisao.getRevisor().equals(revisor)) {
				return revisao;
			}
		}
		return null;
	}
	
	public boolean isRevisor(Pessoa pessoa) {
		for (Revisao revisao : this.revisoes) {
			if (revisao.getRevisor().equals(pessoa)) {
				return true;
			}
		}
		return false;
	}

	public void setRevisoes(List<Revisao> revisoes) {
		this.revisoes = revisoes;
	}

	public boolean isRevisado() {
		if (this.revisoes == null || this.revisoes.isEmpty()) {
			return false;
		}
		for (Revisao revisao : revisoes) {
			if (!revisao.isRevisado()) {
				return false;
			}
		}
		return true;
	}

	public boolean isAprovado() {
		if (this.revisoes == null || this.revisoes.isEmpty()) {
			return false;
		}
		for (Revisao revisao : this.revisoes) {
			if (!isRevisado() || !revisao.getAvaliacao().equals(APROVADO)) {
				return false;
			}
		}
		return true;
	}

	public boolean isReprovado() {
		if (this.revisoes == null || this.revisoes.isEmpty()) {
			return false;
		}
		for (Revisao revisao : this.revisoes) {
			if (isRevisado() && revisao.getAvaliacao().equals(REPROVADO)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAprovadoComRessalvas() {
		if (this.revisoes == null || this.revisoes.isEmpty()) {
			return false;
		}
		for (Revisao revisao : this.revisoes) {
			if (isRevisado() && !revisao.getAvaliacao().equals(REPROVADO)
					&& revisao.getAvaliacao().equals(RESSALVAS)) {
				return true;
			}
		}
		return false;
	}

	public Avaliacao getResultado() {
		return isAprovado() ? APROVADO
				: isAprovadoComRessalvas() ? RESSALVAS
						: isReprovado() ? REPROVADO : NAO_REVISADO;
	}

	public boolean isBestPaper() {
		for (Revisao revisao : this.revisoes) {
			if (revisao.getIndicacao()) {
				return true;
			}
		}
		return false;
	}
}