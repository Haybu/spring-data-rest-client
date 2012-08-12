package at.furti.springrest.client.repository.method;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;

import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.json.JsonUtils;
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
		return new Request(link);
	}

	@Override
	protected void handleResponse(MethodInvocation invoaction, Response response) {
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
