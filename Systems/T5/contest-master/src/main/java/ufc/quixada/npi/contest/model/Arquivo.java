package ufc.quixada.npi.contest.model;

import javax.persistence.*;
import java.io.InputStream;
import java.util.Objects;

@Entity
public class Arquivo {

    @Id
    @SequenceGenerator(name = "arquivo_id_seq", sequenceName = "arquivo_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arquivo_id_seq")
    private Integer id;

    private String nome;

    private String formato;

    @Transient
    private InputStream conteudo;

    public Arquivo() {
    }

    public Arquivo(String nome, String formato) {
        this.nome = nome;
        this.formato = formato;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public InputStream getConteudo() {
        return conteudo;
    }

    public void setConteudo(InputStream conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arquivo arquivo = (Arquivo) o;
        return id.equals(arquivo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
