package br.ufc.quixada.npi.gestaocompetencia.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Diagnostico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;

	private String descricao;

	private LocalDate inicioComportamental;

	private LocalDate fimComportamental;

	private LocalDate inicioResponsabilidadeChefia;

	private LocalDate fimResponsabilidadeChefia;

	private LocalDate inicioResponsabilidadeServidor;

	private LocalDate fimResponsabilidadeServidor;

	@ManyToOne
	private Mapeamento mapeamento;

	private LocalDate criadoEm;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public LocalDate getInicioComportamental() {
		return inicioComportamental;
	}

	public void setInicioComportamental(LocalDate inicioComportamental) {
		this.inicioComportamental = inicioComportamental;
	}

	public LocalDate getFimComportamental() {
		return fimComportamental;
	}

	public void setFimComportamental(LocalDate fimComportamental) {
		this.fimComportamental = fimComportamental;
	}

	public LocalDate getInicioResponsabilidadeChefia() {
		return inicioResponsabilidadeChefia;
	}

	public void setInicioResponsabilidadeChefia(LocalDate inicioResponsabilidadeChefia) {
		this.inicioResponsabilidadeChefia = inicioResponsabilidadeChefia;
	}

	public LocalDate getFimResponsabilidadeChefia() {
		return fimResponsabilidadeChefia;
	}

	public void setFimResponsabilidadeChefia(LocalDate fimResponsabilidadeChefia) {
		this.fimResponsabilidadeChefia = fimResponsabilidadeChefia;
	}

	public LocalDate getInicioResponsabilidadeServidor() {
		return inicioResponsabilidadeServidor;
	}

	public void setInicioResponsabilidadeServidor(LocalDate inicioResponsabilidadeServidor) {
		this.inicioResponsabilidadeServidor = inicioResponsabilidadeServidor;
	}

	public LocalDate getFimResponsabilidadeServidor() {
		return fimResponsabilidadeServidor;
	}

	public void setFimResponsabilidadeServidor(LocalDate fimResponsabilidadeServidor) {
		this.fimResponsabilidadeServidor = fimResponsabilidadeServidor;
	}

	public Mapeamento getMapeamento() {
		return mapeamento;
	}

	public void setMapeamento(Mapeamento mapeamento) {
		this.mapeamento = mapeamento;
	}

	public boolean isPeriodoAvaliacaoComportamental() {
		return validarPrazo(this.getInicioComportamental(), this.getFimComportamental());
	}

	public boolean isPeriodoAvaliacaoRespChefia() {
		return validarPrazo(this.getInicioResponsabilidadeChefia(), this.getFimResponsabilidadeChefia());
	}

	public boolean isPeriodoAvaliacaoRespServidores() {
		return validarPrazo(this.getInicioResponsabilidadeServidor(), this.getFimResponsabilidadeServidor());
	}

	public boolean validarPrazo(LocalDate inicio, LocalDate fim) {
		return LocalDate.now().isAfter(inicio)
				&& (
						LocalDate.now().isBefore(fim) ||
						LocalDate.now().isEqual(inicio) ||
						LocalDate.now().isEqual(fim)
					);
	}

	public LocalDate getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(LocalDate criadoEm) {
		this.criadoEm = criadoEm;
	}
	
}
