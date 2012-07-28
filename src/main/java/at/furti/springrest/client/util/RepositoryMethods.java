package at.furti.springrest.client.util;

import java.lang.reflect.Method;
import java.util.HashSet;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Daniel
 *
 */
public class RepositoryMethods extends HashSet<String> {

	private static final long serialVersionUID = -5959332607084539101L;

	public RepositoryMethods() {
		Method[] methods = CrudRepository.class.getMethods();

		for (Method m : methods) {
			add(m.getName());
		}
	}
}
