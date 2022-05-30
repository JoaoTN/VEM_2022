package ufc.npi.prontuario.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(AlunoTurmaId.class)
public class AlunoTurma {

	@Id
	@ManyToOne(optional = false)
	private Aluno aluno;

	@Id
	@ManyToOne(optional = false)
	private Turma turma;

	private Boolean ativo;

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
		result = prime * result + ((turma == null) ? 0 : turma.hashCode());
		return result;
	}
////////////////////////////////////////////////////////////////////////////////
    public void setInscricao(Aluno aluno, Turma turma, Boolean ativo) {
		this.setAluno(aluno);
		this.setTurma(turma);
		this.setAtivo(ativo);
	}
///////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlunoTurma other = (AlunoTurma) obj;
		if (aluno == null) {
			if (other.aluno != null)
				return false;
		} else if (!aluno.equals(other.aluno))
			return false;
		if (turma == null) {
			if (other.turma != null)
				return false;
		} else if (!turma.equals(other.turma))
			return false;
		return true;
	}

}
