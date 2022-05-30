package br.ufc.quixada.npi.gestaocompetencia.model.dto;

public class CompetenciaDTO {

	private String competencia;
	private Long somatorioImportancias;

	public CompetenciaDTO(String competencia, Long somatorioImportancias) {
		super();
		this.competencia = competencia;
		this.somatorioImportancias = somatorioImportancias;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public Long getSomatorioImportancias() {
		return somatorioImportancias;
	}

	public void setSomatorioImportancias(Long somatorioImportancias) {
		this.somatorioImportancias = somatorioImportancias;
	}

}
