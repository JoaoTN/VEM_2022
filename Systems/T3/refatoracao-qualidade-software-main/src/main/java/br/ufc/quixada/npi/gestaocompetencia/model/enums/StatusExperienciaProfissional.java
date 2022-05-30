package br.ufc.quixada.npi.gestaocompetencia.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusExperienciaProfissional {

    ATUAL("ATUAL", "Atual"),
    CONCLUIDA("CONCLUIDA", "Concluída");

    private String id;
    private String descricao;

    private StatusExperienciaProfissional(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}
