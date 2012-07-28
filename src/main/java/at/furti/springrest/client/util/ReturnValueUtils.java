package at.furti.springrest.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.exception.IncomaptiblePropertyTypeException;
import at.furti.springrest.client.http.DataRestClient;

public final class ReturnValueUtils {

	/**
	 * @param returnType
	 * @param stream
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convertCollection(Class<?> collectionType,
			InputStream in, String rel, DataRestClient client) throws IOException {
		// Return a lazy initializing iteratable
		return new LazyInitializingIterable(JsonUtils.toJsonObject(in), rel,
				client, collectionType);
	}

	/**
	 * @param type
	 * @param in
	 * @param rel
	 * @param client
	 * @return
	 */
	public static Object convertReturnValue(Class<?> type, JSONObject data,
			String rel, DataRestClient client) throws IOException,
			NoSuchMethodException, InvocationTargetException, IllegalAccessException,
			InstantiationException, IncomaptiblePropertyTypeException {
		Constructor<?> c = type.getConstructor();

		Object o = c.newInstance();

		JsonUtils.fillObject(o, data);

		return o;
	}
}
