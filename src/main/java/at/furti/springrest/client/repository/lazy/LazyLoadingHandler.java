package at.furti.springrest.client.repository.lazy;

/**
 * Interface for lazy loading of entity properties.
 * 
 * @author Daniel Furtlehner
 * 
 */
public interface LazyLoadingHandler {

	/**
	 * Returns the target object.
	 * 
	 * If the object was not fetched from the server yet. Should be done now.
	 * Then the Object fetched from the server should be returned.
	 * 
	 * @return
	 */
	public Object doGet();

	/**
	 * Overrides the target Object in the handler. If the object was not
	 * previously fetched from the server, it must not be fetched anymore after
	 * this call.
	 * 
	 * @param newValue the new Value to set
	 */
	public void doSet(Object newValue);
}
