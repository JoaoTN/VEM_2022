package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

@Entity
public class Unidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	private String nome;

	private String sigla;

	@JsonIgnore
	private boolean excluida;

	@ManyToOne
	@JsonIgnore
	private Usuario chefe;

	@ManyToOne
	@JsonIgnore
	private Usuario viceChefe;

	@ManyToOne
	@JsonIgnore
	private Unidade unidadePai;

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

	public boolean isExcluida() {
		return excluida;
	}

	public void setExcluida(boolean excluida) {
		this.excluida = excluida;
	}

	public Usuario getChefe() {
		return chefe;
	}

	public void setChefe(Usuario chefe) {
		this.chefe = chefe;
	}

	public Unidade getUnidadePai() {
		return unidadePai;
	}

	public void setUnidadePai(Unidade unidadePai) {
		this.unidadePai = unidadePai;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public Usuario getViceChefe() {
		return viceChefe;
	}

	public void setViceChefe(Usuario viceChefe) {
		this.viceChefe = viceChefe;
	}

	@JsonIgnore
	public boolean isChefeOrViceChefeUnidadePai(Usuario usuario) {
		return getUnidadePai().isChefe(usuario) || getUnidadePai().isViceChefe(usuario);
	}

	public boolean isChefe(Usuario usuario) {
		return usuario != null && usuario.equals(chefe);
	}

	public boolean isViceChefe(Usuario usuario) {
		return usuario != null && usuario.equals(viceChefe);
	}
	
	public boolean hasPermissionCRUD(Usuario usuario) {	
		return isChefe(usuario) || isViceChefe(usuario) || (getUnidadePai() != null && isChefeOrViceChefeUnidadePai(usuario));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Unidade unidade = (Unidade) o;
		return id.equals(unidade.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
