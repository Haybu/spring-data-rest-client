package at.furti.springrest.client.util;

import java.lang.reflect.Method;

import org.apache.tapestry5.plastic.MethodAdvice;

import at.furti.springrest.client.config.RepositoryConfig;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.link.LinkManager;
import at.furti.springrest.client.repository.method.FindOneAdvice;

public class MethodAdviceFactory {

	private static final String FIND_ONE = "findOne";

	/**
	 * @param m
	 * @param entry
	 * @param client
	 * @param linkManager
	 * @return
	 */
	public static MethodAdvice createAdvice(Method m, RepositoryConfig entry,
			DataRestClient client, LinkManager linkManager) {

		if (m.getName().equals(FIND_ONE)) {
			return new FindOneAdvice(linkManager, entry, client);
		}
		
		//TODO: implement other advices

		return null;
	}
}
