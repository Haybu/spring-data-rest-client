package at.furti.springrest.client.repository.method;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodInvocation;

import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.json.JsonUtils;
import at.furti.springrest.client.json.LinkWorker;

public class ExistsAdvice extends FindOneAdvice {

	public ExistsAdvice(LinkManager linkManager, RepositoryConfig entry,
			DataRestClient client) {
		super(linkManager, entry, client);
	}

	@Override
	protected void handleResponse(MethodInvocation invoaction,
			Response response, String link) {
		/*
		 * To check if a entity exists simply check if a response with a self
		 * link is provided when calling the link for the object.
		 */
		if (response == null || response.getStream() == null) {
			invoaction.setReturnValue(false);
		} else {
			try {
				JSONObject data = JsonUtils.toJsonObject(response.getStream());

				if (data == null) {
					invoaction.setReturnValue(false);
				} else {
					LinkWorker linkWorker = new LinkWorker(data);

					invoaction.setReturnValue(linkWorker.getSelfLink() != null);
				}
			} catch (Exception ex) {
				invoaction.setCheckedException(ex);
				invoaction.rethrow();
			}
		}
	}
}
