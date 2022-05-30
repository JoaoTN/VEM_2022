package ufc.quixada.npi.contest.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Participacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date data;

    @ManyToOne
    private Atividade atividade;

    @ManyToOne
    private Pessoa participante;

    @ManyToOne
    private Pessoa responsavel;

    private boolean valida;

    public Participacao() {
        valida = true;
    }

    public Participacao(Date data, Atividade atividade, Pessoa participante, Pessoa responsavel) {
        this.data = data;
        this.atividade = atividade;
        this.participante = participante;
        this.responsavel = responsavel;
        valida = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public Pessoa getParticipante() {
        return participante;
    }

    public void setParticipante(Pessoa participante) {
        this.participante = participante;
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }
}
