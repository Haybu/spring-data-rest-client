package at.furti.springrest.client.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonWorker<T> {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private T object;

	public JsonWorker(T object) {
		this.object = object;
	}

	public T getObject() {
		return object;
	}

	/**
	 * All private non-static non-transient fields are used.
	 * 
	 * @param o
	 * @return
	 */
	protected Collection<Field> getRelevantFields(Object o) {
		Collection<Field> fields = new ArrayList<Field>();

		Class<?> c = o.getClass();

		while (c != null) {

			Field[] currentFields = c.getDeclaredFields();

			for (Field field : currentFields) {
				int modifiers = field.getModifiers();

				if (!Modifier.isStatic(modifiers)
						&& Modifier.isPrivate(modifiers)
						&& !Modifier.isTransient(modifiers)
						&& !Modifier.isFinal(modifiers)) {
					fields.add(field);
				}
			}

			c = c.getSuperclass();
		}

		return fields;
	}
}
