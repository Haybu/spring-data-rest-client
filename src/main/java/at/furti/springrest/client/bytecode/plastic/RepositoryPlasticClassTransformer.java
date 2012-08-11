package at.furti.springrest.client.bytecode.plastic;

import java.lang.reflect.Method;

import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticClassTransformer;

import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.method.NotImplementedMethodAdvice;
import at.furti.springrest.client.util.MethodAdviceFactory;

/**
 * Class transformer used to implement a JPA repository interface.
 * 
 * Implements all Methods of the interface and adds a methodadvice to each method.
 * The methodadvice is created by the {@link MethodAdviceFactory}
 * 
 * @author Daniel Furtlehner
 * 
 */
public class RepositoryPlasticClassTransformer implements
		PlasticClassTransformer {

	private RepositoryConfig entry;
	private DataRestClient client;
	private LinkManager linkManager;

	public RepositoryPlasticClassTransformer(RepositoryConfig entry,
			DataRestClient client, LinkManager linkManager) {
		this.entry = entry;
		this.client = client;
		this.linkManager = linkManager;
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.plastic.PlasticClassTransformer#transform(org.apache.tapestry5.plastic.PlasticClass)
	 */
	public void transform(PlasticClass plasticClass) {
		for (Method m : entry.getRepoClass().getMethods()) {
			plasticClass.introduceMethod(m).addAdvice(getAdvice(m));
		}
	}

	/**
	 * @return
	 */
	private MethodAdvice getAdvice(Method m) {
		MethodAdvice advice = MethodAdviceFactory.createAdvice(m, entry,
				client, linkManager);

		if (advice != null) {
			return advice;
		} else {
			return new NotImplementedMethodAdvice();
		}
	}
}
