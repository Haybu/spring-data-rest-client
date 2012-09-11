package at.furti.springrest.client.http.link;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.furti.springrest.client.base.ClientAware;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.exception.RepositoryNotExposedException;
import at.furti.springrest.client.json.JsonUtils;
import at.furti.springrest.client.json.LinkWorker;
import at.furti.springrest.client.util.RepositoryUtils;

/**
 * @author Daniel
 * 
 */
public class LinkManager extends ClientAware {

	private static final Logger logger = LoggerFactory
			.getLogger(LinkManager.class);

	private Map<String, LinkEntry> links;

	public LinkManager(DataRestClient client) {
		super(client);
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
			Response response = execute(RequestType.GET, new Request());

			this.links = new HashMap<String, LinkManager.LinkEntry>();

			Collection<Link> list = getLinks(response);

			if (links != null) {
				for (Link link : list) {
					this.links.put(link.getRel(), new LinkEntry(link));
				}
			}
		} catch (Exception ex) {
			logger.error("Error getting links from server", ex);
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
				Response response = execute(RequestType.GET, new Request(
						getLink().getHref() + "/search"));

				Collection<Link> list = getLinks(response);

				searchLinks = new HashMap<String, Link>();

				if (list != null) {
					for (Link link : list) {
						this.searchLinks.put(link.getRel(), link);
					}
				}
			} catch (Exception ex) {
				logger.error("Error getting searchlinks from server", ex);
			}

			searchLinks = new HashMap<String, Link>();
		}
	}

	private Collection<Link> getLinks(Response response) throws IOException {
		return new LinkWorker(JsonUtils.toJsonObject(response.getBody()))
				.getLinks();
	}
}
