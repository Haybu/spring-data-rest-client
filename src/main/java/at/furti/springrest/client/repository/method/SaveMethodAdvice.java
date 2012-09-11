package at.furti.springrest.client.repository.method;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.json.EntityToJsonWorker;
import at.furti.springrest.client.util.IdentifierUtils;
import at.furti.springrest.client.util.ReturnValueUtils;

public class SaveMethodAdvice extends RepositoryMethodAdvice {

	public SaveMethodAdvice(LinkManager linkManager, RepositoryConfig entry,
			DataRestClient client) {
		super(linkManager, HttpMethod.POST, entry, client);
	}

	@Override
	protected Request createReqest(String link, Object... params)
			throws Exception {
		Object param = params[0];

		Request request = null;

		// Single object should be saved.

		String identifier = IdentifierUtils.getIdentifier(param);

		if (identifier == null) {
			request = new Request(link);
		} else {
			request = new Request(identifier);
		}

		JSONObject object = new EntityToJsonWorker(param, null).toJson();

		request.setBody(object.toCompactString().getBytes(
				Charset.forName("UTF-8")));

		return request;
	}

	@Override
	protected void handleResponse(MethodInvocation invocation,
			List<Response> responses, String link) {
		if (CollectionUtils.isEmpty(responses)) {
			invocation.setReturnValue(null);
		} else {
			try {
				Class<?> returnType = invocation.getMethod().getReturnType();

				if (Iterable.class.isAssignableFrom(returnType)) {
					List<Object> ret = new ArrayList<Object>();

					for (Response response : responses) {
						ret.add(getObject(response));
					}
					invocation.setReturnValue(ret);
				} else {
					invocation
							.setReturnValue(getObject(getFirstResponse(responses)));
				}
			} catch (Exception ex) {
				invocation.setCheckedException(ex);
				invocation.rethrow();
			}
		}
	}

	/**
	 * @param firstResponse
	 * @return
	 */
	private Object getObject(Response response) throws Exception {
		String url = response != null ? response.getHeader("Location") : null;

		if (url == null) {
			return null;
		} else {
			JSONObject newEntity = getObjectFromServer(url);

			if (newEntity == null) {
				return null;
			} else {
				return ReturnValueUtils.convertReturnValue(
						getEntry().getType(), newEntity, getEntry()
								.getRepoRel(), getClient());
			}
		}
	}
}
