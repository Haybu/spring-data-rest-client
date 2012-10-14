package at.furti.springrest.client.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import at.furti.springrest.client.bytecode.RepositoryClassTransformer;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.util.RestCollectionUtils;

/**
 * {@link FactoryBean} that creates an instance of a Repository for
 * communication with a rest server.
 * 
 * @author Daniel Furtlehner
 * 
 */
public class RestRepositoryCreator implements FactoryBean<Object> {

	private DataRestClient client;
	private RepositoryEntry entry;
	private LinkManager linkManager;

	public RestRepositoryCreator(DataRestClient client, RepositoryEntry entry,
			LinkManager linkManager) {
		Assert.notNull(client, "Client is required");
		Assert.notNull(entry, "Entry is required");
		Assert.notNull(linkManager, "LinkManager is required");

		this.client = client;
		this.entry = entry;
		this.linkManager = linkManager;
	}

	public Object getObject() throws Exception {
		return RepositoryClassTransformer.getInstance().getTransformedObject(
				entry.getRepoClass(),
				RestCollectionUtils.toMap(
						RepositoryClassTransformer.CLIENT_KEY, client,
						RepositoryClassTransformer.LINK_MANAGER_KEY,
						linkManager,
						RepositoryClassTransformer.REPO_ENTRY_KEY, entry));
	}

	public Class<?> getObjectType() {
		return entry.getRepoClass();
	}

	public boolean isSingleton() {
		return true;
	}
}
