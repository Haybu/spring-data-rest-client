package at.furti.springrest.client.repository.exception;

public class NotExportedException extends RepositoryException {

	private static final long serialVersionUID = 2326840308556443489L;

	public NotExportedException() {
		super();
	}

	public NotExportedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotExportedException(String message) {
		super(message);
	}

	public NotExportedException(Throwable cause) {
		super(cause);
	}
}
