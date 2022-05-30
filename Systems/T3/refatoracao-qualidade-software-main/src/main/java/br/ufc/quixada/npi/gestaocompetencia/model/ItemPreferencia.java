package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ItemPreferencia {

    @Id
    @GeneratedValue
    private Integer id;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "preferencia_id")
    private Preferencia preferencia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Preferencia getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(Preferencia preferencia) {
        this.preferencia = preferencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPreferencia that = (ItemPreferencia) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemPreferencia{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", preferencia=" + preferencia.getNome() +
                '}';
    }
}
