package at.furti.springrest.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Id;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.furti.springrest.client.exception.IncomaptiblePropertyTypeException;
import at.furti.springrest.client.http.link.Link;

public class JsonUtils {

	private static final String LINKS = "_links";
	private static final String HREF = "href";
	private static final String REL = "rel";

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static JSONObject toJsonObject(InputStream in) throws IOException {
		JSONObject object = new JSONObject(IOUtils.toString(in, "UTF-8"));
		in.close();

		return object;
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static List<Link> getLinks(InputStream in) throws IOException {
		return getLinks(toJsonObject(in));
	}

	/**
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static List<Link> getLinks(JSONObject object) throws IOException {
		return getLinks(object, null);
	}

	/**
	 * @param object
	 * @param rel
	 * @return
	 */
	public static List<Link> getLinks(JSONObject object, String rel) {
		List<Link> links = new ArrayList<Link>();

		JSONArray jsonLinks = object.getJSONArray(LINKS);

		Iterator<Object> it = jsonLinks.iterator();

		while (it.hasNext()) {
			JSONObject jsonLink = (JSONObject) it.next();

			String linkRel = jsonLink.getString(REL);

			// if the rel is null --> all links should be added.
			// if the rel is not null and it is equal the rel of the link --> add it
			if (rel == null || linkRel.equals(rel)) {
				links.add(new Link(linkRel, jsonLink.getString(HREF)));
			}
		}

		return links;
	}

	public static void fillObject(Object o, JSONObject jsonObject)
			throws IllegalAccessException, IncomaptiblePropertyTypeException {
		Collection<Field> allFields = getRelevantFields(o);

		for (Field field : allFields) {
			field.setAccessible(true);
			String property = field.getName();

			// Set null if not available
			if (!jsonObject.has(property) || jsonObject.isNull(property)) {
				field.set(o, null);
			} else {
				setValue(field, o, jsonObject.get(property));
			}
		}
		
		//Check the links section --> for lazy initialization
	}

	/**
	 * @param field
	 * @param o
	 * @param value
	 * @throws IncomaptiblePropertyTypeException
	 * @throws IllegalAccessException
	 */
	private static void setValue(Field field, Object o, Object value)
			throws IncomaptiblePropertyTypeException, IllegalAccessException {
		Class<?> t = field.getType();

		if (!t.isAssignableFrom(value.getClass())) {
			throw new IncomaptiblePropertyTypeException(field, value);
		}

		field.set(o, value);
	}

	/**
	 * All private non-static non-transient fields are used.
	 * 
	 * @param o
	 * @return
	 */
	private static Collection<Field> getRelevantFields(Object o) {
		Collection<Field> fields = new ArrayList<Field>();

		Class<?> c = o.getClass();

		while (c != null) {

			Field[] currentFields = c.getDeclaredFields();

			for (Field field : currentFields) {
				int modifiers = field.getModifiers();

				if (!Modifier.isStatic(modifiers) && Modifier.isPrivate(modifiers)
						&& !Modifier.isTransient(modifiers)) {
					fields.add(field);
				}
			}

			c = c.getSuperclass();
		}

		return fields;
	}

	/**
	 * @param value
	 * @param parameters
	 */
	public static void setId(Object value, Object id)
			throws IllegalAccessException {
		Collection<Field> fields = getRelevantFields(value);

		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				f.setAccessible(true);

				f.set(value, id);

				return;
			}
		}
	}

}