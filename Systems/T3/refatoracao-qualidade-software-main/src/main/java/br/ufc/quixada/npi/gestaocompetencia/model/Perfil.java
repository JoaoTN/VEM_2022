package br.ufc.quixada.npi.gestaocompetencia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
public class Perfil {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Boolean instrutorCapacitacao = false;

	private Boolean instrutorCultura = false;

	private Boolean instrutorEsporte = false;

	@ManyToMany
	private List<AreaPerfil> areasPesquisa;

	@ManyToMany
	private List<AreaCapacitacao> areasCapacitacaoInstrutor;

	@ManyToMany
	private List<AreaPerfil> areasProfissional;

	@ManyToMany
	private List<AreaPerfil> areasCultura;

	@ManyToMany
	private List<AreaPerfil> areasCulturaInstrutor;

	@ManyToMany
	private List<AreaPerfil> areasEsporte;

	@ManyToMany
	private List<AreaPerfil> areasEsporteInstrutor;

	@JsonProperty( value = "pathCurriculum")
	private String pathCurriculum;

	private String curriculumLattes;

	@OneToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	/*@ElementCollection(targetClass = Escolaridade.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "escolaridade")
	@Column(name = "escolaridade")
	private List<Escolaridade> escolaridade;*/

	/*@OneToOne
	FormacaoAcademica formacaoAcademica;
	
	@OneToMany(mappedBy = "perfil")
	private List<CursoCapacitacao> cursos;*/
	
	/*@OneToMany(mappedBy = "perfil")
	private List<AreaAtuacao> areaAtuacaoProf;*/

	/*@ElementCollection(targetClass = String.class)
	@CollectionTable(name = "area_pesquisa")
	private List<String> areaPesquisa;*/

	/*@ElementCollection(targetClass = String.class)
	@CollectionTable(name = "area_cultura")
	private List<String> areaCultura;

	@ElementCollection(targetClass = String.class)
	@CollectionTable(name = "area_esporte")
	private List<String> areaEsporte;*/

	/*@OneToMany
	List<AtividadeInteresse> atividades;

	@OneToMany(mappedBy = "perfil")
	private List<ExperienciaChefia> experienciaChefia;*/

	/*@JsonIgnore
	@ManyToMany(mappedBy = "perfil")
	private List<PerfilPreferencia> preferenciaTrabalho;*/
	
	/*@OneToMany(mappedBy = "perfil")
	@JsonIgnore
	private List<PerfilIdioma> perfilIdiomas;*/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getInstrutorCapacitacao() {
		return instrutorCapacitacao;
	}

	public void setInstrutorCapacitacao(Boolean instrutorCapacitacao) {
		this.instrutorCapacitacao = instrutorCapacitacao;
	}

	public Boolean getInstrutorCultura() {
		return instrutorCultura;
	}

	public void setInstrutorCultura(Boolean instrutorCultura) {
		this.instrutorCultura = instrutorCultura;
	}

	public List<AreaPerfil> getAreasCulturaInstrutor() {
		return areasCulturaInstrutor;
	}

	public void setAreasCulturaInstrutor(List<AreaPerfil> areasCulturaInstrutor) {
		this.areasCulturaInstrutor = areasCulturaInstrutor;
	}

	public Boolean getInstrutorEsporte() {
		return instrutorEsporte;
	}

	public void setInstrutorEsporte(Boolean instrutorEsporte) {
		this.instrutorEsporte = instrutorEsporte;
	}

	public List<AreaPerfil> getAreasEsporteInstrutor() {
		return areasEsporteInstrutor;
	}

	public void setAreasEsporteInstrutor(List<AreaPerfil> areasEsporteInstrutor) {
		this.areasEsporteInstrutor = areasEsporteInstrutor;
	}

	/*public List<Escolaridade> getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(List<Escolaridade> escolaridade) {
		this.escolaridade = escolaridade;
	}*/

	/*public List<CursoCapacitacao> getCursos() {
		return cursos;
	}

	public void setCursos(List<CursoCapacitacao> cursos) {
		this.cursos = cursos;
	}*/

	/*public List<AreaAtuacao> getAreaAtuacaoProf() {
		return areaAtuacaoProf;
	}

	public void setAreaAtuacaoProf(List<AreaAtuacao> areaAtuacaoProf) {
		this.areaAtuacaoProf = areaAtuacaoProf;
	}*/

	/*public List<String> getAreasPesquisa() {
		return areaPesquisa;
	}

	public void setAreasPesquisa(List<String> areaPesquisa) {
		this.areaPesquisa = areaPesquisa;
	}*/

	/*public List<String> getAreaCultura() {
		return areaCultura;
	}

	public void setAreaCultura(List<String> areaCultura) {
		this.areaCultura = areaCultura;
	}

	public List<String> getAreaEsporte() {
		return areaEsporte;
	}

	public void setAreaEsporte(List<String> areaEsporte) {
		this.areaEsporte = areaEsporte;
	}*/

	/*public List<ExperienciaChefia> getExperienciaChefia() {
		return experienciaChefia;
	}

	public void setExperienciaChefia(List<ExperienciaChefia> experienciaChefia) {
		this.experienciaChefia = experienciaChefia;
	}*/
	/*public List<PerfilPreferencia> getPreferenciaTrabalho() {
		return preferenciaTrabalho;
	}

	public void setPreferenciaTrabalho(List<PerfilPreferencia> preferenciaTrabalho) {
		this.preferenciaTrabalho = preferenciaTrabalho;
	}*/
	
	/*public List<PerfilIdioma> getPerfilIdiomas() {
		return perfilIdiomas;
	}
	public void setPerfilIdiomas(List<PerfilIdioma> perfilIdiomas) {
		this.perfilIdiomas = perfilIdiomas;
	}*/

	public List<AreaPerfil> getAreasPesquisa() {
		return areasPesquisa;
	}

	public void setAreasPesquisa(List<AreaPerfil> areasPesquisa) {
		this.areasPesquisa = areasPesquisa;
	}

	public List<AreaCapacitacao> getAreasCapacitacaoInstrutor() {
		return areasCapacitacaoInstrutor;
	}

	public void setAreasCapacitacaoInstrutor(List<AreaCapacitacao> areasCapacitacaoInstrutor) {
		this.areasCapacitacaoInstrutor = areasCapacitacaoInstrutor;
	}

	public List<AreaPerfil> getAreasProfissional() {
		return areasProfissional;
	}

	public void setAreasProfissional(List<AreaPerfil> areasProfissional) {
		this.areasProfissional = areasProfissional;
	}

	public List<AreaPerfil> getAreasCultura() {
		return areasCultura;
	}

	public void setAreasCultura(List<AreaPerfil> areasCultura) {
		this.areasCultura = areasCultura;
	}

	public List<AreaPerfil> getAreasEsporte() {
		return areasEsporte;
	}

	public void setAreasEsporte(List<AreaPerfil> areasEsporte) {
		this.areasEsporte = areasEsporte;
	}

	public String getPathCurriculum() {
		return pathCurriculum;
	}

	public void setPathCurriculum(String pathCurriculum) {
		this.pathCurriculum = pathCurriculum;
	}

	public String getCurriculumLattes() {
		return curriculumLattes;
	}

	public void setCurriculumLattes(String curriculumLattes) {
		this.curriculumLattes = curriculumLattes;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Perfil perfil = (Perfil) o;
		return id.equals(perfil.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
