package at.furti.springrest.client.json.exception;

/**
 * Base class for all exceptions thrown in the json package.
 * 
 * @author Daniel Furtlehner
 *
 */
public class JsonException extends RuntimeException {

	private static final long serialVersionUID = 621217145325730224L;

	public JsonException() {
		super();
	}

	public JsonException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JsonException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonException(String message) {
		super(message);
	}

	public JsonException(Throwable cause) {
		super(cause);
	}
}
