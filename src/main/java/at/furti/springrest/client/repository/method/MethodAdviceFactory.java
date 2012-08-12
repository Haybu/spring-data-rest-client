package at.furti.springrest.client.repository.method;

import java.lang.reflect.Method;

import org.apache.tapestry5.plastic.MethodAdvice;
import org.springframework.data.rest.repository.annotation.RestResource;

import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.LinkManager;

public class MethodAdviceFactory {

	private static final String SAVE = "save"; // TODO: WIth iterable and with
												// object
	private static final String FIND_ONE = "findOne";
	private static final String EXISTS = "exists";
	private static final String FIND_ALL = "findAll"; // TODO: With iterable and
														// without
	private static final String COUNT = "count";
	private static final String DELETE = "delete"; // TODO: with id, object and
													// iterable
	private static final String DELETE_ALL = "deleteAll";

	/**
	 * @param m
	 * @param entry
	 * @param client
	 * @param linkManager
	 * @return
	 */
	public static MethodAdvice createAdvice(Method m, RepositoryConfig entry,
			DataRestClient client, LinkManager linkManager) {
		if (m.isAnnotationPresent(RestResource.class)) {
			RestResource resource = m.getAnnotation(RestResource.class);

			// If the method is not exported --> return a advice that throws an
			// exception
			if (!resource.exported()) {
				return new NotExportedAdvice();
			}
		}

		if (m.getName().equals(FIND_ONE)) {
			return new FindOneAdvice(linkManager, entry, client);
		}

		if (m.getName().equals(EXISTS)) {
			return new ExistsAdvice(linkManager, entry, client);
		}

		if (m.getName().equals(FIND_ALL)) {
			return new FindAllMethodAdvice(linkManager, entry, client);
		}

		if (m.getName().equals(COUNT)) {
			return new CountMethodAdvice(linkManager, entry, client);
		}

		// TODO: other advices
		return new NotExportedAdvice();
	}
}
