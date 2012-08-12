package at.furti.springrest.client.repository.method;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Request.ParameterType;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.json.JsonUtils;
import at.furti.springrest.client.util.ReturnValueUtils;

public class FindOneAdvice extends RepositoryMethodAdvice {

	public FindOneAdvice(LinkManager linkManager, RepositoryConfig entry,
			DataRestClient client) {
		super(linkManager, HttpMethod.GET, entry, client);
	}

	@Override
	protected Request createReqest(String link, MethodInvocation invocation) {
		Object parameter = invocation.getParameter(0);

		Assert.notNull(parameter, "Id should not be null");

		return new Request(link + "/{id}").addParameter(ParameterType.PATH,
				"id", parameter);
	}

	@Override
	protected void handleResponse(MethodInvocation invoaction,
			Response response, String link) {
		if (response == null || response.getStream() == null) {
			invoaction.setReturnValue(null);
		} else {

			try {
				JSONObject data = JsonUtils.toJsonObject(response.getStream());

				if (data == null) {
					invoaction.setReturnValue(null);
				} else {
					Object value = ReturnValueUtils.convertReturnValue(
							getEntry().getType(), data,
							getEntry().getRepoRel(), getClient());

					invoaction.setReturnValue(value);
				}
			} catch (Exception ex) {
				invoaction.setCheckedException(ex);
				invoaction.rethrow();
			}
		}
	}
}
