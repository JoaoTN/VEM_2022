package ufc.quixada.npi.contest.model;


public class TrabalhoProduct2 {
	private TrabalhoProduct trabalhoProduct = new TrabalhoProduct();

	public TrabalhoProduct getTrabalhoProduct() {
		return trabalhoProduct;
	}

	public Revisao getRevisao(Pessoa revisor) {
		for (Revisao revisao : trabalhoProduct.getRevisoes()) {
			if (revisao.getRevisor().equals(revisor)) {
				return revisao;
			}
		}
		return null;
	}

	public boolean isRevisor(Pessoa pessoa) {
		for (Revisao revisao : this.trabalhoProduct.getRevisoes()) {
			if (revisao.getRevisor().equals(pessoa)) {
				return true;
			}
		}
		return false;
	}
}