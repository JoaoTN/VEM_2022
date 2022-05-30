package br.udesc.ceavi.pin.infosaude.modelo;

import java.util.Date;

/**
 * Esta Classe define os atributos de Pessoa
 *
 * @author Erick William Henschel
 * @author Fabio Frare
 * @author Gustavo de Carvalho Santos
 * @author Lucas Eduardo Nogueira
 * @author Ricardo José Pinto
 * @version 1.0
 * @since 03/11/2018
 */
public class Pessoa implements Usuario_Logado {

    private long id_pessoa;
    private String cpf;
    private Date dataNascimento;
    private String login;
    private String nome;
    private String numeroSUS;
    private String registroGeral;
    private String senha;
    private Sexo sexo;
    private Endereco endereco;

    //Construtor
    public Pessoa() {
        super();
        this.nome = "Teste InfoSaude";
    }
    
    public Pessoa(Pessoa pessoa) {
        super();
        this.id_pessoa  = (long)pessoa.id_pessoa;
        this.cpf = pessoa.getCpf();
        this.dataNascimento = pessoa.getDataNascimento();
        this.login = pessoa.getLogin();
        this.nome = pessoa.getNome();
        this.numeroSUS = pessoa.getNumeroSUS();
        this.registroGeral = pessoa.getRegistroGeral();
        this.senha = pessoa.getSenha();
        this.sexo = pessoa.getSexo();
        this.endereco = pessoa.getEndereco();
    }

    public Pessoa(long id_pessoa, String cpf, Date dataNascimento, String login, String nome, String numeroSUS, String registroGeral, String senha, Sexo sexo) {
        super();
        this.id_pessoa = id_pessoa;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.login = login;
        this.nome = nome;
        this.numeroSUS = numeroSUS;
        this.registroGeral = registroGeral;
        this.senha = senha;
        this.sexo = sexo;
    }

    public Pessoa(long id_pessoa, String cpf, Date dataNascimento, String login, String nome, 
            String numeroSUS, String registroGeral, String senha, Sexo sexo, Endereco endereco) {
        this.id_pessoa = id_pessoa;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.login = login;
        this.nome = nome;
        this.numeroSUS = numeroSUS;
        this.registroGeral = registroGeral;
        this.senha = senha;
        this.sexo = sexo;
        this.endereco = endereco;
    }
    public Pessoa( String cpf, Date dataNascimento, String login, String nome, 
            String numeroSUS, String registroGeral, String senha, Sexo sexo, Endereco endereco) {
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.login = login;
        this.nome = nome;
        this.numeroSUS = numeroSUS;
        this.registroGeral = registroGeral;
        this.senha = senha;
        this.sexo = sexo;
        this.endereco = endereco;
    }
    
    public String[] retornaUser() {
    	String[] user = {};
    	user[0] = "" + id_pessoa;
    	user[1] = "" + nome;
    	user[2] = "" + numeroSUS;
    	user[3] = "" + cpf;
    	user[4] = "" + dataNascimento;
    	user[5] = "" + login;
    	user[6] = "" + registroGeral;
    	user[7] = "" + senha;
    	user[8] = "" + sexo;
    	user[9] = "" + endereco;
    	return user;
    }

    public long getId() {
        return id_pessoa;
    }

    public void setId(long id) {
        this.id_pessoa = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroSUS() {
        return numeroSUS;
    }

    public void setNumeroSUS(String numeroSUS) {
        this.numeroSUS = numeroSUS;
    }

    public String getRegistroGeral() {
        return registroGeral;
    }

    public void setRegistroGeral(String registroGeral) {
        this.registroGeral = registroGeral;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String getUsuario() {
        return nome;
    }

}
