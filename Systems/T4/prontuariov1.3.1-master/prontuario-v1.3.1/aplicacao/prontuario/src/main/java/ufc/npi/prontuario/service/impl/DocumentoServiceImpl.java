package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ARQUIVO_EXISTENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CARREGAR_ARQUIVO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_SALVAR_ARQUIVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Documento;
import ufc.npi.prontuario.model.Documento.TipoDocumento;
import ufc.npi.prontuario.model.DocumentoDownload;
import ufc.npi.prontuario.model.GetPacienteId;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.repository.DocumentoRepository;
import ufc.npi.prontuario.repository.PacienteRepository;
import ufc.npi.prontuario.service.DocumentoService;

@Service
public class DocumentoServiceImpl implements DocumentoService {

	@Value("${documents.folder}")
	private String DOCUMENTOS_PRONTUARIO;

	@Autowired
	DocumentoRepository documentoRepository;

	@Autowired
	private PacienteRepository pacienteRepository;
////////////////////////////////////////////////////////////////
	@Override
	public void salvar(Paciente paciente, MultipartFile[] files) throws ProntuarioException {
		for (MultipartFile file : files) {
			if (verificarFile(file)) {
				Documento documento = new Documento();
				try {
					documento.setNomeArqCam(getOriginalFilename(file),returnBytesDoc(file),getCaminhoFile(file,paciente));
			

					// pega o fomato do arquivo
					int i = returnLastIndexNomeDoc(documento);
					String extensao = returnSubstringNomeDoc(documento,i);
					documento.setTipo(TipoDocumento.valueOf(returnExtensaoUpperCase(extensao)));

					for (Documento doc : paciente.getDocumentos()) {
						if (compararDoc(documento,doc)) {
							throw new ProntuarioException(ERRO_ARQUIVO_EXISTENTE);
						}
					}

					paciente.addDocumento(documento);
					pacienteRepository.save(paciente);
				} catch (IOException e) {
					throw new ProntuarioException(ERRO_SALVAR_ARQUIVO);
				}
				salvarArquivoLocal(documento, GetPacienteId.mostrarIdPaciente(paciente));
			}
		}
	}
	
	public String returnExtensaoUpperCase(String extensao) {
		return extensao.toUpperCase();
	}
	
	public String getOriginalFilename(MultipartFile file) {
		return file.getOriginalFilename();
	}
	
	public byte[] returnBytesDoc(MultipartFile file) throws IOException {
		return file.getBytes();
	}
	
	public String getCaminhoFile(MultipartFile file,Paciente paciente) {
		return DOCUMENTOS_PRONTUARIO + GetPacienteId.mostrarIdPaciente(paciente) + "/" + getOriginalFilename(file);
	}
	
	public boolean compararDoc(Documento documento,Documento doc) {
		return documento.getCaminho().equals(doc.getCaminho());
	}
	
	public int returnLastIndexNomeDoc(Documento documento) {
		return documento.getNome().lastIndexOf('.');
	}
	
	public String returnSubstringNomeDoc(Documento documento,int i) {
		return documento.getNome().substring(i + 1);
	}
	
	public boolean verificarFile(MultipartFile file) {
		return file != null && !(file.getOriginalFilename().toString().equals(""));
	}
///////////////////////////////////////////////////////////////////
	private void salvarArquivoLocal(Documento documento, Integer idPaciente) throws ProntuarioException {
		String caminhoDiretorio = DOCUMENTOS_PRONTUARIO + idPaciente;

		try {
			File arquivo = criarArquivo(caminhoDiretorio, documento.getNome());

			FileOutputStream fileOutputStream = new FileOutputStream(arquivo);
			fileOutputStream.write(documento.getArquivo());
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
			throw new ProntuarioException(ERRO_SALVAR_ARQUIVO);
		}
	}

	private File criarArquivo(String caminhoDiretorio, String nomeArquivo) throws IOException {
		File diretorio = new File(caminhoDiretorio);
		diretorio.mkdirs();

		File arquivo = new File(diretorio, nomeArquivo);
		arquivo.createNewFile();

		return arquivo;
	}

	public Documento buscarArquivo(Documento documento) throws ProntuarioException {
		FileInputStream fileInputStream = null;
		File file = new File(documento.getCaminho());
		byte[] bFile = new byte[(int) file.length()];

		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (IOException e) {
			throw new ProntuarioException(ERRO_CARREGAR_ARQUIVO);
		}

		documento.setArquivo(bFile);
		return documento;
	}

	public void deletar(Documento documento, Paciente paciente) {
		File file = new File(documento.getCaminho());
		file.delete();

		paciente.removerDocumento(documento);
		pacienteRepository.save(paciente);
		documentoRepository.delete(documento);
	}

	@Override
	public DocumentoDownload downloadDocumento(Documento documento, String procedimento) {
		String extensao = getExtensaoDocumento(documento);
		
		return new DocumentoDownload(documento.getArquivo(), documento.getNome(), procedimento, extensao);
	}
/////////////////////////////////////////////////////////////////////////
	private String getExtensaoDocumento(Documento documento) {
		String extensao = "";

		if(documentoPDF(documento)) {
			extensao = "application/" + getDocumentoDescricao(documento);
		} else {
			extensao = "image/" + getDocumentoDescricao(documento);
		}

		return extensao;
	}
	
	public String getDocumentoDescricao(Documento documento) {
		return documento.getTipo().getDescricao();
	}
	
	public boolean documentoPDF(Documento documento) {
		return documento.getTipo().equals(TipoDocumento.PDF);
	}
	///////////////////////////////////////////////////////////////////
}
