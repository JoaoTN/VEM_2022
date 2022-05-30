package ufc.quixada.npi.contest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "revisao")
public class Revisao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "conteudo", columnDefinition="TEXT")
	private String conteudo;

	@Column(name = "avaliacao")
	@Enumerated(EnumType.STRING)
	private Avaliacao avaliacao;

	// Somente visualizado pela organização do evento
	@Column(name="observacoes", columnDefinition="TEXT")
	private String observacoes;

	@Column(name="comentarios", columnDefinition="TEXT")
	private String comentarios;

	@Column(name = "avaliacao_geral")
	@Enumerated(EnumType.STRING)
	private Classificacao avaliacaoGeral;

	private String autoAvaliacao;
	
	@OneToOne
	private Pessoa revisor;
	
	private Boolean indicacao;

	@Enumerated(EnumType.STRING)
	private Classificacao originalidade;

	@Enumerated(EnumType.STRING)
	private Classificacao merito;

	@Enumerated(EnumType.STRING)
	private Classificacao clareza;

	@Enumerated(EnumType.STRING)
	private Classificacao qualidade;

	@Enumerated(EnumType.STRING)
	private Classificacao relevancia;

	@OneToOne
	private Arquivo arquivo;

	public enum Classificacao {
		RUIM("RUIM"), FRACO("FRACO"), MEDIO("MÉDIO"), BOM("BOM"), OTIMO("ÓTIMO");

		private String nome;

		Classificacao(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}

	@JsonIgnore
	@ManyToOne
	private Trabalho trabalho;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	
	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Pessoa getRevisor() {
		return revisor;
	}

	public void setRevisor(Pessoa revisor) {
		this.revisor = revisor;
	}

	public Trabalho getTrabalho() {
		return trabalho;
	}

	public void setTrabalho(Trabalho trabalho) {
		this.trabalho = trabalho;
	}

	public Boolean getIndicacao() {
		return indicacao != null && indicacao;
	}

	public void setIndicacao(Boolean indicacao) {
		this.indicacao = indicacao;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public Classificacao getAvaliacaoGeral() {
		return avaliacaoGeral;
	}

	public void setAvaliacaoGeral(Classificacao avaliacaoGeral) {
		this.avaliacaoGeral = avaliacaoGeral;
	}

	public String getAutoAvaliacao() {
		return autoAvaliacao;
	}

	public void setAutoAvaliacao(String autoAvaliacao) {
		this.autoAvaliacao = autoAvaliacao;
	}

	public Classificacao getOriginalidade() {
		return originalidade;
	}

	public void setOriginalidade(Classificacao originalidade) {
		this.originalidade = originalidade;
	}

	public Classificacao getMerito() {
		return merito;
	}

	public void setMerito(Classificacao merito) {
		this.merito = merito;
	}

	public Classificacao getClareza() {
		return clareza;
	}

	public void setClareza(Classificacao clareza) {
		this.clareza = clareza;
	}

	public Classificacao getQualidade() {
		return qualidade;
	}

	public void setQualidade(Classificacao qualidade) {
		this.qualidade = qualidade;
	}

	public Classificacao getRelevancia() {
		return relevancia;
	}

	public void setRelevancia(Classificacao relevancia) {
		this.relevancia = relevancia;
	}

	public boolean isRevisado() {
		return !Avaliacao.NAO_REVISADO.equals(this.avaliacao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Revisao other = (Revisao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean canViewFile(Pessoa usuario) {
		return this.getTrabalho().getAutor().equals(usuario) || this.getTrabalho().isCoautor(usuario) || this.revisor.equals(usuario) ||
				this.getTrabalho().getEvento().isOrganizador(usuario);
	}

	@Override
	public String toString() {
		return "Revisao [id=" + id + ", conteudo=" + conteudo + ", revisor=" + revisor + ", trabalho=" + trabalho + "]";
	}
}