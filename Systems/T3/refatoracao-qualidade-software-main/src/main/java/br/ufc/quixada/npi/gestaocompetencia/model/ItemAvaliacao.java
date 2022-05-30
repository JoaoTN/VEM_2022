package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.*;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.EscalaAvaliacao;

@Entity
public class ItemAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EscalaAvaliacao nota;

    @ManyToOne
    private Comportamento fator;

    @ManyToOne
    private Responsabilidade responsabilidade;
    
    @ManyToOne()
    private Avaliacao avaliacao;

    private boolean naoAplica;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EscalaAvaliacao getNota() {
        return nota;
    }

    public void setNota(EscalaAvaliacao nota) {
        this.nota = nota;
    }

    public Comportamento getFator() {
        return fator;
    }

    public void setFator(Comportamento fator) {
        this.fator = fator;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Responsabilidade getResponsabilidade() {
        return responsabilidade;
    }

    public void setResponsabilidade(Responsabilidade responsabilidade) {
        this.responsabilidade = responsabilidade;
    }

    public boolean isNaoAplica() {
        return naoAplica;
    }

    public void setNaoAplica(boolean naoAplica) {
        this.naoAplica = naoAplica;
    }

    public boolean isAvaliador(Usuario usuario) {
    	return avaliacao.getAvaliador().getId().equals(usuario.getId());
    }
}
