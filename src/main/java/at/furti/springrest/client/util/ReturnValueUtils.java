package at.furti.springrest.client.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.bytecode.EntityClassTransformer;
import at.furti.springrest.client.exception.IncomaptiblePropertyTypeException;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.json.JsonToEntityWorker;
import at.furti.springrest.client.json.LinkWorker;

public final class ReturnValueUtils {

	/**
	 * @param returnType
	 * @param stream
	 * @return
	 */
	public static Object convertCollection(Class<?> entityType, JSONArray data,
			String repoRel, DataRestClient client)
			throws IncomaptiblePropertyTypeException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, IOException {
		if (data == null || data.length() == 0) {
			return null;
		}

		List<Object> ret = new ArrayList<Object>();

		Iterator<Object> it = data.iterator();

		while (it.hasNext()) {
			JSONObject object = (JSONObject) it.next();

			ret.add(convertReturnValue(entityType, object, repoRel, client));
		}

		// LinkWorker worker = new LinkWorker(data);
		// String entityRel = repoRel + "." + entityType.getSimpleName();

		// Collection<Link> links = worker.getLinks(entityRel);

		// If no links where found --> return null
		// if (CollectionUtils.isEmpty(links)) {
		// return null;
		// }

		return ret;
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

		JsonToEntityWorker entityWorker = new JsonToEntityWorker(data, client,
				rel);

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
