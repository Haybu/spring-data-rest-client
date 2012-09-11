package at.furti.springrest.client.repository.lazy;

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.base.ClientAware;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.Link;
import at.furti.springrest.client.json.LinkWorker;

public abstract class LazyLoadingHandlerBase extends ClientAware implements
		LazyLoadingHandler {

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

	public List<Link> getLinks() throws IOException {
		// Read the links for the object from the server
		JSONObject linkResponse = getObjectFromServer(getHref());
		LinkWorker linkWorker = new LinkWorker(linkResponse);

		return linkWorker.getLinks();
	}

	/**
	 * @return
	 */
	protected abstract Object loadObject();
}
