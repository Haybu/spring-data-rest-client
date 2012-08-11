package at.furti.springrest.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

	/**
	 * @param strings
	 * @return
	 */
	public static Map<String, Object> toMap(Object... params) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (params.length % 2 != 0) {
			throw new IllegalArgumentException("Odd number of parameters");
		}

		for (int i = 0; i < params.length; i += 2) {
			map.put(params[i].toString(), params[i + 1]);
		}

		return map;
	}
}
