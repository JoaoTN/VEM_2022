package ufc.npi.prontuario.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Pergunta implements Comparable<Pergunta>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String texto;
	
	private Integer ordem;

	@Enumerated(EnumType.STRING)
	private TiposPerguntas tipo;

	@ManyToOne(optional = false)
	private Anamnese anamnese;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public TiposPerguntas getTipo() {
		return tipo;
	}

	public void setTipo(TiposPerguntas tipo) {
		this.tipo = tipo;
	}
	
	@JsonIgnore
	public Anamnese getAnamnese() {
		return anamnese;
	}

	public void setAnamnese(Anamnese anamnese) {
		this.anamnese = anamnese;
	}

	
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}



	public enum TiposPerguntas {

		TEXTO("Texto"), SIM_OU_NAO("Sim/Não"), TEXTO_E_SIM_OU_NAO("Texto/Sim/Não");

		private String descricao;

		TiposPerguntas(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return this.descricao;
		}
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
		Pergunta other = (Pergunta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Pergunta outra) {
		if (this.ordem < outra.ordem) {
            return -1;
        }
        if (this.ordem > outra.ordem) {
            return 1;
        }
		return 0;
	}

}
