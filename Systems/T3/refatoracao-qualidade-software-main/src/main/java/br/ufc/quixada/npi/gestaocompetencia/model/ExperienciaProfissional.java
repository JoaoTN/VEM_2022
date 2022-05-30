package br.ufc.quixada.npi.gestaocompetencia.model;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.StatusExperienciaProfissional;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.TipoExperienciaProfissional;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class ExperienciaProfissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TipoExperienciaProfissional tipo;

    private String cargo;

    private String instituicao;

    @Enumerated(EnumType.STRING)
    private StatusExperienciaProfissional status;
    
    private LocalDate inicio;

    private LocalDate termino;

    @ManyToMany
    private List<AreaPerfil> areasProfissional = null;

    @ManyToOne
    private Perfil perfil;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoExperienciaProfissional getTipo() {
        return tipo;
    }

    public void setTipo(TipoExperienciaProfissional tipo) {
        this.tipo = tipo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public StatusExperienciaProfissional getStatus() {
        return status;
    }

    public void setStatus(StatusExperienciaProfissional status) {
        this.status = status;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getTermino() {
        return termino;
    }

    public void setTermino(LocalDate termino) {
        this.termino = termino;
    }

    public List<AreaPerfil> getAreasProfissional() {
        return areasProfissional;
    }

    public void setAreasProfissional(List<AreaPerfil> areasProfissional) {
        this.areasProfissional = areasProfissional;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
