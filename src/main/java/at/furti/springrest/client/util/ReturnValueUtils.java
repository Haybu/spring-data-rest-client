package at.furti.springrest.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.bytecode.EntityClassTransformer;
import at.furti.springrest.client.exception.IncomaptiblePropertyTypeException;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.json.EntityWorker;
import at.furti.springrest.client.json.JsonUtils;
import at.furti.springrest.client.json.LinkWorker;
import at.furti.springrest.client.repository.lazy.LazyInitializingIterable;

public final class ReturnValueUtils {

	/**
	 * @param returnType
	 * @param stream
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convertCollection(Class<?> collectionType,
			InputStream in, String rel, DataRestClient client)
			throws IOException {
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
			NoSuchMethodException, InvocationTargetException,
			IllegalAccessException, InstantiationException,
			IncomaptiblePropertyTypeException {

		LinkWorker linkWorker = new LinkWorker(data);

		EntityWorker entityWorker = new EntityWorker(data, client, rel);

		Object o = EntityClassTransformer.getInstance().getTransformedObject(
				type,
				RestCollectionUtils.toMap(EntityClassTransformer.SELF_LINK_KEY,
						linkWorker.getSelfLink(),
						EntityClassTransformer.LAZY_PROPERTIES_KEY,
						linkWorker.getLazyProperties(),
						EntityClassTransformer.REPO_REL_KEY, rel,
						EntityClassTransformer.CLIENT_KEY, client));

		entityWorker.fillObject(o);

		return o;
	}
}
