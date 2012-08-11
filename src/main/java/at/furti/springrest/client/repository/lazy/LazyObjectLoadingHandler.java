package at.furti.springrest.client.repository.lazy;

import java.util.Collection;

import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.Link;
import at.furti.springrest.client.json.LinkWorker;
import at.furti.springrest.client.repository.exception.LinkCountException;
import at.furti.springrest.client.util.ReturnValueUtils;

public class LazyObjectLoadingHandler extends LazyLoadingHandlerBase {

	private Class<?> type;
	private String repoRel;

	public LazyObjectLoadingHandler(DataRestClient client, String href,
			Class<?> type, String repoRel) {
		super(client, href);
		this.type = type;
		this.repoRel = repoRel;
	}

	@Override
	protected Object loadObject() {
		try {
			// Read the links for the object from the server
			JSONObject linkResponse = getObjectFromServer(getHref());
			LinkWorker linkWorker = new LinkWorker(linkResponse);
			Collection<Link> links = linkWorker.getLinks();

			// No links found --> no object to fill
			if (links == null || links.size() == 0) {
				return null;
			}

			/*
			 * There is more than one link in the response. Don't know which one
			 * to take. maybe a wrong data type in the entyty. Should be a list.
			 * 
			 * throw an exception
			 */
			if (links.size() > 1) {
				throw new LinkCountException(
						"More than one link was returned from the "
								+ "server while lazily initialising a object of typ ["
								+ type + "]");
			}

			Link link = links.iterator().next();

			// Simple call the link again, because we want the object for that
			// link, not the link.
			JSONObject objectToUse = getObjectFromServer(link.getHref());

			return ReturnValueUtils.convertReturnValue(type, objectToUse,
					repoRel, getClient());
		} catch (Exception ex) {
			logger.error("Error instantiating Object", ex);
		}

		return null;
	}
}
