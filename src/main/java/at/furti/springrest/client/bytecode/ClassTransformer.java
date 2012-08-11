package at.furti.springrest.client.bytecode;

import java.util.Map;

import org.apache.tapestry5.plastic.ClassInstantiator;
import org.apache.tapestry5.plastic.PlasticClassTransformer;
import org.apache.tapestry5.plastic.PlasticManager;

/**
 * Base class for class transformers.
 * 
 * Implementations must provide a {@link PlasticClassTransformer} that does the
 * work for transforming a class.
 * 
 * @author Daniel Furtlehner
 * 
 */
public abstract class ClassTransformer {

	private PlasticManager manager;

	public ClassTransformer() {
		manager = PlasticManager.withContextClassLoader().create();
	}

	/**
	 * Get a new instance of a transformed object.
	 * 
	 * If the type is a interface a proxy will be created that implements the
	 * methods of the interface. Else a new Class will be created that extends
	 * the type.
	 * 
	 * @param type
	 * @return
	 */
	public <T> T getTransformedObject(Class<T> type,
			Map<String, Object> parameters) {
		ClassInstantiator<T> instantiator = getInstantiator(type, parameters);

		return instantiator.newInstance();
	}

	/**
	 * @param type
	 * @param parameters
	 * @return
	 */
	private <T> ClassInstantiator<T> getInstantiator(Class<T> type,
			Map<String, Object> parameters) {

		if (type.isInterface()) {
			return manager.createProxy(type,
					getClassTransformer(type, parameters));
		} else {
			return manager.createClass(type,
					getClassTransformer(type, parameters));
		}
	}

	/**
	 * @param requiredType
	 * @param key
	 * @param parameters
	 * @return
	 * @throws ClassCastException
	 *             if the object could not be cast to the required type
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getParameter(String key, Map<String, Object> parameters) {
		if (parameters == null || !parameters.containsKey(key)) {
			return null;
		}

		return (T) parameters.get(key);
	}

	/**
	 * Implementations must return a classTransformer that is used to transform
	 * the class of the given type.
	 * 
	 * @param type
	 *            the class that should be transformed. Must not return null.
	 * @param parameters
	 *            needed for the class transformer
	 * @return transformer that does the transformation work
	 */

	protected abstract PlasticClassTransformer getClassTransformer(
			Class<?> type, Map<String, Object> parameters);
}
