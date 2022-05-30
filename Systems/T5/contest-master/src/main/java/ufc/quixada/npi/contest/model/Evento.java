package ufc.quixada.npi.contest.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import ufc.quixada.npi.contest.util.CalendarioApplication;
import ufc.quixada.npi.contest.util.ValidacaoPeriodo;

@Entity
@Table(name = "evento")
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotEmpty(message = "{NOME_EVENTO_VAZIO_ERROR}")
	@Column(name = "nome")
	private String nome;

	@Column(name = "descricao")
	private String descricao;

	private String codigo;

	@Enumerated(EnumType.STRING)
	@Column(name = "visibilidade")
	private VisibilidadeEvento visibilidade;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	private EstadoEvento estado;

	@Temporal(TemporalType.DATE)
	@Column(name = "inicio_submissao")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date inicioSubmissao;

	@Temporal(TemporalType.DATE)
	@Column(name = "camera_ready")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date cameraReady;

	@Temporal(TemporalType.DATE)
	@Column(name = "termino_submissao")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date terminoSubmissao;

	@Temporal(TemporalType.DATE)
	@Column(name = "prazo_notificacao")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date prazoNotificacao;

	@ManyToMany
	@JoinTable(name = "organizadores_evento", joinColumns = { @JoinColumn(name = "evento_id") }, inverseJoinColumns = {
			@JoinColumn(name = "organizador_id") })
	private List<Pessoa> organizadores;

	@ManyToMany
	@JoinTable(name = "revisores_evento", joinColumns = { @JoinColumn(name = "evento_id") }, inverseJoinColumns = {
			@JoinColumn(name = "revisor_id") })
	private List<Pessoa> revisores;

	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = false)
	@OrderBy("nome ASC")
	private List<Trilha> trilhas;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
	@OrderBy("nome ASC")
	@JoinTable(name = "modalidade_submissao_evento", joinColumns = @JoinColumn(name = "evento_id"), inverseJoinColumns = @JoinColumn(name = "modalidade_id"))
	private List<Modalidade> modalidadesSubmissao;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
	@OrderBy("nome ASC")
	@JoinTable(name = "modalidade_apresentacao_evento", joinColumns = @JoinColumn(name = "evento_id"), inverseJoinColumns = @JoinColumn(name = "modalidade_id"))
	private List<Modalidade> modalidadesApresentacao;

	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = false)
	@OrderBy("nome ASC")
	private List<Sessao> sessoes;

	public Long getId() {
		return id;
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

	public VisibilidadeEvento getVisibilidade() {
		return visibilidade;
	}

	public void setVisibilidade(VisibilidadeEvento visibilidade) {
		this.visibilidade = visibilidade;
	}

	public EstadoEvento getEstado() {
		return estado;
	}

	public void setEstado(EstadoEvento estado) {
		this.estado = estado;
	}

	public Date getInicioSubmissao() {
		return inicioSubmissao;
	}

	public void setInicioSubmissao(Date inicioSubmissao) {
		this.inicioSubmissao = inicioSubmissao;
	}

	public Date getCameraReady() {
		return cameraReady;
	}

	public void setCameraReady(Date cameraReady) {
		this.cameraReady = cameraReady;
	}

	public Date getTerminoSubmissao() {
		return terminoSubmissao;
	}

	public void setTerminoSubmissao(Date terminoSubmissao) {
		this.terminoSubmissao = terminoSubmissao;
	}

	public Date getPrazoNotificacao() {
		return prazoNotificacao;
	}

	public void setPrazoNotificacao(Date prazoNotificacao) {
		this.prazoNotificacao = prazoNotificacao;
	}

	public List<Pessoa> getOrganizadores() {
		return organizadores;
	}

	public void setOrganizadores(List<Pessoa> organizadores) {
		this.organizadores = organizadores;
	}

	public List<Pessoa> getRevisores() {
		return revisores;
	}

	public void setRevisores(List<Pessoa> revisores) {
		this.revisores = revisores;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public List<Trilha> getTrilhas() {
		return trilhas;
	}

	public void setTrilhas(List<Trilha> trilhas) {
		if (this.trilhas == null) {
			this.trilhas = trilhas;
		}
		this.trilhas.clear();
		this.trilhas.addAll(trilhas);
	}

	public List<Modalidade> getModalidadesSubmissao() {
		return modalidadesSubmissao;
	}
	
	public void setModalidadesSubmissao(List<Modalidade> modalidadeSubmissao) {
		this.modalidadesSubmissao = modalidadeSubmissao;
	}

/*
	public void addModalidadeSubmissao(Modalidade modalidadeSubmissao) {
		this.modalidadesSubmissao.add(modalidadeSubmissao);
	}

	public void removeModalidadeSubmissao(Modalidade modalidadeSubmissao) {
		this.modalidadesSubmissao.remove(modalidadeSubmissao);
	}
*/

	public List<Modalidade> getModalidadesApresentacao() {
		return modalidadesApresentacao;
	}

	public void setModalidadesApresentacao(List<Modalidade> modalidadeApresentacao) {
		this.modalidadesApresentacao = modalidadeApresentacao;
	}
/*
	public void addModalidadeApresentacao(Modalidade modalidadeApresentacao) {
		this.modalidadesApresentacao.add(modalidadeApresentacao);
	}

	public void removeModalidadeApresentacao(Modalidade modalidadeApresentacao) {
		this.modalidadesApresentacao.remove(modalidadeApresentacao);
	}
*/
	public boolean isPeriodoFinal() {
		return (CalendarioApplication.equalDateToday(prazoNotificacao) && CalendarioApplication.equalDateToday(cameraReady));
	}
	
	public boolean isPeriodoSubmissao() {
		return ValidacaoPeriodo.isPeriodo(inicioSubmissao, terminoSubmissao);
	}

	public boolean isPeriodoRevisao() {
		try {
			Date hoje = ValidacaoPeriodo.formatterDateToday();
			return !(hoje.after(prazoNotificacao) && !terminoSubmissao.after(hoje));
		} catch (ParseException e) {
			return false;
		}
	}
	
	public boolean isPeriodoSubmissaoFinal() {
		Date termino = CalendarioApplication.getDateCalendario(cameraReady, 1);
		return ValidacaoPeriodo.isPeriodo(prazoNotificacao, termino);
	}

	public boolean isAfterRevisao() {
		return ValidacaoPeriodo.isAfterPeriod(prazoNotificacao);
	}

	public boolean isAfterSubmissao() {
		return ValidacaoPeriodo.isAfterPeriod(terminoSubmissao);
	}

	public List<Sessao> getSessoes() {
		return sessoes;
	}

	public void setSessoes(List<Sessao> sessoes) {
		if (this.sessoes == null) {
			this.sessoes = sessoes;
		}
		this.sessoes.clear();
		this.sessoes.addAll(sessoes);
	}

	public boolean isOrganizador(Object object) {
		Pessoa pessoa = (Pessoa) object;
		return this.organizadores.contains(pessoa);
	}

	public boolean isRevisor(Object object) {
		Pessoa pessoa = (Pessoa) object;
		return this.revisores.contains(pessoa);
	}

}