package br.ufc.quixada.npi.gestaocompetencia.model;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.EscalaAvaliacao;

public class ItemAvaliacaoSearch {
    private Avaliacao[] avaliacoes;
    private Responsabilidade[] responsabilidades;
    private Comportamento[] comportamentos;
    private EscalaAvaliacao[] notas;
    private Usuario avaliador;
    private Usuario visualizador;

    public ItemAvaliacaoSearch(){

    }

    public ItemAvaliacaoSearch(Avaliacao[] avaliacoes, Responsabilidade[] responsabilidades, Comportamento[] comportamentos,
    EscalaAvaliacao[] notas, Usuario avaliador, Usuario visualizador) {
        this.avaliacoes = avaliacoes;
        this.responsabilidades = responsabilidades;
        this.comportamentos = comportamentos;
        this.notas = notas;
        this.avaliador = avaliador;
        this.visualizador = visualizador;
    }

    public void setAvaliacoes(Avaliacao[] avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public Avaliacao[] getAvaliacoes() {
        return avaliacoes;
    }

    public void setResponsabilidade(Responsabilidade[] responsabilidades) {
        this.responsabilidades = responsabilidades;
    }

    public Responsabilidade[] getResponsabilidades() {
        return responsabilidades;
    }

    public void setComportamentos(Comportamento[] comportamentos) {
        this.comportamentos = comportamentos;
    }

    public Comportamento[] getComportamentos() {
        return comportamentos;
    }

    public void setNotas(EscalaAvaliacao[] notas) {
        this.notas = notas;
    }

    public EscalaAvaliacao[] getNotas() {
        return notas;
    }

    public void setAvaliador(Usuario avaliador) {
        this.avaliador = avaliador;
    }

    public Usuario getAvaliador() {
        return avaliador;
    }

    public void setVisualizador(Usuario visualizador) {
        this.visualizador = visualizador;
    }

    public Usuario getVisualizador() {
        return visualizador;
    }
}
