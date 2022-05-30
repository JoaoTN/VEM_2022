package br.ufc.quixada.npi.gestaocompetencia.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufc.quixada.npi.gestaocompetencia.model.enums.Lotacao;
import br.ufc.quixada.npi.gestaocompetencia.model.enums.VinculoProfissional;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.*;

@Entity
public class Usuario implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	private String nome;
	
	private String nomeSocial;
	
	@NotEmpty
	@Column(unique = true)
	private String email;
	
	@NotEmpty
	@JsonIgnore
	private String password;

	private boolean habilitado;
	
	@ManyToOne
	@JoinColumn(name = "unidade_id")
//	@JsonBackReference	
	private Unidade unidade;
	
	private String cargo;
	private String funcao;
	
	@Enumerated(EnumType.STRING)
	private VinculoProfissional vinculo;
	
	@Enumerated(EnumType.STRING)
	private Lotacao lotacao;
	
	private String imagem;
	private String siape;
	private Date dataNasc;
	private String sexo;
	private String deficiencia;
	private String endereco;
	private String enderecoFuncional;
	private String telefone;
	private String telefoneFuncional;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "perfil_id")
	private Perfil perfil;

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public Lotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(Lotacao lotacao) {
		this.lotacao = lotacao;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
	
	public String getNomeSocial() {
		return nomeSocial;
	}

	public void setNomeSocial(String nomeSocial) {
		this.nomeSocial = nomeSocial;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

	public Date getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getDeficiencia() {
		return deficiencia;
	}

	public void setDeficiencia(String deficiencia) {
		this.deficiencia = deficiencia;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEnderecoFuncional() {
		return enderecoFuncional;
	}

	public void setEnderecoFuncional(String enderecoFuncional) {
		this.enderecoFuncional = enderecoFuncional;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getTelefoneFuncional() {
		return telefoneFuncional;
	}

	public void setTelefoneFuncional(String telefoneFuncional) {
		this.telefoneFuncional = telefoneFuncional;
	}

	public VinculoProfissional getVinculo() {
		return vinculo;
	}

	public void setVinculo(VinculoProfissional vinculo) {
		this.vinculo = vinculo;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.habilitado;
	}

	public List<String> getRoles() {
			return new ArrayList<>();
	}

	public boolean isSubordinado(Usuario usuario) {
		return unidade.getChefe().getId().equals(this.getId()) &&
				usuario.getUnidade().getId().equals(unidade.getId());
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", nomeSocial=" + nomeSocial + ", email=" + email
				+ ", password=" + password + ", habilitado=" + habilitado + ", unidade=" + unidade + ", cargo=" + cargo
				+ ", funcao=" + funcao + ", siape=" + siape + ", dataNasc=" + dataNasc + ", sexo=" + sexo
				+ ", deficiencia=" + deficiencia + ", endereco=" + endereco + ", enderecoFuncional=" + enderecoFuncional
				+ ", telefone=" + telefone + ", ramal=" + "]";
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Usuario usuario = (Usuario) o;
		return id.equals(usuario.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
