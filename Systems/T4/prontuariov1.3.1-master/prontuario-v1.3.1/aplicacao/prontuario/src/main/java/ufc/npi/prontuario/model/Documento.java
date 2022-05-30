package ufc.npi.prontuario.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Documento {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Transient
	private byte[] arquivo;
	
	@NotNull
	private String nome;
	
	@NotNull
	private String caminho;
	
	@Enumerated(EnumType.STRING)
	private TipoDocumento tipo;

	public Documento() {
		super();
	}

	public Documento(byte[] arquivo, String nome, String caminho) {
		super();
		this.arquivo = arquivo;
		this.nome = nome;
		this.caminho = caminho;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	
	public TipoDocumento getTipo() {
		return tipo;
	}

	public void setTipo(TipoDocumento tipo) {
		this.tipo = tipo;
	}
	///////////////////////////////
	public void setNomeArqCam(String nome,byte[] arquivo,String caminho) {
		this.arquivo=arquivo;
		this.caminho=caminho;
		this.nome=nome;
	}
	//////////////////////////////
	
	public enum TipoDocumento {
		PDF("pdf"), JPG("jpg"), JPEG("jpeg"), PNG("png"), GIF("gif");
		
		private String descricao;
		
		private TipoDocumento(String descricao){
			this.descricao = descricao;
		}
		
		public String getDescricao() {
			return descricao;
		}
	}
}
