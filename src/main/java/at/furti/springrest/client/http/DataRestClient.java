package at.furti.springrest.client.http;

import java.io.IOException;

/**
 * 
 * @author Daniel Furtlehner
 * 
 */
public interface DataRestClient {

	/**
	 * Sends a get request and returns all relevant response data.
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public Response executeGet(Request request) throws IOException;

	/**
	 * Sends a post request and returns the relevant response data.
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public Response executePost(Request request) throws IOException;

	/**
	 * Sends a delete request and returns the relevant response data.
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public Response executeDelete(Request request) throws IOException;
}