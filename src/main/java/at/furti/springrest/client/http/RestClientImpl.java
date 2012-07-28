package at.furti.springrest.client.http;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

public class RestClientImpl extends DefaultHttpClient {

	private HttpHost host = null;

	public RestClientImpl() {

	}

	public RestClientImpl(HttpHost host) {
		this.host = host;
	}

	@Override
	public <T> T execute(HttpUriRequest request,
			ResponseHandler<? extends T> responseHandler, HttpContext context)
			throws IOException, ClientProtocolException {
		if (host != null) {
			return super.execute(host, request, responseHandler, context);
		} else {
			return super.execute(request, responseHandler, context);
		}
	}
}
