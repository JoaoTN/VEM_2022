package br.ufc.quixada.npi.gestaocompetencia.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.Dificuldade;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.Impacto;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Responsabilidade {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Column(columnDefinition="TEXT")
	private String titulo;

	@ColumnDefault(value = "1")
	private int importancia = 1;
	
	@Enumerated(EnumType.STRING)
	private Impacto impacto;

	@Enumerated(EnumType.STRING)
	private Dificuldade dificuldade ;
	
	@ManyToMany
	private List<Competencia> competencias = null;

	@Column(columnDefinition="TEXT")
	private String tituloComissao;
	
	@Enumerated(EnumType.STRING)
	private Impacto impactoComissao;
	
	@Enumerated(EnumType.STRING)
	private Dificuldade dificuldadeComissao;
	
	@ManyToMany
	private List<Competencia> competenciasComissao = null;

	@Column(columnDefinition="TEXT")
	private String tituloChefia;
	
	@Enumerated(EnumType.STRING)
	private Impacto impactoChefia;
	
	@Enumerated(EnumType.STRING)
	private Dificuldade dificuldadeChefia;
	
	@ManyToMany
	private List<Competencia> competenciasChefia = null;

	@Column(columnDefinition="TEXT")
	private String tituloConsolidado;
	
	@Enumerated(EnumType.STRING)
	private Impacto impactoConsolidado;
	
	@Enumerated(EnumType.STRING)
	private Dificuldade dificuldadeConsolidado;
	
	@ManyToMany
	private List<Competencia> competenciasConsolidado = null;
	
	@ManyToOne
	@JoinColumn(name="usuario_gestor_id")
	private Usuario usuarioGestor;
	
	@ManyToOne
	@JoinColumn(name="usuario_comissao_id")
	private Usuario usuarioComissao;
	
	@ManyToOne
	@JoinColumn(name="usuario_chefia_id")
	private Usuario usuarioChefia;
	
	@ManyToOne
	@JoinColumn(name="usuario_consolidacao_id")
	private Usuario usuarioConsolidacao;

	@Column(name="editada", columnDefinition="boolean default false")
	private boolean editada;
	
	@Column(name="validada", columnDefinition="boolean default false")
	private boolean validada= false;
	
	@Column(name="consolidada", columnDefinition="boolean default false")
	private boolean consolidada= false;

	@Column(name="excluida", columnDefinition="boolean default false")
	private boolean excluida;

	@ManyToOne
	private Mapeamento mapeamento;
	
	@ManyToOne
	private Unidade unidade;

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getImportancia() {
		return importancia;
	}

	public void setImportancia(int importancia) {
		this.importancia = importancia;
	}

	public Impacto getImpacto() {
		return impacto;
	}

	public void setImpacto(Impacto impacto) {
		this.impacto = impacto;
	}

	public Dificuldade getDificuldade() {
		return dificuldade;
	}

	public void setDificuldade(Dificuldade dificuldade) {
		this.dificuldade = dificuldade;
	}

	public String getTituloComissao() {
		return tituloComissao;
	}

	public void setTituloComissao(String tituloComissao) {
		this.tituloComissao = tituloComissao;
	}

	public Impacto getImpactoComissao() {
		return impactoComissao;
	}

	public void setImpactoComissao(Impacto impactoComissao) {
		this.impactoComissao = impactoComissao;
	}

	public Dificuldade getDificuldadeComissao() {
		return dificuldadeComissao;
	}

	public void setDificuldadeComissao(Dificuldade dificuldadeComissao) {
		this.dificuldadeComissao = dificuldadeComissao;
	}

	public String getTituloChefia() {
		return tituloChefia;
	}

	public void setTituloChefia(String tituloChefia) {
		this.tituloChefia = tituloChefia;
	}

	public Impacto getImpactoChefia() {
		return impactoChefia;
	}

	public void setImpactoChefia(Impacto impactoChefia) {
		this.impactoChefia = impactoChefia;
	}

	public Dificuldade getDificuldadeChefia() {
		return dificuldadeChefia;
	}

	public void setDificuldadeChefia(Dificuldade dificuldadeChefia) {
		this.dificuldadeChefia = dificuldadeChefia;
	}

	public String getTituloConsolidado() {
		return tituloConsolidado;
	}

	public void setTituloConsolidado(String tituloConsolidado) {
		this.tituloConsolidado = tituloConsolidado;
	}

	public Impacto getImpactoConsolidado() {
		return impactoConsolidado;
	}

	public void setImpactoConsolidado(Impacto impactoConsolidado) {
		this.impactoConsolidado = impactoConsolidado;
	}

	public Dificuldade getDificuldadeConsolidado() {
		return dificuldadeConsolidado;
	}

	public void setDificuldadeConsolidado(Dificuldade dificuldadeConsolidado) {
		this.dificuldadeConsolidado = dificuldadeConsolidado;
	}

	public List<Competencia> getCompetencias() {
		return competencias;
	}

	public void setCompetencias(List<Competencia> competencias) {
		this.competencias = competencias;
	}

	public Usuario getUsuarioGestor() {
		return usuarioGestor;
	}

	public void setUsuarioGestor(Usuario usuarioGestor) {
		this.usuarioGestor = usuarioGestor;
	}

	public Usuario getUsuarioComissao() {
		return usuarioComissao;
	}

	public void setUsuarioComissao(Usuario usuarioComissao) {
		this.usuarioComissao = usuarioComissao;
	}

	public Usuario getUsuarioChefia() {
		return usuarioChefia;
	}

	public void setUsuarioChefia(Usuario usuarioChefia) {
		this.usuarioChefia = usuarioChefia;
	}

	public Usuario getUsuarioConsolidacao() {
		return usuarioConsolidacao;
	}

	public void setUsuarioConsolidacao(Usuario usuarioConsolidacao) {
		this.usuarioConsolidacao = usuarioConsolidacao;
	}

	public boolean isExcluida() {
		return excluida;
	}

	public void setExcluida(boolean excluida) {
		this.excluida = excluida;
	}

	public Mapeamento getMapeamento() {
		return mapeamento;
	}

	public void setMapeamento(Mapeamento mapeamento) {
		this.mapeamento = mapeamento;
	}

	public List<Competencia> getCompetenciasComissao() {
		return competenciasComissao;
	}

	public void setCompetenciasComissao(List<Competencia> competenciasComissao) {
		this.competenciasComissao = new ArrayList<>();
		for (Competencia competencia : competenciasComissao) {
			if(!this.competenciasComissao.contains(competencia))
				this.competenciasComissao.add(competencia);
		}
	}

	public List<Competencia> getCompetenciasChefia() {
		return competenciasChefia;
	}

	public void setCompetenciasChefia(List<Competencia> competenciasChefia) {
		this.competenciasChefia = new ArrayList<>();
		for (Competencia competencia : competenciasChefia) {
			if(!this.competenciasChefia.contains(competencia))
				this.competenciasChefia.add(competencia);
		}
	}

	public List<Competencia> getCompetenciasConsolidado() {
		return competenciasConsolidado;
	}

	public void setCompetenciasConsolidado(List<Competencia> competenciasConsolidado) {
		this.competenciasConsolidado = new ArrayList<>();
		for (Competencia competencia : competenciasConsolidado) {
			if(!this.competenciasConsolidado.contains(competencia))
				this.competenciasConsolidado.add(competencia);
		}
	}

	public boolean isEditada() {
		return editada;
	}

	public void setEditada(boolean editada) {
		this.editada = editada;
	}

	public boolean isValidada() {
		return validada;
	}

	public void setValidada(boolean validada) {
		this.validada = validada;
	}

	public boolean isConsolidada() {
		return consolidada;
	}

	public void setConsolidada(boolean consolidada) {
		this.consolidada = consolidada;
	}

	public String getTituloAtual() {
		if(tituloConsolidado != null) {
			return tituloConsolidado;
		} else if(tituloChefia != null) {
			return tituloChefia;
		} else if(tituloComissao != null) {
			return tituloComissao;
		} else {
			return titulo;
		}
	}
	
	public Impacto getImpactoAtual() {
		if(impactoConsolidado != null) {
			return impactoConsolidado;
		} else if(impactoChefia != null) {
			return impactoChefia;
		} else if(impactoComissao != null) {
			return impactoComissao;
		} else {
			return impacto;
		}
	}
	
	public Dificuldade getDificuldadeAtual() {
		if(dificuldadeConsolidado != null) {
			return dificuldadeConsolidado;
		} else if(dificuldadeChefia != null) {
			return dificuldadeChefia;
		} else if(dificuldadeComissao != null) {
			return dificuldadeComissao;
		} else {
			return dificuldade;
		}
	}
	
	public List<Competencia> getCompetenciasAtuais(){
		if(competenciasConsolidado != null && !competenciasConsolidado.isEmpty()) {
			return competenciasConsolidado;
		} else if(competenciasChefia != null && !competenciasChefia.isEmpty()) {
			return competenciasChefia;
		} else if(competenciasComissao != null && !competenciasComissao.isEmpty()) {
			return competenciasComissao;
		}
		return competencias;
	}

	public void atualizarConsolidacao(Usuario usuario, Responsabilidade responsabilidade) {
		setUsuarioConsolidacao(usuario);
		setTituloConsolidado(responsabilidade.getTituloAtual());
		setDificuldadeConsolidado(responsabilidade.getDificuldadeAtual());
		setImpactoConsolidado(responsabilidade.getImpactoAtual());
		setCompetenciasConsolidado(responsabilidade.getCompetenciasAtuais());
	}

	public void atualizarValidacao(Usuario usuario, Responsabilidade responsabilidade) {
		setUsuarioChefia(usuario);
		setTituloChefia(responsabilidade.getTituloAtual());
		setImpactoChefia(responsabilidade.getImpactoAtual());
		setDificuldadeChefia(responsabilidade.getDificuldadeAtual());
		setCompetenciasChefia(responsabilidade.getCompetenciasAtuais());
	}
}
