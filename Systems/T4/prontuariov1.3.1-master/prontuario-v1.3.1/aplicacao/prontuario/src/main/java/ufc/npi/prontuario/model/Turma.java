package ufc.npi.prontuario.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ufc.npi.prontuario.repository.TurmaRepository;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ano", "semestre", "nome", "disciplina_id"}))
public class Turma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	private Integer ano;

	@NotNull
	private Integer semestre;

	@NotNull
	private String nome;

	private Boolean ativo;

	@OneToMany(mappedBy = "turma", cascade = CascadeType.MERGE)
	private List<AlunoTurma> alunoTurmas;

	@NotNull
	@Size(min = 1)
	@ManyToMany
	@OrderBy("nome ASC")
	private List<Servidor> professores;

	@ManyToOne(optional = false)
	@JoinColumn(name="disciplina_id") 
	private Disciplina disciplina;

	@OneToMany(mappedBy = "turma", cascade = CascadeType.MERGE)
	private List<Atendimento> atendimentos;

	public Turma() {		
	}
	
	public Turma(String nome, int ano, int semestre, Disciplina disciplina, List<Servidor> professores) {
		this.nome = nome;
		this.ano = ano;
		this.semestre = semestre;
		this.disciplina = disciplina;
		this.professores = professores;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getSemestre() {
		return semestre;
	}

	public void setSemestre(Integer semestre) {
		this.semestre = semestre;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public List<AlunoTurma> getAlunoTurmas() {
		return alunoTurmas;
	}

	public void setAlunoTurmas(List<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}

	public List<Servidor> getProfessores() {
		return professores;
	}

	public void setProfessores(List<Servidor> professores) {
		this.professores = professores;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public List<Atendimento> getAtendimentos() {
		return atendimentos;
	}

	public void setAtendimentos(List<Atendimento> atendimentos) {
		this.atendimentos = atendimentos;
	}
	////////////////////////////////////////////////////////////////////////////////////
	public boolean turmaExisteNoRepositorio(TurmaRepository turmaRepository) {
		return turmaRepository.findByNomeAndAnoAndSemestreAndDisciplina(this.getNome(), this.getAno(), this.getSemestre(), this.getDisciplina()) != null;
	}
	//////////////////////////////////////////////////////////////////////////////////
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
		Turma other = (Turma) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
