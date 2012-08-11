package at.furti.springrest.client.http;

import java.io.InputStream;

public class Response {

	private InputStream stream;

	public Response(InputStream stream) {
		super();
		this.stream = stream;
	}

	public InputStream getStream() {
		return stream;
	}
}
