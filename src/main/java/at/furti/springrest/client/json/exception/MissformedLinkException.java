package at.furti.springrest.client.json.exception;

/**
 * Exception is thrown if a link object does not contain a rel and href field.
 * 
 * @author Daniel Furtlehner
 *
 */
public class MissformedLinkException extends JsonException {
	private static final long serialVersionUID = -3683639247523608361L;

	public MissformedLinkException() {
	}

	public MissformedLinkException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissformedLinkException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissformedLinkException(String message) {
		super(message);
	}

	public MissformedLinkException(Throwable cause) {
		super(cause);
	}
}
