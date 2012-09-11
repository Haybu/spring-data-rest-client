package at.furti.springrest.client.repository.method;

import java.util.ArrayList;
import java.util.List;

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
	protected Request createReqest(String link, Object... params) {
		// If the method takes no arguments --> we want all entities from the
		// server
		if (params == null || params.length == 0) {
			return new Request(link);
		} else {
			/*
			 * Else we only want the entities with the ids from the iterable
			 */
			return new Request(link + "/" + params[0].toString());
		}
	}

	@Override
	protected void handleResponse(MethodInvocation invocation,
			List<Response> responses, String link) {
		Class<?>[] paramTypes = invocation.getMethod().getParameterTypes();

		if (paramTypes.length == 0) {
			handleAll(invocation, getFirstResponse(responses));
		} else {
			handleIds(invocation, responses);
		}
	}

	/**
	 * @param invoaction
	 * @param link
	 * @param paramTypes
	 */
	private void handleIds(MethodInvocation invoaction, List<Response> responses) {
		List<Object> ret = new ArrayList<Object>();

		try {
			for (Response response : responses) {
				JSONObject data = JsonUtils.toJsonObject(response.getBody());

				ret.add(ReturnValueUtils.convertCollection(entry.getType(),
						data, entry.getRepoRel(), getClient()));
			}
		} catch (Exception ex) {
			invoaction.setCheckedException(ex);
			invoaction.rethrow();
		}

		invoaction.setReturnValue(ret);

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
				JSONObject data = JsonUtils.toJsonObject(response.getBody());

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
