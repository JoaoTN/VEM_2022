package ufc.quixada.npi.contest.model;

public enum Avaliacao {
	APROVADO("Aprovado"), RESSALVAS("Ressalvas"),
	REPROVADO("Reprovado"), MODERACAO("Em moderação"), NAO_REVISADO("Não revisado");

	private String descricao;

	Avaliacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}
}
