package at.furti.springrest.client.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class DataRestClientBase implements DataRestClient {

	// private LinkManager linkManager;
	private Map<String, String> headers;
	private String basePath;

	public DataRestClientBase(String basePath) {
		Assert.notNull(basePath);

		this.basePath = basePath;

		headers = new HashMap<String, String>();
		headers.put("accept", "application/json");
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// * at.furti.springrest.client.http.DataRestClient#executeMethod(java.lang.
	// * String, java.lang.reflect.Method)
	// */
	// public InputStream executeMethod(String repoRel, Method m,
	// Object... parameters) throws RepositoryNotExposedException, IOException {
	// String link = linkManager.getHref(repoRel, m);
	//
	// switch (RepositoryUtils.getRequestType(m)) {
	// case DELETE:
	// return executeDelete(RestCollectionUtils.toCollection(link));
	// case POST:
	// return executePost(RestCollectionUtils.toCollection(link));
	// case GET:
	// return executeGet(RestCollectionUtils.toCollection(link));
	// }
	//
	// return null;
	// }

	/**
	 * Builds the url for the path.
	 * 
	 * Concatenates the basePath and the path.
	 * 
	 * @param path
	 * @param parameters
	 *            String[] with key value pairs. must have an even number of
	 *            entries
	 * @return
	 */
	protected String buildUrl(Request request) {
		StringBuilder builder = new StringBuilder();
		String url = request.getUrl();

		if (basePathNeeded(url)) {
			builder.append(basePath);

			if (!basePath.endsWith("/") && !url.startsWith("/")) {
				builder.append("/");
			}
		}

		if (url != null) {
			builder.append(url);
		}

		UriComponents components = UriComponentsBuilder.fromHttpUrl(
				builder.toString()).build();

		UriComponents encoded = components.encode();
		return encoded.toUriString();
	}

	/**
	 * @param paths
	 * @return
	 */
	private boolean basePathNeeded(String url) {
		if (StringUtils.isEmpty(url)) {
			return true;
		}

		return !url.startsWith("http");
	}

	/**
	 * @return
	 */
	protected Map<String, String> getRequiredHeaders() {
		return headers;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}
