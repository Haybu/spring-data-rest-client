package at.furti.springrest.client.repository.lazy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.Link;
import at.furti.springrest.client.util.ReturnValueUtils;

public class LazyCollectionLoadingHandler extends LazyLoadingHandlerBase {

	private Class<?> type;
	private Class<?> genericType;
	private String repoRel;

	public LazyCollectionLoadingHandler(Class<?> type, Class<?> genericType,
			DataRestClient client, String href, String repoRel) {
		super(client, href);
		this.type = type;
		this.genericType = genericType;
		this.repoRel = repoRel;
	}

	@Override
	protected Object loadObject() {
		try {
			Collection<Link> links = getLinks();

			// No Links found --> return null;
			if (CollectionUtils.isEmpty(links)) {
				return null;
			}

			Collection<Object> ret = null;

			if (List.class.isAssignableFrom(type)) {
				ret = new ArrayList<Object>();
			} else if (Set.class.isAssignableFrom(type)) {
				ret = new HashSet<Object>();
			} else {
				// TODO: throw exception. Only set and list are allowed for now
			}

			for (Link link : links) {
				ret.add(ReturnValueUtils.convertReturnValue(genericType,
						getObjectFromServer(link.getHref()), repoRel,
						getClient()));
			}

			return ret;
		} catch (Exception ex) {
			logger.error("Error loading links from server", ex);
			return null;
		}

	}
}
