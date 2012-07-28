package at.furti.springrest.client.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
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
	 *          String[] with key value pairs. must have an even number of entries
	 * @return
	 */
	protected String buildUrl(Collection<String> paths, ParameterType type,
			Object... parameters) {
		StringBuilder builder = new StringBuilder();

		if (basePathNeeded(paths)) {
			builder.append(basePath);
		}

		if (paths != null) {
			for (String path : paths) {
				if (StringUtils.isNotBlank(path)) {
					if (builder.length() > 0
							&& builder.lastIndexOf("/") != builder.length()
							&& !path.startsWith("/")) {
						builder.append("/");
					}

					builder.append(path);
				}
			}
		}

		appendParameters(builder, type, parameters);

		UriComponents components = UriComponentsBuilder.fromHttpUrl(builder.toString()).build();
		
		UriComponents encoded = components.encode();
		return encoded.toUriString();
	}

	/**
	 * @param url
	 * @param type
	 * @param parameters
	 */
	private void appendParameters(StringBuilder url, ParameterType type,
			Object... parameters) {
		if (type == ParameterType.NONE || parameters == null
				|| parameters.length == 0) {
			return;
		}

		switch (type) {
		case QUERY: {
			Assert.isTrue(parameters.length % 2 == 0,
					"Parameter array must have a even number of entries");

			// If the url does not end with an ? append it
			if (url.lastIndexOf("?") != url.length()) {
				url.append("?");
			}

			for (int i = 0; i < parameters.length; i += 2) {
				// Append a & after the first parameter
				if (i > 1) {
					url.append("&");
				}

				url.append(parameters[i]).append("=").append(parameters[i + 1]);
			}

			break;
		}
		case PATH: {
			for (Object o : parameters) {
				String parameter = o != null ? o.toString() : null;

				if (StringUtils.isNotBlank(parameter)) {
					if (url.length() > 0 && url.lastIndexOf("/") != url.length()
							&& !parameter.startsWith("/")) {
						url.append("/");
					}

					url.append(parameter);
				}
			}
			break;
		}
		}
	}

	/**
	 * @param paths
	 * @return
	 */
	private boolean basePathNeeded(Collection<String> paths) {
		if (CollectionUtils.isEmpty(paths)) {
			return true;
		}

		return !paths.iterator().next().startsWith("http");
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
