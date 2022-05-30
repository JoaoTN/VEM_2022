package br.ufc.quixada.npi.gestaocompetencia.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Escolaridade {

	ENSINO_FUNDAMENTAL("ENSINO_FUNDAMENTAL","Ensino Fundamental"),
	ENSINO_MEDIO("ENSINO_MEDIO","Ensino Médio"),
	ENSINO_PROFISSIONAL_TECNICO("ENSINO_PROFISSIONAL_TECNICO","Ensino Profissional de Nível Técnico"),
	GRADUACAO("GRADUACAO","Graduação"),
	ESPECIALIZACAO("ESPECIALIZACAO","Especialização"),
	ESPECIALIZACAO_RESIDENCIA_MEDICA("ESPECIALIZACAO_RESIDENCIA_MEDICA","Especialização - Residência médica"),
	MESTRADO_ACADEMICO("MESTRADO_ACADEMICO","Mestrado Acadêmico"),
	MESTRADO_PROFISSIONAL("MESTRADO_PROFISSIONAL","Mestrado Profissional"),
	DOUTORADO_ACADEMICO("DOUTORADO_ACADEMICO","Doutorado Acadêmico"),
	DOUTORADO_PROFISSIONAL("DOUTORADO_PROFISSIONAL","Doutorado Profissional"),
	POS_DOUTORADO("POS_DOUTORADO","Pós-Doutorado");

    private String id;
    private String descricao;

    private Escolaridade(String id, String descricao) {
    	 this.id = id;
         this.descricao = descricao;
	}

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

}
