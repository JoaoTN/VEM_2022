package ufc.npi.prontuario.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemAvaliacaoAtendimento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private double nota;
	
	@ManyToOne
	private ItemAvaliacao itemAvaliacao;
	
	@JsonIgnore
	@ManyToOne
	private AvaliacaoAtendimento avaliacaoAtendimento;
	
	

	public ItemAvaliacaoAtendimento(double nota, ItemAvaliacao itemAvaliacao) {
		this.nota = nota;
		this.itemAvaliacao = itemAvaliacao;
	}

	public ItemAvaliacaoAtendimento(ItemAvaliacao itemAvaliacao, AvaliacaoAtendimento avaliacaoAtendimento) {
		this.itemAvaliacao = itemAvaliacao;
		this.avaliacaoAtendimento = avaliacaoAtendimento;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}

	public AvaliacaoAtendimento getAvaliacaoAtendimento() {
		return avaliacaoAtendimento;
	}

	public void setAvaliacaoAtendimento(AvaliacaoAtendimento avaliacaoAtendimento) {
		this.avaliacaoAtendimento = avaliacaoAtendimento;
	}

	public ItemAvaliacao getItemAvaliacao() {
		return itemAvaliacao;
	}
////////////////////////////////////////////////////	
	public int getPesoItemAvaliacao() {
		return itemAvaliacao.getPeso();
	}
////////////////////////////////////////////////////	
	public void setItemAvaliacao(ItemAvaliacao itemAvaliacao) {
		this.itemAvaliacao = itemAvaliacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avaliacaoAtendimento == null) ? 0 : avaliacaoAtendimento.hashCode());
		result = prime * result + ((itemAvaliacao == null) ? 0 : itemAvaliacao.hashCode());
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
		ItemAvaliacaoAtendimento other = (ItemAvaliacaoAtendimento) obj;
		if (avaliacaoAtendimento == null) {
			if (other.avaliacaoAtendimento != null)
				return false;
		} else if (!avaliacaoAtendimento.equals(other.avaliacaoAtendimento))
			return false;
		if (itemAvaliacao == null) {
			if (other.itemAvaliacao != null)
				return false;
		} else if (!itemAvaliacao.equals(other.itemAvaliacao))
			return false;
		return true;
	}
	
	

}
