package at.furti.springrest.client.http.commonsclient;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import at.furti.springrest.client.http.DataRestClientBase;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;

/**
 * @author Daniel
 * 
 */
public class CommonsDataRestClient extends DataRestClientBase {

	private HttpClient client;

	public CommonsDataRestClient(String basePath) {
		super(basePath);
		this.client = new DefaultHttpClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.furti.springrest.client.http.DataRestClient#executeGet(java.lang.String
	 * )
	 */
	public Response executeGet(Request request) throws IOException {
		String urlToUse = buildUrl(request);

		HttpGet getRequest = new HttpGet(urlToUse);

		setupRequest(getRequest);

		try {
			HttpResponse response = client.execute(getRequest);

			return new Response(response.getEntity().getContent());
		} catch (ClientProtocolException ex) {
			// TODO: rethrow the exception with springrest exception
			ex.printStackTrace();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.furti.springrest.client.http.DataRestClient#executePost(java.lang.
	 * String )
	 */
	public Response executePost(Request request) throws IOException {
		String urlToUse = buildUrl(request);

		HttpPost postRequest = new HttpPost(urlToUse);

		setupRequest(postRequest);

		try {
			HttpResponse response = client.execute(postRequest);

			return new Response(response.getEntity().getContent());
		} catch (ClientProtocolException ex) {
			// TODO: rethrow the exception with springrest exception
			ex.printStackTrace();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.furti.springrest.client.http.DataRestClient#executeDelete(java.lang.
	 * String)
	 */
	public Response executeDelete(Request request) throws IOException {
		String urlToUse = buildUrl(request);

		HttpDelete deleteRequest = new HttpDelete(urlToUse);

		setupRequest(deleteRequest);

		try {
			HttpResponse response = client.execute(deleteRequest);

			return new Response(response.getEntity().getContent());
		} catch (ClientProtocolException ex) {
			// TODO: rethrow the exception with springrest exception
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Set the required headers for the request
	 * 
	 * @param request
	 */
	private void setupRequest(HttpRequestBase request) {
		Map<String, String> headers = getRequiredHeaders();

		if (headers == null) {
			return;
		}

		for (String headerName : headers.keySet()) {
			request.setHeader(new BasicHeader(headerName, headers
					.get(headerName)));
		}
	}
}
