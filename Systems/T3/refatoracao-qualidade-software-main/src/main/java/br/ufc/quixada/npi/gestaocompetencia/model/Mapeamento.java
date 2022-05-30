package br.ufc.quixada.npi.gestaocompetencia.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Mapeamento{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate inicioPerComportServidor;
    private LocalDate fimPerComportServidor;
    private LocalDate inicioPerComportComissao;
    private LocalDate fimPerComportComissao;
    private LocalDate inicioPerRespGestor;
    private LocalDate fimPerRespGestor;
    private LocalDate inicioPerRespCom;
    private LocalDate fimPerRespCom;
    private LocalDate inicioPerRespChefiaSuperior;
    private LocalDate fimPerRespChefiaSuperior;
    private LocalDate inicioPerRespConsol;
    private LocalDate fimPerRespConsol;

    private String nome;

    private String descricao;

    @JsonIgnore
    @OneToMany(mappedBy="mapeamento")
    private List<UnidadeMapeada> unidadesMapeadas;

    @OneToMany(mappedBy="mapeamento")
    private List<Comportamento> comportamentos;
    
    @OneToMany(mappedBy="mapeamento")
    private List<Responsabilidade> responsabilidades;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public LocalDate getInicioPerComportServidor() {
		return inicioPerComportServidor;
	}

	public void setInicioPerComportServidor(LocalDate inicioPerComportServidor) {
		this.inicioPerComportServidor = inicioPerComportServidor;
	}

	public LocalDate getFimPerComportServidor() {
		return fimPerComportServidor;
	}

	public void setFimPerComportServidor(LocalDate fimPerComportServidor) {
		this.fimPerComportServidor = fimPerComportServidor;
	}

	public LocalDate getInicioPerComportComissao() {
		return inicioPerComportComissao;
	}

	public void setInicioPerComportComissao(LocalDate inicioPerComportComissao) {
		this.inicioPerComportComissao = inicioPerComportComissao;
	}

	public LocalDate getFimPerComportComissao() {
		return fimPerComportComissao;
	}

	public void setFimPerComportComissao(LocalDate fimPerComportComissao) {
		this.fimPerComportComissao = fimPerComportComissao;
	}

	public LocalDate getInicioPerRespGestor() {
		return inicioPerRespGestor;
	}

	public void setInicioPerRespGestor(LocalDate inicioPerRespGestor) {
		this.inicioPerRespGestor = inicioPerRespGestor;
	}

	public LocalDate getFimPerRespGestor() {
		return fimPerRespGestor;
	}

	public void setFimPerRespGestor(LocalDate fimPerRespGestor) {
		this.fimPerRespGestor = fimPerRespGestor;
	}

	public LocalDate getInicioPerRespCom() {
		return inicioPerRespCom;
	}

	public void setInicioPerRespCom(LocalDate inicioPerRespCom) {
		this.inicioPerRespCom = inicioPerRespCom;
	}

	public LocalDate getFimPerRespCom() {
		return fimPerRespCom;
	}

	public void setFimPerRespCom(LocalDate fimPerRespCom) {
		this.fimPerRespCom = fimPerRespCom;
	}

	public LocalDate getInicioPerRespChefiaSuperior() {
		return inicioPerRespChefiaSuperior;
	}

	public void setInicioPerRespChefiaSuperior(LocalDate inicioPerRespChefiaSuperior) {
		this.inicioPerRespChefiaSuperior = inicioPerRespChefiaSuperior;
	}

	public LocalDate getFimPerRespChefiaSuperior() {
		return fimPerRespChefiaSuperior;
	}

	public void setFimPerRespChefiaSuperior(LocalDate fimPerRespChefiaSuperior) {
		this.fimPerRespChefiaSuperior = fimPerRespChefiaSuperior;
	}

	public LocalDate getInicioPerRespConsol() {
		return inicioPerRespConsol;
	}

	public void setInicioPerRespConsol(LocalDate inicioPerRespConsol) {
		this.inicioPerRespConsol = inicioPerRespConsol;
	}

	public LocalDate getFimPerRespConsol() {
		return fimPerRespConsol;
	}

	public void setFimPerRespConsol(LocalDate fimPerRespConsol) {
		this.fimPerRespConsol = fimPerRespConsol;
	}

	public List<UnidadeMapeada> getUnidadesMapeadas() {
		return unidadesMapeadas;
	}

	public void addUnidadeMapeada(UnidadeMapeada unidadeMapeada) {
    	if (this.unidadesMapeadas == null) {
			this.unidadesMapeadas = new ArrayList<>();
		}
		this.unidadesMapeadas.add(unidadeMapeada);
	}

	public void addAllUnidadesMapeadas(List<UnidadeMapeada> unidadesMapeadas) {
		if (this.unidadesMapeadas == null) {
			this.unidadesMapeadas = new ArrayList<>();
		}
		this.unidadesMapeadas.addAll(unidadesMapeadas);
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
	
	/**
	 * Verifica se está no periodo de cadastros de comportamentos 
	 * @return - True: Se estiver no período de cadastros
	 * 		   - False: Se estiver fora do periodo de cadastros
	 */
	public boolean isPeriodoCadastroComportamentos() {
		return (
                    (
                        LocalDate.now().isAfter(this.getInicioPerComportServidor())
                        && LocalDate.now().isBefore(this.getFimPerComportServidor())
                    )
                    || (LocalDate.now().isEqual(this.getInicioPerComportServidor()))
                    || (LocalDate.now().isEqual(this.getFimPerComportServidor()))
        );
	}
	
	/**
	 * Verifica se está no periodo de normalizacao de comportamentos 
	 * @return - True: Se estiver no período de normalizacao
	 * 		   - False: Se estiver fora do periodo de normalizacao
	 */
	public boolean isPeriodoNormalizacaoComportamentos() {
		return (
					(
						LocalDate.now().isAfter(this.getInicioPerComportComissao())
						&& LocalDate.now().isBefore(this.getFimPerComportComissao())
					)
					|| (LocalDate.now().isEqual(this.getInicioPerComportComissao()))
					|| (LocalDate.now().isEqual(this.getFimPerComportComissao()))
		);
	}
	
	/**
	 * Verificas se está no periodo de edicao 
	 * da COMISSAO na normalização de responsabilidades
	 * @return 	- True: Se estiver no periodo
	 * 			- False: Se estiver fora do perdiodo
	 */
	public boolean isPeriodoEdicaoComissaoResponsabilidades() {
		return (
                    (
                        LocalDate.now().isAfter(this.getInicioPerRespCom())
                        && LocalDate.now().isBefore(this.getFimPerRespCom())
                    )
                    || (LocalDate.now().isEqual(this.getInicioPerRespCom()))
                    || (LocalDate.now().isEqual(this.getFimPerRespCom()))
        );
	}
	
	/**
	 * Verificas se está no periodo de edicao 
	 * da CHEFIA na normalização de responsabilidades
	 * @return 	- True: Se estiver no periodo
	 * 			- False: Se estiver fora do perdiodo
	 */
	public boolean isPeriodoEdicaoChefiaResponsabilidades() {
		return (
                    (
                        LocalDate.now().isAfter(this.getInicioPerRespChefiaSuperior())
                        && LocalDate.now().isBefore(this.getFimPerRespChefiaSuperior())
                    )
                    || (LocalDate.now().isEqual(this.getInicioPerRespChefiaSuperior()))
                    || (LocalDate.now().isEqual(this.getFimPerRespChefiaSuperior()))
        );
	}
	
	/**
	 * Verificas se está no periodo de edicao 
	 * da CONSOLIDACAO na normalização de responsabilidades
	 * @return 	- True: Se estiver no periodo
	 * 			- False: Se estiver fora do perdiodo
	 */
	public boolean isPeriodoEdicaoConsolidacaoResponsabilidades() {
		return (
                    (
                        LocalDate.now().isAfter(this.getInicioPerRespConsol())
                        && LocalDate.now().isBefore(this.getFimPerRespConsol())
                    )
                    || (LocalDate.now().isEqual(this.getInicioPerRespConsol()))
                    || (LocalDate.now().isEqual(this.getFimPerRespConsol()))
        );
	}
	
	/**
	 * Verificas se está no periodo de edicao
	 * das responsabilidades pelo GESTOR
	 * @return 	- True: Se estiver no periodo
	 * 			- False: Se estiver fora do perdiodo
	 */
	public boolean isPeriodoGestorEdicaoResponsabilidades() {
		return (
                    (
                        LocalDate.now().isAfter(this.getInicioPerRespGestor())
                        && LocalDate.now().isBefore(this.getFimPerRespGestor())
                    )
                    || (LocalDate.now().isEqual(this.getInicioPerRespGestor()))
                    || (LocalDate.now().isEqual(this.getFimPerRespGestor()))
        );
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Mapeamento that = (Mapeamento) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
