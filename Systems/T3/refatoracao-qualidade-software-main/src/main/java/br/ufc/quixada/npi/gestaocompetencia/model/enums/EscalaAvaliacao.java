package br.ufc.quixada.npi.gestaocompetencia.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EscalaAvaliacao {

    SEMPRE("SEMPRE","SEMPRE", 4, 100, 5),
    MUITAS_VEZES("MUITAS_VEZES","MUITAS VEZES", 3, 75, 4),
    AS_VEZES("AS_VEZES", "ÀS VEZES", 2, 50, 3),
    RARAMENTE("RARAMENTE", "RARAMENTE", 1, 25, 2),
    NUNCA("NUNCA", "NUNCA", 0, 0, 1),

    EXCELENTE("EXCELENTE", "EXCELENTE", 4, 100, 5),
    BOM("BOM", "BOM", 3, 75, 4),
    REGULAR("REGULAR", "REGULAR", 2, 50, 3),
    INSUFICIENTE("INSUFICIENTE", "INSUFICIENTE", 1, 25, 2),
    NAO_EXECUTOU("NAO_EXECUTOU", "NÃO EXECUTOU", 0, 0, 1);

    private String id;
    private String descricao;
    private int valor;
    private int escala;
    private int rating;

    private EscalaAvaliacao(String id, String descricao, int valor, int escala, int rating) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.escala = escala;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getValor() {
        return valor;
    }

    public int getEscala() {
        return escala;
    }

    public int getRating() {
        return rating;
    }
}
