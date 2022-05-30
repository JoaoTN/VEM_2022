package ufc.npi.prontuario.model;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class AvaliacaoAtendimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String observacao;

	@DateTimeFormat(pattern = DATE_PATTERN)
	private Date data;

	@ManyToOne
	private Avaliacao avaliacao;
	
	public AvaliacaoAtendimento() {}
	
	public AvaliacaoAtendimento(String observacao, Avaliacao avaliacao) {
		this.observacao = observacao;
		this.avaliacao = avaliacao;
		this.itens = new ArrayList<>();
	}
	
	@OneToMany(mappedBy = "avaliacaoAtendimento", cascade = { CascadeType.ALL })
	private List<ItemAvaliacaoAtendimento> itens;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public List<ItemAvaliacaoAtendimento> getItens() {
		if (this.itens == null) {
			itens = new ArrayList<ItemAvaliacaoAtendimento>();
			return itens;
		}
		return itens;
	}
	
	public void setItens(List<ItemAvaliacaoAtendimento> itens) {
		this.itens = itens;
	}

	public void addItem(ItemAvaliacaoAtendimento item) {
		if (itens == null) {
			itens = new ArrayList<ItemAvaliacaoAtendimento>();
		}
		if (!itens.contains(item)) {
			itens.add(item);
		}
	}

	public void removeItem(ItemAvaliacaoAtendimento item) {
		if (itens != null) {
			itens.remove(item);
		}
	}
//////////////////////////////////////////////////////////////////
	public Double getMedia() {
		String resultado = "0.0";
		if (!this.itens.isEmpty()) {
			Double notas = 0.0;
			Double pesos = 0.0;
			for (ItemAvaliacaoAtendimento item : this.itens) {
				notas += calculoNotaPesoItem(item);
				pesos += avaliacaoPesoItem(item);
			}
			resultado = new DecimalFormat("#.##").format(notas / pesos);
		}

		return Double.valueOf(resultado.replaceAll(",", "."));

	}
	
	public Double calculoNotaPesoItem(ItemAvaliacaoAtendimento item) {
		return item.getNota() * item.getPesoItemAvaliacao();
	}
	
	public int avaliacaoPesoItem(ItemAvaliacaoAtendimento item) {
		return item.getItemAvaliacao().getPeso();
	}
	
////////////////////////////////////////////////////////////////////////
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
		AvaliacaoAtendimento other = (AvaliacaoAtendimento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
