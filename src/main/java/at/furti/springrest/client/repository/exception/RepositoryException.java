package at.furti.springrest.client.repository.exception;

public class RepositoryException extends RuntimeException {

	private static final long serialVersionUID = 1639077416831702577L;

	public RepositoryException() {
		super();
	}

	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoryException(String message) {
		super(message);
	}

	public RepositoryException(Throwable cause) {
		super(cause);
	}

}
