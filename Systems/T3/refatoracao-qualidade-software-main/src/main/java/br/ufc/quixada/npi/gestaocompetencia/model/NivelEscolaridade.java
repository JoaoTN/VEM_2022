package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.*;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.Escolaridade;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.StatusNivelEscolaridade;

import java.time.LocalDate;

@Entity
public class NivelEscolaridade {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Escolaridade escolaridade;

	private String instituicao;

	private String curso;

	@Enumerated(EnumType.STRING)
	private StatusNivelEscolaridade status;

	private String residenciaEm;

	private LocalDate inicio;

	private LocalDate termino;

	@ManyToOne
	private Perfil perfil;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public String getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}
	
	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public StatusNivelEscolaridade getStatus() {
		return status;
	}

	public void setStatus(StatusNivelEscolaridade status) {
		this.status = status;
	}

	public String getResidenciaEm() {
		return residenciaEm;
	}

	public void setResidenciaEm(String residenciaEm) {
		this.residenciaEm = residenciaEm;
	}

	public LocalDate getInicio() {
		return inicio;
	}

	public void setInicio(LocalDate inicio) {
		this.inicio = inicio;
	}

	public LocalDate getTermino() {
		return termino;
	}

	public void setTermino(LocalDate termino) {
		this.termino = termino;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
}
