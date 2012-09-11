package at.furti.springrest.client.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.exception.IncomaptiblePropertyTypeException;

/**
 * Used to fill the properties of an object with the data from a
 * {@link JSONObject}
 * 
 * @author Daniel Furtlehner
 * 
 */
public class EntityToJsonWorker extends JsonWorker<Object> {

	private List<String> toAdd;
	private List<String> toRemove;

	private Object fromServer;

	public EntityToJsonWorker(Object object, Object fromServer) {
		super(object);
		this.fromServer = fromServer;
	}

	/**
	 * @param o
	 * @param jsonObject
	 * @throws IllegalAccessException
	 * @throws IncomaptiblePropertyTypeException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	public JSONObject toJson() throws IllegalAccessException {
		Collection<Field> allFields = getRelevantFields(getObject());

		JSONObject json = new JSONObject();

		for (Field field : allFields) {
			field.setAccessible(true);

			Object value = field.get(getObject());

			if (value != null) {
				handleValue(json, value, field.getName());
			}
		}

		return json;
	}

	/**
	 * @param json
	 * @param value
	 */
	private void handleValue(JSONObject json, Object value, String name)
			throws IllegalAccessException {
		// Boolean, Double, Integer, Long, String
		if (isPrimitive(value)) {
			json.put(name, value);
		} else if (Date.class.isAssignableFrom(value.getClass())) {
			json.put(name, ((Date) value).getTime());
		} else {
			// TODO: check for collections and map

			// Check for identifier. If found do not add -->
			json.put(name, new EntityToJsonWorker(value, null).toJson());
		}
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean isPrimitive(Object value) {
		Class<?> valueClass = value.getClass();

		return Boolean.class.isAssignableFrom(valueClass)
				|| Double.class.isAssignableFrom(valueClass)
				|| Integer.class.isAssignableFrom(valueClass)
				|| Long.class.isAssignableFrom(valueClass)
				|| String.class.isAssignableFrom(valueClass);
	}

	public List<String> getToAdd() {
		return toAdd;
	}

	public List<String> getToRemove() {
		return toRemove;
	}
}