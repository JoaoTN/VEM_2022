package ufc.npi.prontuario.model;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Patologia implements Comparable<Patologia> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private Dente dente;

	@Enumerated(EnumType.STRING)
	private FaceDente face;

	@Enumerated(EnumType.STRING)
	private Local local;

	private String descricao;

	@DateTimeFormat(pattern = DATE_PATTERN)
	private Date data;

	@ManyToOne(optional = false)
	private TipoPatologia tipo;

	@ManyToOne(optional = false)
	private Odontograma odontograma;

	@JsonIgnore
	@ManyToOne(optional = false)
	private Atendimento atendimento;

	@OneToOne(cascade = CascadeType.MERGE)
	private Tratamento tratamento;

	public Patologia() {
		
	}
		
	public Patologia(Dente dente, FaceDente face, Local local, String descricao, Date data,
			Odontograma odontograma, Atendimento atendimento) {
		super();
		this.dente = dente;
		this.face = face;
		this.local = local;
		this.descricao = descricao;
		this.data = data;
		this.odontograma = odontograma;
		this.atendimento = atendimento;
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoPatologia getTipo() {
		return tipo;
	}

	public void setTipo(TipoPatologia tipo) {
		this.tipo = tipo;
	}

	@JsonIgnore
	public Odontograma getOdontograma() {
		return odontograma;
	}

	public void setOdontograma(Odontograma odontograma) {
		this.odontograma = odontograma;
	}

	public Tratamento getTratamento() {
		return tratamento;
	}

	public void setTratamento(Tratamento tratamento) {
		this.tratamento = tratamento;
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
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
		Patologia other = (Patologia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Patologia o) {
		return o.getData().compareTo(this.data);
	}

}
