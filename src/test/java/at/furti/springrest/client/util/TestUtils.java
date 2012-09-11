package at.furti.springrest.client.util;

import java.util.Iterator;

public class TestUtils {
	public static final String HOST = "http://furti-springrest.cloudfoundry.com/";
	
	public static int getSize(Iterable<?> iterable) {
		if (iterable == null) {
			return 0;
		}

		Iterator<?> it = iterable.iterator();

		int size = 0;

		while (it.hasNext()) {
			it.next();
			size++;
		}

		return size;
	}
}
