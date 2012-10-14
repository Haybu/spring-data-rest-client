package at.furti.springrest.client.repository.method;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import at.furti.springrest.client.base.ClientAware;
import at.furti.springrest.client.config.RepositoryEntry;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.exception.NotExportedException;

/**
 * @author Daniel
 * 
 */
public abstract class RepositoryMethodAdvice extends ClientAware implements
		MethodAdvice {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected LinkManager linkManager;
	protected HttpMethod method;
	protected RepositoryEntry entry;

	public RepositoryMethodAdvice(LinkManager linkManager, HttpMethod method,
			RepositoryEntry entry, DataRestClient client) {
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
			String link = linkManager.getHref(entry.getRepoRel(),
					invocation.getMethod());

			logger.debug("Using href [{}] for methodcall", link);

			Object[] params = getParameters(invocation);

			List<Response> responses = new ArrayList<Response>();

			if (shouldIterate(params)) {
				Iterator<?> it = getIterator(params);

				while (it.hasNext()) {
					responses.add(executeMethod(link, it.next()));
				}
			} else {
				responses.add(executeMethod(link, params));
			}

			handleResponse(invocation, responses, link);
		} catch (Exception e) {
			invocation.setCheckedException(e);
			invocation.rethrow();
		}
	}

	private Iterator<?> getIterator(Object[] params) {
		return ((Iterable<?>) params[0]).iterator();
	}

	/**
	 * @param params
	 * @return
	 */
	private boolean shouldIterate(Object[] params) {
		if (params == null || params.length != 1) {
			return false;
		}

		Object firstParam = params[0];

		return Iterable.class.isAssignableFrom(firstParam.getClass());
	}

	/**
	 * @return
	 */
	protected Response executeMethod(String link, Object... params)
			throws Exception {
		Request request = createReqest(link, params);

		if (request == null) {
			return null;
		}

		Response response = null;

		switch (method) {
		case DELETE:
			response = execute(RequestType.DELETE, request);
			break;
		case POST:
			response = execute(RequestType.POST, request);
			break;
		case GET:
			response = execute(RequestType.GET, request);
			break;
		default:
			break;
		}

		return checkStatus(response);
	}

	/**
	 * @param response
	 * @return
	 */
	private Response checkStatus(Response response) {
		if (response != null) {
			switch (response.getStatus()) {
			case NO_CONTENT:
				return null;
			case METHOD_NOT_ALLOWED:
				throw new NotExportedException("Method is not exported");
			default:
				break;
			}
		}

		return response;
	}

	protected RepositoryEntry getEntry() {
		return entry;
	}

	/**
	 * @param responses
	 * @return
	 */
	protected Response getFirstResponse(List<Response> responses) {
		if (responses == null || responses.size() < 1) {
			return null;
		}

		return responses.get(0);
	}

	/**
	 * @param invocation
	 * @return
	 */
	private Object[] getParameters(MethodInvocation invocation) {
		Method m = invocation.getMethod();

		Class<?>[] paramTypes = m.getParameterTypes();

		if (paramTypes.length > 0) {
			Object[] params = new Object[paramTypes.length];

			for (int i = 0; i < paramTypes.length; i++) {
				params[i] = invocation.getParameter(i);
			}

			return params;
		}

		return null;
	}

	protected abstract Request createReqest(String link, Object... params)
			throws Exception;

	protected abstract void handleResponse(MethodInvocation invocation,
			List<Response> responses, String link);
}
