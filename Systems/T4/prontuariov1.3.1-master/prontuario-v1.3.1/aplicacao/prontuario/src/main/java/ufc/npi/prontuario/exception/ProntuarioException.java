package ufc.npi.prontuario.exception;

public class ProntuarioException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private String message;

	public ProntuarioException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
