package at.furti.springrest.client.config;

import org.apache.tapestry5.plastic.ClassInstantiator;
import org.apache.tapestry5.plastic.PlasticManager;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.RepositoryClassTransformer;
import at.furti.springrest.client.repository.RepositoryEntry;

public class RestRepositoryCreator implements FactoryBean<Object> {

	private PlasticManager plasticManager = PlasticManager
			.withContextClassLoader().create();

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
		ClassInstantiator<?> instantiator = plasticManager.createProxy(entry
				.getRepoClass(), new RepositoryClassTransformer(entry, client,
				linkManager));

		Object repository = instantiator.newInstance();

		return repository;
	}

	public Class<?> getObjectType() {
		return entry.getRepoClass();
	}

	public boolean isSingleton() {
		return true;
	}
}
