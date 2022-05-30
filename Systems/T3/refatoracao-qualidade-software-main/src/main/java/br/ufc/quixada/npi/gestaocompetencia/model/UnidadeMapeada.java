package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UnidadeMapeada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Unidade unidade;

    @ManyToMany
    private List<Unidade> unidades;

    @ManyToOne
    private Mapeamento mapeamento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Unidade> getUnidades() {
        return unidades;
    }

    public void addUnidade(Unidade unidade) {
        if (unidades == null) {
            unidades = new ArrayList<>();
        }
        this.unidades.add(unidade);
    }

    public void addAllUnidades(List<Unidade> unidades) {
        if (this.unidades == null) {
            this.unidades = new ArrayList<>();
        }
        this.unidades.addAll(unidades);
    }

    public Mapeamento getMapeamento() {
        return mapeamento;
    }

    public void setMapeamento(Mapeamento mapeamento) {
        this.mapeamento = mapeamento;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
}
