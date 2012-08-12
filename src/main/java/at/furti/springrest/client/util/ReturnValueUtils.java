package at.furti.springrest.client.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.tapestry5.json.JSONObject;
import org.springframework.util.CollectionUtils;

import at.furti.springrest.client.bytecode.EntityClassTransformer;
import at.furti.springrest.client.exception.IncomaptiblePropertyTypeException;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.Link;
import at.furti.springrest.client.json.EntityWorker;
import at.furti.springrest.client.json.LinkWorker;
import at.furti.springrest.client.repository.lazy.LazyInitializingIterable;

public final class ReturnValueUtils {

	/**
	 * @param returnType
	 * @param stream
	 * @return
	 */
	public static Object convertCollection(Class<?> entityType,
			JSONObject data, String repoRel, DataRestClient client) {
		if (data == null) {
			return null;
		}

		LinkWorker worker = new LinkWorker(data);
		String entityRel = repoRel + "." + entityType.getSimpleName();

		Collection<Link> links = worker.getLinks(entityRel);

		// If no links where found --> return null
		if (CollectionUtils.isEmpty(links)) {
			return null;
		}

		// Return a lazy initializing iteratable
		return new LazyInitializingIterable(links, repoRel, client, entityType);
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
