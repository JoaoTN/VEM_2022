package ufc.npi.prontuario.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Procedimento implements Comparable<Procedimento> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private Dente dente;

	@Enumerated(EnumType.STRING)
	private FaceDente face;

	@Enumerated(EnumType.STRING)
	private Local local;

	@ManyToOne(optional = false)
	private TipoProcedimento tipoProcedimento;

	private String descricao;

	@ManyToOne(optional = false)
	private Atendimento atendimento;

	@ManyToOne
	private Odontograma odontograma;

	private Boolean preExistente;
	
	public Procedimento(Dente dente, FaceDente face, Local local, TipoProcedimento tipoProcedimento, String descricao,
			Atendimento atendimento, Odontograma odontograma, Boolean preExistente) {
		this.dente = dente;
		this.face = face;
		this.local = local;
		this.tipoProcedimento = tipoProcedimento;
		this.descricao = descricao;
		this.atendimento = atendimento;
		this.odontograma = odontograma;
		this.preExistente = preExistente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Dente getDente() {
		return dente;
	}

	public void setDente(Dente dente) {
		this.dente = dente;
	}

	public FaceDente getFace() {
		return face;
	}

	public void setFace(FaceDente face) {
		this.face = face;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoProcedimento getTipoProcedimento() {
		return tipoProcedimento;
	}

	public void setTipoProcedimento(TipoProcedimento tipoProcedimento) {
		this.tipoProcedimento = tipoProcedimento;
	}

	@JsonIgnore
	public Atendimento getAtendimento() {
		return atendimento;
	}
////////////////////////////////
	public Date getDataAtendimento() {
		return atendimento.getData();
	}
////////////////////////////////
	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	@JsonIgnore
	public Odontograma getOdontograma() {
		return odontograma;
	}

	public void setOdontograma(Odontograma odontograma) {
		this.odontograma = odontograma;
	}

	public Boolean getPreExistente() {
		return preExistente;
	}

	public void setPreExistente(Boolean preExistente) {
		this.preExistente = preExistente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Procedimento other = (Procedimento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Procedimento o) {
		return this.atendimento.compareTo(o.getAtendimento());
	}

}
