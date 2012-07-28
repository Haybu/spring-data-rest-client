package at.furti.springrest.client.repository.method;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.DataRestClient.ParameterType;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.RepositoryEntry;
import at.furti.springrest.client.util.ReturnValueUtils;

/**
 * @author Daniel
 * 
 */
public class FindAllMethodAdvice extends RepositoryMethodAdvice {

	public FindAllMethodAdvice(LinkManager linkManager, RepositoryEntry entry,
			DataRestClient client) {
		super(linkManager, HttpMethod.GET, ParameterType.NONE, entry, client);
	}

	@Override
	protected Object getParameters(MethodInvocation invocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void handleResponse(MethodInvocation invoaction, JSONObject response) {
		// TODO Auto-generated method stub

	}
}
