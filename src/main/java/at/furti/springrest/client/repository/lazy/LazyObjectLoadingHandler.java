package at.furti.springrest.client.repository.lazy;

import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.http.DataRestClient;
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
			JSONObject object = getObjectFromServer(getHref());

			// No links found --> no object to fill
			if (object == null) {
				return null;
			}

			LinkWorker linkWorker = new LinkWorker(object);

			/*
			 * There is more than one link in the response. Don't know which one
			 * to take. maybe a wrong data type in the entyty. Should be a list.
			 * 
			 * throw an exception
			 */
			if (linkWorker.getSelfLink() == null) {
				// TODO: throw an exception if no self link was found.
				// Maybe we got a collection from the server?
				throw new LinkCountException(
						"No self link was found in the object returned by the server "
								+ " while lazily initialising a object of typ ["
								+ type
								+ "]. Maybe we got a collection from the server.");
			}

			return ReturnValueUtils.convertReturnValue(type, object, repoRel,
					getClient());
		} catch (Exception ex) {
			logger.error("Error instantiating Object", ex);
			// TODO: rethrow exception??
		}

		return null;
	}
}
