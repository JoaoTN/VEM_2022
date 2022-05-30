package ufc.npi.prontuario.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Avaliacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;
	
	private Status status;

	private boolean ativa;
	
	@OneToMany(mappedBy = "avaliacao", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	private List<ItemAvaliacao> itens;
	
	public Avaliacao() {
		this.ativa = false;
		this.status = Status.EM_ANDAMENTO;
	}
	
	public enum Status {
		EM_ANDAMENTO("Em andamento"), FINALIZADA("Finalizada");

		private String descricao;

		private Status(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setItens(List<ItemAvaliacao> itens) {
		this.itens = itens;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public List<ItemAvaliacao> getItens() {
		return itens;
	}

	public void addItem(ItemAvaliacao item) {
		if (itens == null) {
			itens = new ArrayList<ItemAvaliacao>();
		}
		if (!itens.contains(item)) {
			itens.add(item);
		}
	}

	public void removeItem(ItemAvaliacao item) {
		if (itens != null) {
			itens.remove(item);
		}
	}
	
	/*public String getNomesItens() {
		String nomes = "";
		List<ItemAvaliacao> tempList = itens.subList(0, itens.size() - 1);
		for (ItemAvaliacao i: tempList)
			nomes += i.getNome() + ", ";
		nomes += itens.get(itens.size() - 1).getNome();
		return nomes;
	}*/

	public List<ItemAvaliacao> itensNaoAvaliados(Atendimento atendimento) {
		List<ItemAvaliacao> itens = this.itens;
		for(ItemAvaliacaoAtendimento i: atendimento.getAvaliacao().getItens()){
			if(itens.contains(i.getItemAvaliacao())){
				itens.remove(i.getItemAvaliacao());
			}
		}
		return itens;
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
		Avaliacao other = (Avaliacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
