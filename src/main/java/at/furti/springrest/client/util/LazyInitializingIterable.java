package at.furti.springrest.client.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.exception.ObjectLoadingException;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.Link;

/**
 * @author Daniel
 * 
 */
public class LazyInitializingIterable<T extends Object> implements Iterable<T> {

	private List<Link> links;
	private List<T> objects;
	private Boolean[] loadedObjects;
	private DataRestClient client;

	private LazyInitializingIterator iterator;

	public LazyInitializingIterable(JSONObject object, String rel,
			DataRestClient client, Class<T> type) {
		links = JsonUtils.getLinks(object, rel);

		loadedObjects = new Boolean[links.size()];
		objects = new ArrayList<T>();
	}

	public Iterator<T> iterator() {
		if (iterator == null) {
			iterator = new LazyInitializingIterator();
		}

		return iterator();
	}

	/**
	 * Checks if the object at the index was loaded
	 * 
	 * @return
	 */
	private T getObject(int index) throws IOException {
		if (loadedObjects[index] == null || !loadedObjects[index].booleanValue()) {
			// TODO: load object
//			InputStream stream = client.executeGet(RestCollectionUtils
//					.toCollection(links.get(index).getHref()));

//			System.out.println(JsonUtils.toJsonObject(stream));
			loadedObjects[index] = Boolean.TRUE;
		}

		return objects.get(index);
	}

	/**
	 * @author Daniel
	 * 
	 */
	private class LazyInitializingIterator implements Iterator<T> {

		private int currentIndex;

		public LazyInitializingIterator() {
			currentIndex = -1;
		}

		public boolean hasNext() {
			currentIndex++;

			return currentIndex <= links.size();
		}

		public T next() {
			try {
				return getObject(currentIndex);
			} catch (IOException ex) {
				throw new ObjectLoadingException();
			}
		}

		public void remove() {
			// TODO implement remove

		}

	}
}
