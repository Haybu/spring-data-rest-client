package at.furti.springrest.client.bytecode.plastic;

import java.lang.reflect.Method;

import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticClassTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.furti.springrest.client.config.RepositoryEntry;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.method.MethodAdviceFactory;
import at.furti.springrest.client.repository.method.NotExportedAdvice;

/**
 * Class transformer used to implement a JPA repository interface.
 * 
 * Implements all Methods of the interface and adds a methodadvice to each
 * method. The methodadvice is created by the {@link MethodAdviceFactory}
 * 
 * @author Daniel Furtlehner
 * 
 */
public class RepositoryPlasticClassTransformer implements
		PlasticClassTransformer {

	private RepositoryEntry entry;
	private DataRestClient client;
	private LinkManager linkManager;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public RepositoryPlasticClassTransformer(RepositoryEntry entry,
			DataRestClient client, LinkManager linkManager) {
		this.entry = entry;
		this.client = client;
		this.linkManager = linkManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.tapestry5.plastic.PlasticClassTransformer#transform(org.apache
	 * .tapestry5.plastic.PlasticClass)
	 */
	public void transform(PlasticClass plasticClass) {
		for (Method m : entry.getRepoClass().getMethods()) {
			MethodAdvice advice = getAdvice(m);
			plasticClass.introduceMethod(m).addAdvice(advice);

			logger.info("Implemented advice [{}] for method [{}]", advice
					.getClass().toString(), m);
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
			return new NotExportedAdvice();
		}
	}
}
