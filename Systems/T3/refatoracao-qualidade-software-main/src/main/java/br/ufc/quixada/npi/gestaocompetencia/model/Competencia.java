
 package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoCompetencia;

import java.util.Objects;

@Entity
public class Competencia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	private String nome;

	@Enumerated(EnumType.STRING)
	private TipoCompetencia tipo;
	
	private String descricao;

	@JsonIgnore
	private boolean excluida;
	
	private boolean validada;

	@ManyToOne
	private Unidade unidade;

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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoCompetencia getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompetencia tipo) {
		this.tipo = tipo;
	}

	public boolean isExcluida() {
		return excluida;
	}

	public void setExcluida(boolean excluida) {
		this.excluida = excluida;
	}

	public boolean isValidada() {
		return validada;
	}

	public void setValidada(boolean validada) {
		this.validada = validada;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Competencia that = (Competencia) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Competencia [id=" + id + ", nome=" + nome + "]";
	}
}
