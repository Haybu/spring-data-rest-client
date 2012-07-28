package at.furti.springrest.client.util;

import java.util.ArrayList;
import java.util.Collection;

public class RestCollectionUtils {

	/**
	 * @param strings
	 * @return
	 */
	public static Collection<String> toCollection(String... strings) {
		Collection<String> collection = new ArrayList<String>();

		for (String s : strings) {
			collection.add(s);
		}

		return collection;
	}
}
