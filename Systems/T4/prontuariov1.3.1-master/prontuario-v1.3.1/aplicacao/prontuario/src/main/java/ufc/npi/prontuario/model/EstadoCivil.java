package ufc.npi.prontuario.model;

public enum EstadoCivil {
    SOLTEIRO("Solteiro(a)"), CASADO("Casado(a)"), DIVORCIADO("Divorciado(a)"), VIUVO("Viúvo(a)"), SEPARADO(
            "Separado(a)"), COMPANHEIRO("Companheiro(a)");

    private String descricao;

    private EstadoCivil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}