package at.furti.springrest.client.bytecode.plastic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticClassTransformer;
import org.apache.tapestry5.plastic.PlasticField;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.repository.lazy.LazyCollectionLoadingHandler;
import at.furti.springrest.client.repository.lazy.LazyLoadingHandler;
import at.furti.springrest.client.repository.lazy.LazyObjectLoadingHandler;
import at.furti.springrest.client.util.RepositoryUtils;

public class EntityPlasticClassTransformer extends
		AbstractEntityPlasticClassTransformer implements
		PlasticClassTransformer {

	private Logger logger = LoggerFactory.getLogger(getClass());

	Map<String, String> lazyProperties;
	private String repoRel;
	private String selfLink;
	private DataRestClient client;

	/**
	 * @param lazyProperties
	 *            a map containing the rel of the property as key and the url
	 *            for the property as value
	 * @param selfLink
	 *            the self link of the entity
	 * @param repoRel
	 *            the repositoryRel
	 * @param client
	 *            the client used to lazyly initialize the object
	 */
	public EntityPlasticClassTransformer(Map<String, String> lazyProperties,
			String selfLink, String repoRel, DataRestClient client) {
		super();
		this.lazyProperties = lazyProperties;
		this.repoRel = repoRel;
		this.client = client;
		this.selfLink = selfLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.tapestry5.plastic.PlasticClassTransformer#transform(org.apache
	 * .tapestry5.plastic.PlasticClass)
	 */
	public void transform(PlasticClass plasticClass) {
		// At first add the field for the self link.
		appendSelfUriField(plasticClass, selfLink);

		try {
			// initialize all lazy loading properties
			if (lazyProperties != null) {
				Class<?> superClass = Class.forName(plasticClass
						.getSuperClassName());

				for (String propertyRel : lazyProperties.keySet()) {
					Field propertyField = getPropertyField(superClass,
							propertyRel);

					if (propertyField != null) {
						LazyLoadingHandler handler = createLazyLoadingHandler(
								propertyField, lazyProperties.get(propertyRel));

						PlasticField newField = plasticClass.introduceField(
								LazyLoadingHandler.class,
								propertyField.getName() + "_lazyHandler");

						newField.inject(handler);

						adviceGetter(plasticClass, superClass, propertyField);
						adviceSetter(plasticClass, superClass, propertyField);
					} else {
						logger.warn("Property [{}] not found", propertyRel);
					}
				}
			}
		} catch (ClassNotFoundException ex) {
			logger.error("Error getting superclass", ex);
		} catch (NoSuchMethodException ex) {
			logger.error("Error adding handler advice", ex);
		}
	}

	/**
	 * @param plasticClass
	 * @param superClass
	 * @param propertyField
	 * @throws NoSuchMethodException
	 */
	private void adviceSetter(PlasticClass plasticClass, Class<?> superClass,
			Field propertyField) throws NoSuchMethodException {
		Method m = superClass.getMethod(
				"set" + StringUtils.capitalize(propertyField.getName()),
				propertyField.getType());

		PlasticMethod newMethod = plasticClass.introduceMethod(m);

		newMethod.addAdvice(new SetLazyHandlerAdvice(propertyField.getName()));
	}

	/**
	 * @param plasticClass
	 * @param superClass
	 * @param propertyField
	 * @throws NoSuchMethodException
	 */
	private void adviceGetter(PlasticClass plasticClass, Class<?> superClass,
			Field propertyField) throws NoSuchMethodException {
		Method m = superClass.getMethod("get"
				+ StringUtils.capitalize(propertyField.getName()));

		PlasticMethod newMethod = plasticClass.introduceMethod(m);

		newMethod.addAdvice(new GetLazyHandlerAdvice(propertyField.getName()));
	}

	/**
	 * @param propertyField
	 * @return
	 */
	private LazyLoadingHandler createLazyLoadingHandler(Field propertyField,
			String href) {
		if (Collection.class.isAssignableFrom(propertyField.getType())) {
			return new LazyCollectionLoadingHandler(propertyField.getType(),
					getGenericType(propertyField), client, href, repoRel);
		} else {
			return new LazyObjectLoadingHandler(client, href,
					propertyField.getType(), repoRel);
		}
	}

	/**
	 * @param propertyField
	 * @return
	 */
	private Class<?> getGenericType(Field propertyField) {
		Type type = propertyField.getGenericType();

		if (type instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) type;

			for (Type actualType : paramType.getActualTypeArguments()) {
				if (actualType instanceof Class) {
					return (Class<?>) actualType;
				}
			}
		}
		return null;
	}

	/**
	 * @param superClass
	 * @param propertyRel
	 * @return
	 */
	private Field getPropertyField(Class<?> superClass, String propertyRel) {
		Assert.notNull(superClass, "Superclass must not be null");
		Assert.notNull(propertyRel, "PropertyRel must not be null");

		Field[] fields = superClass.getDeclaredFields();

		for (Field field : fields) {
			String fieldRel = generateRel(superClass, field);

			if (fieldRel.equals(propertyRel)) {
				return field;
			}
		}

		return null;
	}

	/**
	 * @param plasticClass
	 * @param field
	 * @return
	 */
	private String generateRel(Class<?> type, Field field) {
		StringBuilder relBuilder = new StringBuilder(repoRel);

		relBuilder.append(".").append(RepositoryUtils.getEntityRel(type));
		relBuilder.append(".").append(RepositoryUtils.getFieldRel(field));
		return relBuilder.toString();
	}
}
