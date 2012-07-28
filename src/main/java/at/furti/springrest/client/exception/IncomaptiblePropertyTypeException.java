package at.furti.springrest.client.exception;

import java.lang.reflect.Field;

/**
 * @author Daniel Furtlehner
 *
 */
public class IncomaptiblePropertyTypeException extends Exception {

	private static final long serialVersionUID = 8690407588724328785L;

	public IncomaptiblePropertyTypeException(Field field, Object value) {
		super(generateMessage(field, value));
	}

	private static String generateMessage(Field field, Object value) {
		StringBuilder builder = new StringBuilder("Incompatible PropertyType");

		builder.append(" for field[").append(field.getName()).append("]");
		builder.append(" fieldType is[").append(field.getType()).append("]");
		builder.append(" valueType is[").append(value.getClass()).append("]");

		return builder.toString();
	}
}
