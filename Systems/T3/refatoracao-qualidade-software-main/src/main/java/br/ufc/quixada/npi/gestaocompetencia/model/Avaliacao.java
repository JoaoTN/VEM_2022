package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TipoAvaliacao tipo;
    
    @ManyToOne
    private Usuario avaliador;

    @ManyToOne
    private Usuario avaliado;

    @ManyToOne
    private Unidade unidade;

    @ManyToOne
    private Diagnostico diagnostico;

    @Enumerated(EnumType.STRING)
    private Perspectiva perspectiva;

    private boolean ignorada;

    private String justificativa;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "avaliacao")
    private List<ItemAvaliacao> itens;

    @JsonIgnore
    public List<ItemAvaliacao> getItensAplicaveis() {
        List<ItemAvaliacao> itensAplicaveis = new ArrayList<>();
        for (ItemAvaliacao item : this.itens) {
            if (!item.isNaoAplica()) {
                itensAplicaveis.add(item);
            }
        }
        return itensAplicaveis;
    }

    public enum TipoAvaliacao {
        AUTOAVALIACAO, CHEFIA, SUBORDINADO, PARES;
    }

    public enum Perspectiva {
        COMPORTAMENTAL, RESPONSABILIDADE;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoAvaliacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoAvaliacao tipo) {
        this.tipo = tipo;
    }

    public Usuario getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(Usuario avaliador) {
        this.avaliador = avaliador;
    }

    public Usuario getAvaliado() {
        return avaliado;
    }

    public void setAvaliado(Usuario avaliado) {
        this.avaliado = avaliado;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Perspectiva getPerspectiva() {
        return perspectiva;
    }

    public void setPerspectiva(Perspectiva perspectiva) {
        this.perspectiva = perspectiva;
    }

    public List<ItemAvaliacao> getItens() {
        return itens;
    }

    public void setIgnorada(boolean ignorada) {
        this.ignorada = ignorada;
    }

    public boolean isIgnorada() {
        return ignorada;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void addItem(ItemAvaliacao item) {
        if (itens == null) {
            itens = new ArrayList<>();
        }
        itens.add(item);
    }

    public void addAllItens(List<ItemAvaliacao> itens) {
        if (this.itens == null) {
            this.itens = new ArrayList<>();
        }
        this.itens.addAll(itens);
    }
}
