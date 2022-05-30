package ufc.npi.prontuario.model;

public enum FaceDente {

	R("Raiz"), L("Lingual/Palatal"), D("Distal"), O("Oclusal"), M("Mesial"), V("Vestibular");

	private String descricao;

	private FaceDente(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
