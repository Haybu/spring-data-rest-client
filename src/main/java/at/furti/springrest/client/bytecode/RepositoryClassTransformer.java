package at.furti.springrest.client.bytecode;

import java.util.Map;

import org.apache.tapestry5.plastic.PlasticClassTransformer;

import at.furti.springrest.client.bytecode.plastic.RepositoryPlasticClassTransformer;
import at.furti.springrest.client.config.RepositoryEntry;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.LinkManager;

/**
 * @author Daniel Furtlehner
 * 
 */
public class RepositoryClassTransformer extends ClassTransformer {

	public static final String REPO_ENTRY_KEY = "repoEntry";
	public static final String CLIENT_KEY = "client";
	public static final String LINK_MANAGER_KEY = "linkManager";

	private static final RepositoryClassTransformer INSTANCE = new RepositoryClassTransformer();

	public static RepositoryClassTransformer getInstance() {
		return INSTANCE;
	}

	@Override
	protected PlasticClassTransformer getClassTransformer(Class<?> type,
			Map<String, Object> parameters) {
		return new RepositoryPlasticClassTransformer(
				(RepositoryEntry) getParameter(REPO_ENTRY_KEY, parameters),
				(DataRestClient) getParameter(CLIENT_KEY, parameters),
				(LinkManager) getParameter(LINK_MANAGER_KEY, parameters));
	}
}
