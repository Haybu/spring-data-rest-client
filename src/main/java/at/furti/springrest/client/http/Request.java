package at.furti.springrest.client.http;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.tapestry5.json.JSONObject;
import org.springframework.util.CollectionUtils;

/**
 * @author Daniel Furtlehner
 * 
 */
public class Request {
	public enum ParameterType {
		QUERY, PATH
	}

	private String path;
	private Map<String, Object> queryParameters;
	private Map<PathParameterKey, Object> pathParameters;

	public Request() {
		this("");
	}

	public Request(String path) {
		this.path = path;
	}

	/**
	 * 
	 */
	public String toString() {
		JSONObject object = new JSONObject();

		object.put("url", getUrl());

		return object.toString();
	}

	/**
	 * Adds a parameter to the request.
	 * 
	 * Queryparameters are appened to the url. To add a path parameter the path
	 * must contain a placeholder for the parameter e.g.:
	 * testserver/person/{personId} if you add a PathParameter with the name
	 * "personId" to the request, the placeholder will be replaced with the
	 * value of the parameter if the getUrl method is called.
	 * 
	 * @param type
	 * @param name
	 * @param value
	 */
	public Request addParameter(ParameterType type, String name, Object value) {
		switch (type) {
		case QUERY:
			if (queryParameters == null) {
				queryParameters = new HashMap<String, Object>();
			}

			// TODO: should support same parameter more than once??
			queryParameters.put(name, value);

			break;

		case PATH:
			if (pathParameters == null) {
				pathParameters = new HashMap<PathParameterKey, Object>();
			}
			pathParameters.put(new PathParameterKey(name), value);

			break;
		default:
			throw new IllegalArgumentException(
					"type [" + type != null ? type.toString() : "null"
							+ "] not supportet");
		}

		return this;
	}

	public String getUrl() {
		StringBuilder builder = new StringBuilder();

		builder.append(processPathParameters());

		appendQueryParameters(builder);

		return builder.toString();
	}

	/**
	 * @param builder
	 */
	private void appendQueryParameters(StringBuilder builder) {
		if (CollectionUtils.isEmpty(queryParameters)) {
			return;
		}

		boolean questionMarkNeeded = true;

		for (String name : queryParameters.keySet()) {
			if (questionMarkNeeded) {
				builder.append("?");
				questionMarkNeeded = false;
			} else {
				builder.append("&");
			}

			builder.append(name).append("=").append(queryParameters.get(name));
		}
	}

	/**
	 * @return
	 */
	private String processPathParameters() {
		if (CollectionUtils.isEmpty(pathParameters)) {
			return path;
		}

		String newPath = path;

		for (PathParameterKey key : pathParameters.keySet()) {
			newPath = key.getPattern().matcher(newPath)
					.replaceAll(pathParameters.get(key).toString());
		}

		return newPath;
	}

	/**
	 * @author Daniel Furtlehner
	 * 
	 */
	private class PathParameterKey {
		private String name;
		private Pattern pattern;

		public PathParameterKey(String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			int hash = 1;

			if (name != null) {
				hash *= name.hashCode();
			}

			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PathParameterKey) {
				return ObjectUtils.equals(name,
						((PathParameterKey) obj).getName());
			}

			return false;
		}

		public String getName() {
			return name;
		}

		public Pattern getPattern() {
			if (pattern == null) {
				pattern = Pattern.compile("\\{" + name + "\\}");
			}

			return pattern;
		}
	}
}
