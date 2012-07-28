package at.furti.springrest.client.repository;

import java.lang.reflect.Method;

import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticClassTransformer;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.method.NotImplementedMethodAdvice;
import at.furti.springrest.client.util.MethodAdviceFactory;

public class RepositoryClassTransformer implements PlasticClassTransformer {

	private RepositoryEntry entry;
	private DataRestClient client;
	private LinkManager linkManager;

	public RepositoryClassTransformer(RepositoryEntry entry,
			DataRestClient client, LinkManager linkManager) {
		this.entry = entry;
		this.client = client;
		this.linkManager = linkManager;
	}

	public void transform(PlasticClass plasticClass) {
		
		for (Method m : entry.getRepoClass().getMethods()) {
			plasticClass.introduceMethod(m).addAdvice(getAdvice(m));
		}
	}

	/**
	 * @return
	 */
	private MethodAdvice getAdvice(Method m) {
		MethodAdvice advice = MethodAdviceFactory.createAdvice(m, entry, client,
				linkManager);

		if (advice != null) {
			return advice;
		} else {
			return new NotImplementedMethodAdvice();
		}
	}
}
