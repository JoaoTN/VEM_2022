package br.ufc.quixada.npi.gestaocompetencia.model;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.NivelIdioma;

import javax.persistence.*;

@Entity
public class PerfilIdioma {

    @EmbeddedId
    private PerfilIdiomaKey perfilIdiomaKey;

    @ManyToOne
    @MapsId("perfil_id")
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    @ManyToOne
    @MapsId("idioma_id")
    @JoinColumn(name = "idioma_id")
    private Idioma idioma;

    @Enumerated(EnumType.STRING)
    private NivelIdioma compreensao;

    @Enumerated(EnumType.STRING)
    private NivelIdioma fala;

    @Enumerated(EnumType.STRING)
    private NivelIdioma escrita;

    @Enumerated(EnumType.STRING)
    private NivelIdioma leitura;

    public PerfilIdiomaKey getPerfilIdiomaKey() {
        return perfilIdiomaKey;
    }

    public void setPerfilIdiomaKey(PerfilIdiomaKey perfilIdiomaKey) {
        this.perfilIdiomaKey = perfilIdiomaKey;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public NivelIdioma getCompreensao() {
        return compreensao;
    }

    public void setCompreensao(NivelIdioma compreensao) {
        this.compreensao = compreensao;
    }

    public NivelIdioma getFala() {
        return fala;
    }

    public void setFala(NivelIdioma fala) {
        this.fala = fala;
    }

    public NivelIdioma getEscrita() {
        return escrita;
    }

    public void setEscrita(NivelIdioma escrita) {
        this.escrita = escrita;
    }

    public NivelIdioma getLeitura() {
        return leitura;
    }

    public void setLeitura(NivelIdioma leitura) {
        this.leitura = leitura;
    }
}
