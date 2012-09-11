package at.furti.springrest.client.repository.lazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.json.JSONObject;
import org.springframework.util.CollectionUtils;

import at.furti.springrest.client.base.ClientAware;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.Link;
import at.furti.springrest.client.util.ReturnValueUtils;

/**
 * @author Daniel
 * 
 */
public class LazyInitializingIterable extends ClientAware implements
		Iterable<Object> {

	private List<Entry> entries;
	private Class<?> type;
	private String repoRel;

	public LazyInitializingIterable(Collection<Link> links, String repoRel,
			DataRestClient client, Class<?> type) {
		super(client);
		this.type = type;
		this.repoRel = repoRel;

		initEntries(links);
	}

	public Iterator<Object> iterator() {
		return new LazyInitializingIterator();
	}

	/**
	 * Loads the object at index from the server and returns it.
	 * 
	 * @return
	 * @throws IndexOutOfBoundsException
	 *             if the index is larger than the size of the entries
	 */
	protected Object getObject(int index) throws Exception {
		if (entries == null) {
			return null;
		}

		Entry entry = entries.get(index);

		if (entry != null) {
			if (!entry.loaded) {
				JSONObject data = getObjectFromServer(entry.link.getHref());

				if (data != null) {
					entry.object = ReturnValueUtils.convertReturnValue(type,
							data, repoRel, getClient());
				}

				entry.loaded = true;
			}
			return entry.object;
		}

		return null;
	}

	/**
	 * Creates an entry for each link in the list.
	 * 
	 * @param links
	 */
	private void initEntries(Collection<Link> links) {
		entries = new ArrayList<Entry>();

		if (CollectionUtils.isEmpty(links)) {
			return;
		}

		for (Link link : links) {
			entries.add(new Entry(link));
		}
	}

	/**
	 * @author Daniel
	 * 
	 */
	private class LazyInitializingIterator implements Iterator<Object> {

		private int currentIndex;

		public LazyInitializingIterator() {
			currentIndex = -1;
		}

		public boolean hasNext() {
			currentIndex++;

			return currentIndex < entries.size();
		}

		public Object next() {
			try {
				return getObject(currentIndex);
			} catch (Exception ex) {
				// TODO: rethrow the exception
				return null;
			}
		}

		public void remove() {
			if (currentIndex == -1) {
				throw new IllegalStateException(
						"Could not remove index before call to next");
			}

			entries.remove(currentIndex);
			currentIndex -= 1;
		}
	}

	/**
	 * @author Daniel Furtlehner
	 * 
	 */
	private class Entry {
		private Object object;
		private Link link;
		private boolean loaded;

		public Entry(Link link) {
			this.link = link;
			this.loaded = false;
		}
	}
}
