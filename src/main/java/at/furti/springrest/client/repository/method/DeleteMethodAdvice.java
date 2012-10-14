package at.furti.springrest.client.repository.method;

import java.util.List;

import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;

import at.furti.springrest.client.config.RepositoryEntry;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Request.ParameterType;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.util.IdentifierUtils;

/**
 * @author Daniel
 * 
 */
public class DeleteMethodAdvice extends RepositoryMethodAdvice {

	public DeleteMethodAdvice(LinkManager linkManager, RepositoryEntry entry,
			DataRestClient client) {
		super(linkManager, HttpMethod.DELETE, entry, client);
	}

	@Override
	protected Request createReqest(String link, Object... params) {
		if (Iterable.class.isAssignableFrom(params[0].getClass())
				|| getEntry().getEntityType().isAssignableFrom(params[0].getClass())) {
			return new Request(IdentifierUtils.getIdentifier(params[0]));
		} else {
			return new Request(link + "/{id}").addParameter(ParameterType.PATH,
					"id", params[0]);
		}
	}

	@Override
	protected void handleResponse(MethodInvocation invocation,
			List<Response> responses, String link) {
		// Nothing to do here
	}
}