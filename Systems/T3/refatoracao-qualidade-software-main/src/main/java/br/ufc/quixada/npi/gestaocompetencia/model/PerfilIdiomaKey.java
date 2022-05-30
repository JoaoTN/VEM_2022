package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PerfilIdiomaKey implements Serializable {

    @Column(name = "perfil_id")
    private Integer perfilId;

    @Column(name = "idioma_id")
    private Integer idiomaId;

    public Integer getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(Integer perfilId) {
        this.perfilId = perfilId;
    }

    public Integer getIdiomaId() {
        return idiomaId;
    }

    public void setIdiomaId(Integer idiomaId) {
        this.idiomaId = idiomaId;
    }
}
