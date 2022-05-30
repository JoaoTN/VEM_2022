package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Comissao implements Serializable {

	@Id
	private Integer id;

	@OneToOne
	@JoinColumn(name = "id")
	@MapsId
	private Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
