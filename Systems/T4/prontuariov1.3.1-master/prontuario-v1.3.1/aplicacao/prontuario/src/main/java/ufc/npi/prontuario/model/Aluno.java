package ufc.npi.prontuario.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;


@Entity
public class Aluno extends Usuario {

	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer anoIngresso;

	@NotNull
	private Integer semestreIngresso;

	@OneToMany(mappedBy = "aluno")
	private List<AlunoTurma> alunoTurmas;

	@OneToMany(mappedBy = "responsavel")
	private List<PacienteAnamnese> pacienteAnamneses;

	@OneToMany(mappedBy = "responsavel")
	private List<Atendimento> atendimentos;

	public Integer getAnoIngresso() {
		return anoIngresso;
	}

	public void setAnoIngresso(Integer anoIngresso) {
		this.anoIngresso = anoIngresso;
	}

	public Integer getSemestreIngresso() {
		return semestreIngresso;
	}

	public void setSemestreIngresso(Integer semestreIngresso) {
		this.semestreIngresso = semestreIngresso;
	}

	public List<AlunoTurma> getAlunoTurmas() {
		return alunoTurmas;
	}

	public void setAlunoTurmas(List<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}

	public List<PacienteAnamnese> getPacienteAnamneses() {
		return pacienteAnamneses;
	}

	public void setPacienteAnamneses(List<PacienteAnamnese> pacienteAnamneses) {
		this.pacienteAnamneses = pacienteAnamneses;
	}

	public List<Atendimento> getAtendimentos() {
		return atendimentos;
	}

	public void setAtendimentos(List<Atendimento> atendimentos) {
		this.atendimentos = atendimentos;
	}
	

}
