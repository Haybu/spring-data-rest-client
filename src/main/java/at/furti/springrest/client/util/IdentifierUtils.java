package at.furti.springrest.client.util;

import java.lang.reflect.Field;

/**
 * @author Daniel Furtlehner
 * 
 */
public class IdentifierUtils {

	public static final String IDENTIFIER_NAME = "_selfHref";

	/**
	 * Returns the self uri for the object if set.
	 * 
	 * The uri is used to identify the object for REST operations.
	 * 
	 * @param object
	 * @return
	 */
	public static String getIdentifier(Object object) {
		Field f = getField(object);

		if (f == null) {
			return null;
		}

		try {
			Object href = f.get(object);

			return href != null ? href.toString() : null;
		} catch (IllegalAccessException ex) {
			// TODO: logging
		}

		return null;
	}

	// /**
	// * Set the uri that identifies the object for REST operations.
	// *
	// * @param object
	// * @param href
	// */
	// public static void setIdentifier(Object object, String href) {
	// try {
	// Field f = getField(object);
	//
	// if (f != null) {
	// f.set(object, href);
	// }
	// } catch (IllegalAccessException ex) {
	// // TODO: logging
	// }
	// }

	private static Field getField(Object object) {
		try {
			Field f = object.getClass().getDeclaredField(IDENTIFIER_NAME);
			f.setAccessible(true);

			return f;
		} catch (NoSuchFieldException ex) {
			// TODO: logging
		}

		return null;
	}
}
