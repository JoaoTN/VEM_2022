package br.ufc.quixada.npi.gestaocompetencia.model;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoAreaPerfil;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class AreaPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoAreaPerfil tipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

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

    public TipoAreaPerfil getTipo() {
        return tipo;
    }

    public void setTipo(TipoAreaPerfil tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
