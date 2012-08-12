package at.furti.springrest.client.repository.method;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;

import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.Link;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.json.JsonUtils;
import at.furti.springrest.client.repository.lazy.LazyInitializingIterable;
import at.furti.springrest.client.util.ReturnValueUtils;

/**
 * @author Daniel
 * 
 */
public class FindAllMethodAdvice extends RepositoryMethodAdvice {

	public FindAllMethodAdvice(LinkManager linkManager, RepositoryConfig entry,
			DataRestClient client) {
		super(linkManager, HttpMethod.GET, entry, client);
	}

	@Override
	protected Request createReqest(String link, MethodInvocation invocation) {
		// If the method takes no arguments --> we want all entities from the
		// server
		if (invocation.getMethod().getParameterTypes().length == 0) {
			return new Request(link);
		} else {
			/*
			 * Else we only want the entities with the ids from the iterable
			 * 
			 * So no request is needed. The handleResponse mehtod initializes a
			 * Lazyloadingiterable that has the required links.
			 */
			return null;
		}
	}

	@Override
	protected void handleResponse(MethodInvocation invocation,
			Response response, String link) {
		Class<?>[] paramTypes = invocation.getMethod().getParameterTypes();

		if (paramTypes.length == 0) {
			handleAll(invocation, response);
		} else {
			handleIds(invocation, link, paramTypes);
		}
	}

	/**
	 * @param invoaction
	 * @param link
	 * @param paramTypes
	 */
	private void handleIds(MethodInvocation invoaction, String link,
			Class<?>[] paramTypes) {
		if (paramTypes.length > 1) {
			throw new IllegalArgumentException(
					"Method should take one or zero parameters");
		}

		Object o = invoaction.getParameter(0);

		if (!Iterable.class.isAssignableFrom(o.getClass())) {
			throw new IllegalArgumentException(
					"The method parameter should be a instance of iterable");
		}

		Iterator<?> it = ((Iterable<?>) o).iterator();

		List<Link> links = new ArrayList<Link>();

		String entityRel = getEntry().getRepoRel() + "."
				+ getEntry().getType().getSimpleName();

		while (it.hasNext()) {
			Object id = it.next();

			links.add(new Link(entityRel, link + "/" + id.toString()));
		}

		invoaction.setReturnValue(new LazyInitializingIterable(links,
				getEntry().getRepoRel(), getClient(), getEntry().getType()));
	}

	/**
	 * @param invoaction
	 * @param response
	 */
	private void handleAll(MethodInvocation invoaction, Response response) {
		if (response == null) {
			invoaction.setReturnValue(null);
		} else {
			try {
				JSONObject data = JsonUtils.toJsonObject(response.getStream());

				invoaction
						.setReturnValue(ReturnValueUtils.convertCollection(
								entry.getType(), data, entry.getRepoRel(),
								getClient()));
			} catch (Exception ex) {
				invoaction.setCheckedException(ex);
				invoaction.rethrow();
			}
		}
	}
}
