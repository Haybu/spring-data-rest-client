package at.furti.springrest.client.http;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class Response {

	private byte[] body;
	private Map<String, String> headers;
	private HttpStatus status;

	public Response(byte[] body, HttpStatus status) {
		super();
		this.body = body;
		this.status = status;
	}

	public byte[] getBody() {
		return body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getHeader(String name) {
		if (headers == null) {
			return null;
		}

		return headers.get(name);
	}

	public void setHeader(String name, String value) {
		if (headers == null) {
			this.headers = new HashMap<String, String>();
		}

		this.headers.put(name, value);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
