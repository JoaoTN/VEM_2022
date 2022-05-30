package br.ufc.quixada.npi.gestaocompetencia.model;

import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
public class Idioma {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	private String nome;

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Idioma idioma = (Idioma) o;
		return id.equals(idioma.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
