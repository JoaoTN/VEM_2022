package ufc.npi.prontuario.model;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Atendimento implements Comparable<Atendimento> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@DateTimeFormat(pattern = DATE_PATTERN)
	private Date data;

	@ManyToOne(optional = false)
	private Paciente paciente;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Aluno responsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	private Aluno ajudante;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Turma turma;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Servidor professor;

	@Enumerated(EnumType.STRING)
	private Status status;

	@OneToMany(mappedBy = "atendimento", cascade={CascadeType.ALL, CascadeType.REMOVE})
	private List<Procedimento> procedimentos;

	@OneToMany(mappedBy = "atendimento", cascade={CascadeType.ALL, CascadeType.REMOVE})
	private List<Patologia> patologias;

	@OneToOne(cascade={CascadeType.ALL, CascadeType.REMOVE})
	private AvaliacaoAtendimento avaliacao;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	@JsonIgnore
	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	@JsonIgnore
	public Aluno getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Aluno responsavel) {
		this.responsavel = responsavel;
	}

	@JsonIgnore
	public Aluno getAjudante() {
		return ajudante;
	}

	public void setAjudante(Aluno ajudante) {
		this.ajudante = ajudante;
	}

	@JsonIgnore
	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	@JsonIgnore
	public Servidor getProfessor() {
		return professor;
	}

	public void setProfessor(Servidor professor) {
		this.professor = professor;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@JsonIgnore
	public List<Procedimento> getProcedimentos() {
		return procedimentos;
	}

	public void setProcedimentos(List<Procedimento> procedimentos) {
		this.procedimentos = procedimentos;
	}

	@JsonIgnore
	public List<Patologia> getPatologias() {
		return patologias;
	}

	public void setPatologias(List<Patologia> patologias) {
		this.patologias = patologias;
	}

	public AvaliacaoAtendimento getAvaliacao() {
		return this.avaliacao;
	}

	public void setAvaliacao(AvaliacaoAtendimento avaliacao) {
		this.avaliacao = avaliacao;
	}

	public enum Status {
		EM_ANDAMENTO("Em andamento"), REALIZADO("Aguardando validação"), VALIDADO("Finalizado");

		private String descricao;

		private Status(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public boolean isValidado() {
		return status.equals(Status.VALIDADO);
	}
///////////////////////////////////////////////////////////
	public boolean isVisivel(String email) {
		boolean statusValidado = status.equals(Status.VALIDADO);
		boolean responsavelEmail = compararEmailResponsavel(email);
		
		boolean ajudanteEmail = ajudanteIgualEmail(email);
		boolean professorEmail = GetEmailUsuario.getUsuarioEmail(professor).equals(email);
		
		return statusValidado || responsavelEmail || ajudanteEmail || professorEmail;
	}
	
	public boolean compararEmailResponsavel(String email) {
		return GetEmailUsuario.getUsuarioEmail(responsavel).equals(email);
	}
	
	public boolean ajudanteIgualEmail(String email) {
		return (ajudante != null && GetEmailUsuario.getUsuarioEmail(ajudante).equals(email));
	}
//////////////////////////////////////////////////////////
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
		Atendimento other = (Atendimento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Atendimento o) {
		return o.getData().compareTo(this.data);
	}
}
