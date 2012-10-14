package at.furti.springrest.client.repository.method;

import java.util.List;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import at.furti.springrest.client.config.RepositoryEntry;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Request.ParameterType;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.json.JsonUtils;
import at.furti.springrest.client.util.ReturnValueUtils;

public class FindOneAdvice extends RepositoryMethodAdvice {

	public FindOneAdvice(LinkManager linkManager, RepositoryEntry entry,
			DataRestClient client) {
		super(linkManager, HttpMethod.GET, entry, client);
	}

	@Override
	protected Request createReqest(String link, Object... params) {
		Object parameter = params[0];

		Assert.notNull(parameter, "Id should not be null");

		return new Request(link + "/{id}").addParameter(ParameterType.PATH,
				"id", parameter);
	}

	@Override
	protected void handleResponse(MethodInvocation invoaction,
			List<Response> responses, String link) {
		Response response = getFirstResponse(responses);

		if (response == null || response.getBody() == null) {
			invoaction.setReturnValue(null);
		} else {

			try {
				JSONObject data = JsonUtils.toJsonObject(response.getBody());

				if (data == null) {
					invoaction.setReturnValue(null);
				} else {
					Object value = ReturnValueUtils.convertReturnValue(
							getEntry().getEntityType(), data, getEntry()
									.getRepoRel(), getClient());

					invoaction.setReturnValue(value);
				}
			} catch (Exception ex) {
				invoaction.setCheckedException(ex);
				invoaction.rethrow();
			}
		}
	}
}
