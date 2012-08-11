package at.furti.springrest.client.repository.lazy;

import at.furti.springrest.client.base.ClientAware;
import at.furti.springrest.client.http.DataRestClient;

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

	/**
	 * @return
	 */
	protected abstract Object loadObject();
}
