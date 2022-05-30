package ufc.npi.prontuario.model;

import static ufc.npi.prontuario.util.ConfigurationConstants.DATE_PATTERN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String nome;

    @DateTimeFormat(pattern = DATE_PATTERN)
    private Date nascimento;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    private Raca raca;

    private String naturalidade;

    private String nacionalidade;
    
    private List<Documento> documentos;

    private String cpf;

    private String cns;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'Não informado'")
    private String nomeDaMae;

    private String profissao;

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;

    private String responsavel;

    private String telefone;

    private String segundoTelefone;

    private String endereco;

    private String bairro;

    private String cidade;

    private String cep;

    private String pais;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @OrderBy("data DESC")
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.MERGE)
    private List<PacienteAnamnese> pacienteAnamneses;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Odontograma odontograma;

    @OrderBy("data DESC")
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.MERGE)
    private List<Atendimento> atendimentos;

    @OneToMany(mappedBy = "paciente")
    private List<PlanoTratamento> tratamentos;

    public List<PlanoTratamento> getTratamentos() {
		return tratamentos;
	}

	public void setTratamentos(List<PlanoTratamento> tratamentos) {
		this.tratamentos = tratamentos;
	}

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

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSegundoTelefone() {
        return segundoTelefone;
    }

    public void setSegundoTelefone(String segundoTelefone) {
        this.segundoTelefone = segundoTelefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<PacienteAnamnese> getPacienteAnamneses() {
        return pacienteAnamneses;
    }

    public void setPacienteAnamneses(List<PacienteAnamnese> pacienteAnamneses) {
        this.pacienteAnamneses = pacienteAnamneses;
    }

    public void addPacienteAnamnese(PacienteAnamnese anamnese) {
        if (this.pacienteAnamneses == null) {
            this.pacienteAnamneses = new ArrayList<>();
        }

        this.pacienteAnamneses.add(anamnese);
    }

    public boolean temSexo() {
        return this.sexo != null;
    }

    public boolean temEstadoCivil() {
        return this.estadoCivil != null;
    }

    public boolean temEstado() {
        return this.estado != null;
    }

    public Odontograma getOdontograma() {
        return odontograma;
    }

    public void setOdontograma(Odontograma odontograma) {
        this.odontograma = odontograma;
    }

    public List<Atendimento> getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(List<Atendimento> atendimentos) {
        this.atendimentos = atendimentos;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getNomeDaMae() {
        return nomeDaMae;
    }

    public void setNomeDaMae(String nomeDaMae) {
        this.nomeDaMae = nomeDaMae;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Raca getRaca() {
        return raca;
    }

    public void setRaca(Raca raca) {
        this.raca = raca;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void addDocumento(Documento documento) {
        if (this.documentos == null) {
            this.documentos = new ArrayList<>();
        }

        this.documentos.add(documento);
    }

    public void removerDocumento(Documento documento) {
        this.documentos.remove(documento);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Paciente other = (Paciente) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

}
