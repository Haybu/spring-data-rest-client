package at.furti.springrest.client.repository.method;

import java.io.IOException;

import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import at.furti.springrest.client.base.ClientAware;
import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.exception.RepositoryNotExposedException;
import at.furti.springrest.client.http.link.LinkManager;

/**
 * @author Daniel
 * 
 */
public abstract class RepositoryMethodAdvice extends ClientAware implements
		MethodAdvice {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected LinkManager linkManager;
	protected HttpMethod method;
	protected RepositoryConfig entry;

	public RepositoryMethodAdvice(LinkManager linkManager, HttpMethod method,
			RepositoryConfig entry, DataRestClient client) {
		super(client);

		Assert.notNull(linkManager, "LinkManager must not be null");
		Assert.notNull(method, "HttpMethod must not be null");
		Assert.notNull(entry, "Entry must not be null");

		this.linkManager = linkManager;
		this.method = method;
		this.entry = entry;
	}

	public void advise(MethodInvocation invocation) {
		try {
			Response response = executeMethod(invocation);

			handleResponse(invocation, response);
		} catch (Exception e) {
			invocation.setCheckedException(e);
			invocation.rethrow();
		}
	}

	/**
	 * @return
	 */
	protected Response executeMethod(MethodInvocation invocation)
			throws IOException, RepositoryNotExposedException {
		String link = linkManager.getHref(entry.getRepoRel(),
				invocation.getMethod());

		logger.debug("Using href [{}] for methodcall", link);

		switch (method) {
		case DELETE:
			return execute(RequestType.DELETE, createReqest(link, invocation));
		case POST:
			return execute(RequestType.POST, createReqest(link, invocation));
		case GET:
			return execute(RequestType.GET, createReqest(link, invocation));
		}

		return null;
	}

	protected RepositoryConfig getEntry() {
		return entry;
	}

	protected abstract Request createReqest(String link,
			MethodInvocation invocation);

	protected abstract void handleResponse(MethodInvocation invoaction,
			Response response);
}
