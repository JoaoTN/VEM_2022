package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.CategoriaComportamento;
import io.swagger.annotations.ApiModelProperty;

@Entity
public class Comportamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	private Long id;
	
	@NotEmpty
	@Column(columnDefinition="TEXT")
	private String descricao;

	@Column(columnDefinition="TEXT")
	private String descricaoAtualizada;

	@ApiModelProperty(hidden = true)
	private boolean excluido;

	@Enumerated(EnumType.STRING)
	private CategoriaComportamento categoria;
	
	@ManyToOne
	@JoinColumn(name="competencia_id")
	@ApiModelProperty(hidden = true)
	private Competencia competencia;

	@ManyToOne
	@JsonIgnore
	private Usuario servidor;

	@ManyToOne
	private Mapeamento mapeamento;
	
	@ColumnDefault(value = "false")
	private boolean consolidado;
	
	@ManyToOne
	private Usuario responsavelComissao;
	
	@ColumnDefault(value = "1")
	private int importancia = 1;

    @ManyToOne
    private Unidade unidade;

    public Unidade getUnidade() { return unidade; }

    public void setUnidade(Unidade unidade) { this.unidade = unidade; }
	
	public int getImportancia() {
		return importancia;
	}

	public void setImportancia(int importancia) {
		this.importancia = importancia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricaoAtualizada() {
		return descricaoAtualizada;
	}

	public void setDescricaoAtualizada(String descricaoAtualizada) {
		this.descricaoAtualizada = descricaoAtualizada;
	}

	public boolean isExcluido() {
		return excluido;
	}

	public void setExcluido(boolean excluido) {
		this.excluido = excluido;
	}

	public CategoriaComportamento getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaComportamento categoria) {
		this.categoria = categoria;
	}

	public Competencia getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}

	public Usuario getServidor() {
		return servidor;
	}

	public void setServidor(Usuario servidor) {
		this.servidor = servidor;
	}

	public Mapeamento getMapeamento() {
		return mapeamento;
	}

	public void setMapeamento(Mapeamento mapeamento) {
		this.mapeamento = mapeamento;
	}

	public boolean isConsolidado() {
		return consolidado;
	}

	public void setConsolidado(boolean consolidado) {
		this.consolidado = consolidado;
	}

	public Usuario getResponsavelComissao() {
		return responsavelComissao;
	}

	public void setResponsavelComissao(Usuario responsavelComissao) {
		this.responsavelComissao = responsavelComissao;
	}

	@JsonIgnore
	public boolean isValid() {
		return this.competencia != null && this.descricaoAtualizada.trim().length() > 0;
	}

	@Override
	public String toString() {
		return "Comportamento [id=" + id + ", descricao=" + descricao + ", descricaoAtualizada=" + descricaoAtualizada
				+ ", excluido=" + excluido + ", categoria=" + categoria + ", competencia=" + competencia + ", servidor="
				+ servidor + ", mapeamento=" + mapeamento + ", consolidado=" + consolidado + ", responsavelComissao="
				+ responsavelComissao + "]";
	}
	
	
}
