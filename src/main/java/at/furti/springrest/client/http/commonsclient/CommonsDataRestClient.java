package at.furti.springrest.client.http.commonsclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
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
	 * at.furti.springrest.client.http.DataRestClient#executeGet(java.lang.String)
	 */
	public InputStream executeGet(Collection<String> paths, ParameterType type,
			Object... parameters) throws IOException {
		String urlToUse = buildUrl(paths, type, parameters);

		HttpGet request = new HttpGet(urlToUse);

		setupRequest(request);

		try {
			HttpResponse response = client.execute(request);

			return response.getEntity().getContent();
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
	 * at.furti.springrest.client.http.DataRestClient#executePost(java.lang.String
	 * )
	 */
	public InputStream executePost(Collection<String> paths) throws IOException {
		String urlToUse = buildUrl(paths, ParameterType.NONE);

		HttpPost request = new HttpPost(urlToUse);

		setupRequest(request);

		try {
			HttpResponse response = client.execute(request);

			return response.getEntity().getContent();
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
	public InputStream executeDelete(Collection<String> paths) throws IOException {
		String urlToUse = buildUrl(paths, ParameterType.NONE);

		HttpDelete request = new HttpDelete(urlToUse);

		setupRequest(request);

		try {
			HttpResponse response = client.execute(request);

			return response.getEntity().getContent();
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
			request.setHeader(new BasicHeader(headerName, headers.get(headerName)));
		}
	}
}
