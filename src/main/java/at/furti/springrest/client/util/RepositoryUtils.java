package at.furti.springrest.client.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.repository.annotation.RestResource;

import at.furti.springrest.client.http.RequestType;

public final class RepositoryUtils {

	private static final Pattern REPO_PATTERN = Pattern.compile("Repository");

	/**
	 * Methods from the crud repository. The url for this methods is the same as
	 * for the repository.
	 */
	public static final RepositoryMethods TOPLEVEL_METHODS = new RepositoryMethods();

	/**
	 * @param repoClass
	 * @return
	 */
	public static String getRepositoryId(Class<?> repoClass) {
		return StringUtils.uncapitalize(repoClass.getSimpleName());
	}

	/**
	 * @param repoClass
	 * @return
	 */
	public static String getRepositoryRel(Class<?> repoClass) {
		if (repoClass.isAnnotationPresent(RestResource.class)) {
			RestResource annotation = repoClass.getAnnotation(RestResource.class);

			if (StringUtils.isNotEmpty(annotation.rel())) {
				return annotation.rel();
			}
		}

		String repoName = getRepositoryId(repoClass);

		return REPO_PATTERN.matcher(repoName).replaceAll("");
	}

	/**
	 * Returns the rel for the method.
	 * 
	 * @param m
	 *          the method
	 * @return the rel for the method. May be null for methods with the same
	 *         location as the repository. Like findAll
	 */
	public static String getMethodRel(String repoRel, Method m) {
		if (m == null) {
			return null;
		}

		String name = m.getName();

		if (TOPLEVEL_METHODS.contains(name)) {
			return null;
		}

		if (m.isAnnotationPresent(RestResource.class)) {
			RestResource annotation = m.getAnnotation(RestResource.class);

			if (StringUtils.isNotBlank(annotation.rel())) {
				return annotation.rel();
			}
		}

		return repoRel + "." + StringUtils.uncapitalize(name);
	}

	/**
	 * @param m
	 * @return
	 */
	public boolean isCrudMethod(Method m) {
		if (m == null) {
			return false;
		}

		String name = m.getName();

		return TOPLEVEL_METHODS.contains(name);
	}

	/**
	 * Check which HTTPMethod to use for the request
	 * 
	 * @param m
	 * @return
	 */
	public static RequestType getRequestType(Method m) {

		if (m.getName().startsWith("save")) {
			return RequestType.POST;
		}

		if (m.getName().startsWith("delete")) {
			return RequestType.DELETE;
		}

		return RequestType.GET;
	}

	/**
	 * @param clazz
	 * @return
	 */
	public static Class<?> extractEntryType(Class<?> clazz) {
		Type[] types = clazz.getGenericInterfaces();

		for (Type t : types) {
			if (t instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) t;

				if (paramType.getRawType().equals(CrudRepository.class)) {
					Type[] typeArguments = paramType.getActualTypeArguments();

					for (Type argument : typeArguments) {
						if (argument instanceof Class) {
							return (Class<?>) argument;
						} else {
							return null;
						}
					}
				}
			}
		}

		// Type[] typeArguments = ((ParameterizedType) t).getActualTypeArguments();
		//
		// if (typeArguments.length == 0) {
		// return null;
		// }
		//
		// Type typeToReturn = typeArguments[0];
		// }
		return null;
	}
}
