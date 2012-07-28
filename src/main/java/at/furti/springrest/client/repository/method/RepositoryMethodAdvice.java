package at.furti.springrest.client.repository.method;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.DataRestClient.ParameterType;
import at.furti.springrest.client.http.exception.RepositoryNotExposedException;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.RepositoryEntry;
import at.furti.springrest.client.util.JsonUtils;
import at.furti.springrest.client.util.RestCollectionUtils;

/**
 * @author Daniel
 * 
 */
public abstract class RepositoryMethodAdvice implements MethodAdvice {

	protected LinkManager linkManager;
	protected HttpMethod method;
	protected ParameterType paramType;
	protected RepositoryEntry entry;
	protected DataRestClient client;

	public RepositoryMethodAdvice(LinkManager linkManager, HttpMethod method,
			ParameterType paramType, RepositoryEntry entry, DataRestClient client) {
		Assert.notNull(linkManager, "LinkManager must not be null");
		Assert.notNull(method, "HttpMethod must not be null");
		Assert.notNull(paramType, "ParameterType must not be null");
		Assert.notNull(entry, "Entry must not be null");
		Assert.notNull(client, "Client must not be null");

		this.linkManager = linkManager;
		this.method = method;
		this.entry = entry;
		this.client = client;
		this.paramType = paramType;
	}

	public void advise(MethodInvocation invocation) {
		try {
			InputStream stream = executeMethod(invocation);

			handleResponse(invocation, JsonUtils.toJsonObject(stream));
		} catch (Exception e) {
			invocation.setCheckedException(e);
			invocation.rethrow();
		}
	}

	/**
	 * @return
	 */
	protected InputStream executeMethod(MethodInvocation invocation)
			throws IOException, RepositoryNotExposedException {
		String link = linkManager.getHref(entry.getRepoRel(),
				invocation.getMethod());

		// TODO: logging
		System.out.println(link);

		switch (method) {
		case DELETE:
			return client.executeDelete(RestCollectionUtils.toCollection(link));
		case POST:
			return client.executePost(RestCollectionUtils.toCollection(link));
		case GET:
			return client.executeGet(RestCollectionUtils.toCollection(link),
					paramType, getParameters(invocation));
		}

		return null;
	}

	protected RepositoryEntry getEntry() {
		return entry;
	}

	protected DataRestClient getClient() {
		return client;
	}

	protected abstract Object getParameters(MethodInvocation invocation);

	protected abstract void handleResponse(MethodInvocation invoaction,
			JSONObject response);
}
