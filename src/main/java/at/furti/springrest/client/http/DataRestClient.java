package at.furti.springrest.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * @author Daniel Furtlehner
 * 
 */
public interface DataRestClient {

	public enum ParameterType {
		NONE, QUERY, PATH
	}

	/**
	 * Executes a get request to the path. Path is relative.
	 * 
	 * Therefor the client has to be aware of the basePath for the application.
	 * 
	 * @param path
	 *          if null a request to the basePath will be performed
	 * @return
	 */
	public InputStream executeGet(Collection<String> paths, ParameterType parameterType, Object... parameters)
			throws IOException;

	/**
	 * Executes a post request to the path. Path is relative.
	 * 
	 * Therefor the client has to be aware of the basePath for the application.
	 * 
	 * @param path
	 *          if null a request to the basePath will be performed
	 * @return
	 */
	public InputStream executePost(Collection<String> paths) throws IOException;

	/**
	 * Executes a delete request to the path. Path is relative.
	 * 
	 * Therefor the client has to be aware of the basePath for the application.
	 * 
	 * @param path
	 *          if null a request to the basePath will be performed
	 * @return
	 */
	public InputStream executeDelete(Collection<String> paths) throws IOException;
}