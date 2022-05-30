package br.ufc.quixada.npi.gestaocompetencia.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusNivelEscolaridade {

    INCOMPLETO("INCOMPLETO", "Incompleto"),
    EM_ANDAMENTO("EM_ANDAMENTO", "Em andamento"),
    CONCLUIDO("CONCLUIDO", "Concluído");

    private String id;
    private String descricao;

    private StatusNivelEscolaridade(String id, String descricao) {
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
