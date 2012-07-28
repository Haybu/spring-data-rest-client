package at.furti.springrest.client.http;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class RestClientFactoryBean extends AbstractFactoryBean<HttpClient> {

	private String scheme;
	private String hostname;
	private Integer port;

	@Override
	public Class<?> getObjectType() {
		return HttpClient.class;
	}

	@Override
	protected HttpClient createInstance() throws Exception {
		HttpHost host = null;

		if (hostname != null) {
			if (port != null && scheme != null) {
				host = new HttpHost(hostname, port.intValue(), scheme);
			} else if (port != null) {
				host = new HttpHost(hostname, port.intValue());
			} else {
				host = new HttpHost(hostname);
			}
		}

		RestClientImpl client = new RestClientImpl(host);

		return client;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
}
