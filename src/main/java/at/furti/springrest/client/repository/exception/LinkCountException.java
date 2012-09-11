package at.furti.springrest.client.repository.exception;

/**
 * Is thrown if we read links from the server but we get not the expected number of links.
 * 
 * e.g. if we want to lazy initialize a object, we expect to get a single link from the server.
 * If we get more than one link, throw a exception because we do not know what to do.
 * @author Daniel Furtlehner
 *
 */
public class LinkCountException extends RuntimeException {

	private static final long serialVersionUID = -5797131794138171283L;

	public LinkCountException() {
		super();
	}

	public LinkCountException(String message, Throwable cause) {
		super(message, cause);
	}

	public LinkCountException(String message) {
		super(message);
	}

	public LinkCountException(Throwable cause) {
		super(cause);
	}
}
