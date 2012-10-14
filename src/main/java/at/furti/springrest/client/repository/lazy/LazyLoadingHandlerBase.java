package at.furti.springrest.client.repository.lazy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.base.ClientAware;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.json.LinkWorker;

public abstract class LazyLoadingHandlerBase extends ClientAware implements
		LazyLoadingHandler {

	private static final String CONTENT = "content";

	private String href;
	private Object target;
	private boolean initialized;

	public LazyLoadingHandlerBase(DataRestClient client, String href) {
		super(client);
		this.href = href;
	}

	public Object doGet() {
		initializeIfNeeded();

		return target;
	}

	public void doSet(Object newValue) {
		this.initialized = true;
		this.target = newValue;
	}

	/**
	 * 
	 */
	private void initializeIfNeeded() {
		if (initialized) {
			return;
		}

		initialized = true;
		target = loadObject();
	}

	public String getHref() {
		return href;
	}

	public List<String> getLinks() throws IOException {
		// Read the links for the object from the server
		JSONObject linkResponse = getObjectFromServer(getHref());

		if (linkResponse == null || linkResponse.isNull(CONTENT)) {
			return null;
		}

		JSONArray content = linkResponse.getJSONArray(CONTENT);

		Iterator<Object> contentIt = content.iterator();

		List<String> links = new ArrayList<String>();

		while (contentIt.hasNext()) {
			LinkWorker worker = new LinkWorker((JSONObject) contentIt.next());

			if (worker.getSelfLink() != null) {
				links.add(worker.getSelfLink());
			}
		}

		return links;
	}

	/**
	 * @return
	 */
	protected abstract Object loadObject();
}
