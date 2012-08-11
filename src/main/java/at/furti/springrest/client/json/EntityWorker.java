package at.furti.springrest.client.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.bytecode.EntityClassTransformer;
import at.furti.springrest.client.exception.IncomaptiblePropertyTypeException;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.util.RestCollectionUtils;

/**
 * Used to fill the properties of an object with the data from a
 * {@link JSONObject}
 * 
 * @author Daniel Furtlehner
 * 
 */
public class EntityWorker extends JsonWorker {

	private DataRestClient client;
	private String repoRel;

	public EntityWorker(JSONObject object, DataRestClient client, String repoRel) {
		super(object);
		this.client = client;
		this.repoRel = repoRel;
	}

	/**
	 * @param o
	 * @param jsonObject
	 * @throws IllegalAccessException
	 * @throws IncomaptiblePropertyTypeException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	public void fillObject(Object o) throws IllegalAccessException,
			IncomaptiblePropertyTypeException, InstantiationException,
			InvocationTargetException {
		Collection<Field> allFields = getRelevantFields(o);

		for (Field field : allFields) {
			field.setAccessible(true);

			// Set null if not available
			if (!getObject().has(field.getName())
					|| getObject().isNull(field.getName())) {
				field.set(o, null);
			} else {
				setValue(field, o);
			}
		}
	}

	/**
	 * @param field
	 * @param o
	 * @param value
	 * @throws IncomaptiblePropertyTypeException
	 * @throws IllegalAccessException
	 */
	private void setValue(Field field, Object o)
			throws IncomaptiblePropertyTypeException, IllegalAccessException,
			InstantiationException, InvocationTargetException {
		Class<?> type = field.getType();

		Object value = getObject().get(field.getName());

		if (value instanceof JSONObject) {
			LinkWorker valueLinkWorker = new LinkWorker((JSONObject) value);

			Object objectToSet = EntityClassTransformer.getInstance()
					.getTransformedObject(
							type,
							RestCollectionUtils.toMap(
									EntityClassTransformer.CLIENT_KEY, client,
									EntityClassTransformer.SELF_LINK_KEY,
									valueLinkWorker.getSelfLink(),
									EntityClassTransformer.LAZY_PROPERTIES_KEY,
									valueLinkWorker.getLazyProperties(),
									EntityClassTransformer.REPO_REL_KEY,
									repoRel));

			EntityWorker valueWorker = new EntityWorker((JSONObject) value,
					client, repoRel);

			valueWorker.fillObject(objectToSet);

			field.set(o, objectToSet);
		} else {
			Object converted = convertValue(type,
					getObject().getString(field.getName()));

			if (converted == null) {
				throw new IncomaptiblePropertyTypeException(field, value);
			}

			field.set(o, converted);
		}
	}

	/**
	 * @param type
	 * @param string
	 * @return
	 */
	private Object convertValue(Class<?> type, String string)
			throws InvocationTargetException, IllegalAccessException,
			InstantiationException {
		if (String.class.equals(type)) {
			return string;
		}

		if (Date.class.isAssignableFrom(type)) {
			return new Date(Long.parseLong(string));
		}

		// At first try to create the object from the constructor with a single
		// String argument
		Constructor<?> c = getStringConstructor(type);

		if (c != null) {
			return c.newInstance(string);
		}

		// At next try the static valueOf Method
		Method m = getValueOf(type);

		if (m != null) {
			return m.invoke(null, string);
		}

		// Else try to instantiate from a single value constructor
		Constructor<?>[] constructors = type.getConstructors();

		for (Constructor<?> constructor : constructors) {
			// If a single value constructor
			if (constructor.getParameterTypes().length == 1) {
				Class<?> argumentType = constructor.getParameterTypes()[0];

				c = getStringConstructor(argumentType);

				if (c != null) {
					return c.newInstance(string);
				}

				m = getValueOf(argumentType);

				if (m != null) {
					return m.invoke(null, string);
				}
			}
		}

		return null;
	}

	/**
	 * @param type
	 * @return
	 */
	private Constructor<?> getStringConstructor(Class<?> type) {
		try {
			return type.getConstructor(String.class);

		} catch (NoSuchMethodException ex) {
			// TODO: Logging
		}

		return null;
	}

	/**
	 * @param type
	 * @return
	 */
	private Method getValueOf(Class<?> type) {
		try {
			Method m = type.getMethod("valueOf", String.class);

			if (Modifier.isStatic(m.getModifiers())) {
				return m;
			}

		} catch (NoSuchMethodException ex) {
			// TODO: Logging
		}
		return null;
	}

	/**
	 * All private non-static non-transient fields are used.
	 * 
	 * @param o
	 * @return
	 */
	private Collection<Field> getRelevantFields(Object o) {
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
