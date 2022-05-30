package br.ufc.quixada.npi.gestaocompetencia.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PerfilPreferenciaKey implements Serializable {

    @Column(name = "perfil_id")
    private Integer perfilId;

    @Column(name = "preferencia_id")
    private Integer preferenciaId;

    public Integer getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(Integer perfilId) {
        this.perfilId = perfilId;
    }

    public Integer getPreferenciaId() {
        return preferenciaId;
    }

    public void setPreferenciaId(Integer preferenciaId) {
        this.preferenciaId = preferenciaId;
    }
}
