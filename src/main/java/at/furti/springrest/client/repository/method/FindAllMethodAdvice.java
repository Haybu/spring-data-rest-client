package at.furti.springrest.client.repository.method;

import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;

import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void handleResponse(MethodInvocation invoaction, Response response) {
		// TODO Auto-generated method stub

	}
}
