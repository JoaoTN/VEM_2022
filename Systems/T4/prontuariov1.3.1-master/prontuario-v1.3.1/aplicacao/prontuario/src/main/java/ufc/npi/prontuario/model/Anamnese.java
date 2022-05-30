package ufc.npi.prontuario.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import ufc.npi.prontuario.repository.AnamneseRepository;

@Entity
public class Anamnese {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	private String nome;

	@Column(length = 5000)
	private String descricao;

	@Enumerated(EnumType.STRING)
	private Status status;

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	@JoinColumn(name = "anamnese_id")
	private List<Pergunta> perguntas;

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

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Pergunta> getPerguntas() {
		if (perguntas == null) {
			perguntas = new ArrayList<Pergunta>();
		}
		Collections.sort(this.perguntas);
		return this.perguntas;
	}

	public void setPerguntas(List<Pergunta> perguntas) {
		this.perguntas = perguntas;
	}

	public void addPergunta(Pergunta pergunta) {
		if (this.perguntas == null) {
			this.perguntas = new ArrayList<>();
		}

		this.perguntas.add(pergunta);
	}
	
	public boolean verificarNomeDescricao() {
		return this.getNome().trim().isEmpty() || this.getDescricao().trim().isEmpty();
	}
////////////////////////////////////////////////////////////
	public boolean findNameInRepository(AnamneseRepository anamneseRepository) {
		return anamneseRepository.findByNome(this.getNome()) != null;
	}
////////////////////////////////////////////////////////////
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
		Anamnese other = (Anamnese) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
