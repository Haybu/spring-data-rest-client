package at.furti.springrest.client.repository.method;

import java.util.List;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.MethodInvocation;

import at.furti.springrest.client.config.RepositoryEntry;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.json.JsonUtils;

/**
 * @author Daniel
 * 
 */
public class CountMethodAdvice extends FindAllMethodAdvice {

	public CountMethodAdvice(LinkManager linkManager, RepositoryEntry entry,
			DataRestClient client) {
		super(linkManager, entry, client);
	}

	@Override
	protected void handleResponse(MethodInvocation invoaction,
			List<Response> responses, String link) {
		Response response = getFirstResponse(responses);

		if (response == null) {
			invoaction.setReturnValue(new Long(0));
		} else {
			try {
				JSONObject data = JsonUtils.toJsonObject(response.getBody());

				if (data == null || data.isNull(JsonUtils.CONTENT)) {
					invoaction.setReturnValue(new Long(0));
				} else {
					JSONArray content = data.getJSONArray(JsonUtils.CONTENT);

					invoaction.setReturnValue(new Long(content.length()));
					// LinkWorker worker = new LinkWorker(data);
					// String entityRel = getEntry().getRepoRel() + "."
					// + getEntry().getEntityType().getSimpleName();
					//
					// Collection<Link> links = worker.getLinks(entityRel);
					//
					// if (links == null) {
					// invoaction.setReturnValue(new Long(0));
					// } else {
					// invoaction.setReturnValue(new Long(links.size()));
					// }
				}
			} catch (Exception ex) {
				invoaction.setCheckedException(ex);
				invoaction.rethrow();
			}
		}
	}
}
