package at.furti.springrest.client.base;

import java.io.IOException;

import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.json.JsonUtils;

public abstract class ClientAware {

	public enum RequestType {
		GET, POST, DELETE
	}

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private DataRestClient client;

	public ClientAware(DataRestClient client) {
		Assert.notNull(client, "Client must not be null");

		this.client = client;
	}

	/**
	 * Executes a get Request to the server and returns the response body as
	 * jsonobject.
	 * 
	 * @param url
	 * @return
	 */
	protected JSONObject getObjectFromServer(String url) throws IOException {
		Response response = execute(RequestType.GET, new Request(url));

		return response == null ? null : JsonUtils.toJsonObject(response
				.getStream());
	}

	/**
	 * @param type
	 * @param request
	 * @return
	 * @throws IOException
	 */
	protected Response execute(RequestType type, Request request)
			throws IOException {
		switch (type) {
		case GET:
			return client.executeGet(request);
		case DELETE:
			return client.executeDelete(request);
		case POST:
			return client.executePost(request);
		default:
			throw new IllegalArgumentException("Type " + type
					+ " not rekognized");
		}
	}

	public DataRestClient getClient() {
		return client;
	}
}
