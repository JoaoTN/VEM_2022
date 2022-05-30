package ufc.npi.prontuario.model;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class DocumentoDownload extends HttpEntity<byte[]>{

	private HttpEntity<byte[]> httpEntity;
	
	public DocumentoDownload(byte[] arquivo, String nomeArquivo, String procedimento, String tipoArquivo){
		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.add("Content-disposition", procedimento + "; filename=\""+nomeArquivo+"\"");
        httpHeaders.setContentType(MediaType.parseMediaType(tipoArquivo));
        httpEntity = new HttpEntity<byte[]>(arquivo, httpHeaders);
	}
	
	public HttpHeaders getHeaders() {
        return httpEntity.getHeaders();
    }
 
    public byte[] getBody() {
        return httpEntity.getBody();
    }
 
    public boolean hasBody() {
        return httpEntity.hasBody();
    }
 
    public boolean equals(Object other) {
        return httpEntity.equals(other);
    }
 
    public int hashCode() {
        return httpEntity.hashCode();
    }
 
    public String toString() {
        return httpEntity.toString();
    }
}
