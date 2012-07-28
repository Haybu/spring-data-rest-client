package at.furti.springrest.client.http.link;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.DataRestClient.ParameterType;
import at.furti.springrest.client.http.exception.RepositoryNotExposedException;
import at.furti.springrest.client.util.JsonUtils;
import at.furti.springrest.client.util.RepositoryUtils;
import at.furti.springrest.client.util.RestCollectionUtils;

/**
 * @author Daniel
 * 
 */
public class LinkManager {

	private DataRestClient client;

	private Map<String, LinkEntry> links;

	public LinkManager(DataRestClient client) {
		this.client = client;
	}

	/**
	 * @param repoRel
	 * @return
	 */
	public String getHref(String repoRel) {
		setupLinks();

		if (links == null) {
			return null;
		}

		if (!links.containsKey(repoRel)) {
			return null;
		}

		return links.get(repoRel).getLink().getHref();
	}

	/**
	 * @param repoRel
	 * @param m
	 * @return
	 */
	public String getHref(String repoRel, Method m)
			throws RepositoryNotExposedException {
		setupLinks();

		if (links == null) {
			return null;
		}

		if (!links.containsKey(repoRel)) {
			throw new RepositoryNotExposedException();
		}

		String methodRel = RepositoryUtils.getMethodRel(repoRel, m);

		if (methodRel == null) {
			return links.get(repoRel).getLink().getHref();
		}

		Link searchLink = links.get(repoRel).getSearchLink(methodRel);

		return searchLink != null ? searchLink.getHref() : null;
	}

	/**
	 * Load the links from the server if they are not loaded yet.
	 * 
	 */
	private synchronized void setupLinks() {
		if (links != null) {
			return;
		}

		try {
			InputStream stream = client.executeGet(null, ParameterType.NONE);

			this.links = new HashMap<String, LinkManager.LinkEntry>();
			Collection<Link> list = JsonUtils.getLinks(stream);

			if (links != null) {
				for (Link link : list) {
					this.links.put(link.getRel(), new LinkEntry(link));
				}
			}
		} catch (Exception ex) {
			// TODO: log error and do nothing. Try again later
			ex.printStackTrace();
		}
	}

	private class LinkEntry {
		private Link link;
		private Map<String, Link> searchLinks;

		public LinkEntry(Link link) {
			super();
			this.link = link;
		}

		public Link getLink() {
			return link;
		}

		public Link getSearchLink(String searchRel) {
			setupSearchLinks();

			return searchLinks.get(searchRel);
		}

		/**
		 * Load the links from the server if they are not loaded yet
		 */
		private synchronized void setupSearchLinks() {
			if (searchLinks != null) {
				return;
			}

			try {
				InputStream stream = client.executeGet(RestCollectionUtils
						.toCollection(getLink().getHref(), "search"), ParameterType.NONE);

				Collection<Link> list = JsonUtils.getLinks(stream);

				searchLinks = new HashMap<String, Link>();

				if (list != null) {
					for (Link link : list) {
						this.searchLinks.put(link.getRel(), link);
					}
				}
			} catch (Exception ex) {
				// TODO: log error and do nothing. Try again later
				ex.printStackTrace();
			}
			searchLinks = new HashMap<String, Link>();
		}
	}
}
