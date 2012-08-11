package at.furti.springrest.client.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.http.link.Link;
import at.furti.springrest.client.json.exception.MissformedLinkException;

/**
 * Used to extract the links of an {@link JSONObject}
 * 
 * @author Daniel Furtlehner
 * 
 */
public class LinkWorker extends JsonWorker {

	private static final String LINKS = "_links";
	private static final String HREF = "href";
	private static final String REL = "rel";
	private static final String SELF = "self";

	public LinkWorker(JSONObject object) {
		super(object);
	}

	/**
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public List<Link> getLinks() {
		return getLinks(null);
	}

	/**
	 * @param object
	 * @param rel
	 * @return
	 * @throws LinkArrayException
	 *             if no linkarray is found in the object
	 */
	public List<Link> getLinks(String rel) {
		List<Link> links = new ArrayList<Link>();

		if (getObject() == null) {
			return links;
		}

		if (!getObject().has(LINKS) || getObject().isNull(LINKS)) {
			return null;
		}

		JSONArray jsonLinks = getObject().getJSONArray(LINKS);

		Iterator<Object> it = jsonLinks.iterator();

		while (it.hasNext()) {
			JSONObject jsonLink = (JSONObject) it.next();

			try {
				String linkRel = jsonLink.getString(REL);

				// if the rel is null --> all links should be added.
				// if the rel is not null and it is equal the rel of the link
				// -->
				// add it
				if (rel == null || linkRel.equals(rel)) {
					links.add(new Link(linkRel, jsonLink.getString(HREF)));
				}
			} catch (RuntimeException ex) {
				throw new MissformedLinkException(
						"Error creating link from object", ex);
			}
		}

		return links;
	}

	/**
	 * @return
	 */
	public String getSelfLink() {
		List<Link> links = getLinks(SELF);

		if (links != null && links.size() > 0) {
			return links.get(0).getHref();
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public Map<String, String> getLazyProperties() {
		List<Link> links = getLinks();

		if (links == null) {
			return null;
		}

		Map<String, String> lazyProperties = new HashMap<String, String>();

		for (Link link : links) {
			if (!link.getRel().equals(SELF)) {
				lazyProperties.put(link.getRel(), link.getHref());
			}
		}

		return lazyProperties;
	}
}