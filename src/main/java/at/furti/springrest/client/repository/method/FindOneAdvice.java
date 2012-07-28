package at.furti.springrest.client.repository.method;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.DataRestClient.ParameterType;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.RepositoryEntry;
import at.furti.springrest.client.util.JsonUtils;
import at.furti.springrest.client.util.ReturnValueUtils;

public class FindOneAdvice extends RepositoryMethodAdvice {

	public FindOneAdvice(LinkManager linkManager, RepositoryEntry entry,
			DataRestClient client) {
		super(linkManager, HttpMethod.GET, ParameterType.PATH, entry, client);
	}

	@Override
	protected Object getParameters(MethodInvocation invocation) {
		Object parameter = invocation.getParameter(0);

		Assert.notNull(parameter, "Id should not be null");

		return parameter;
	}

	@Override
	protected void handleResponse(MethodInvocation invoaction, JSONObject response) {
		try {
			Object value = ReturnValueUtils.convertReturnValue(getEntry().getType(),
					response, getEntry().getRepoRel(), getClient());

			if (value != null) {
				JsonUtils.setId(value, getParameters(invoaction));
			}

			invoaction.setReturnValue(value);
		} catch (Exception ex) {
			invoaction.setCheckedException(ex);
			invoaction.rethrow();
		}
	}
}
