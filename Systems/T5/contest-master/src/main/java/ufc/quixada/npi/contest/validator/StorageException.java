package ufc.quixada.npi.contest.validator;
public class StorageException extends RuntimeException {

	private static final long serialVersionUID = -5815953696855501562L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}