package at.furti.springrest.client.bytecode;

import java.util.Map;

import org.apache.tapestry5.plastic.PlasticClassTransformer;

import at.furti.springrest.client.bytecode.plastic.EntityPlasticClassTransformer;
import at.furti.springrest.client.http.DataRestClient;

/**
 * Transformer used to transform entity classes.
 * 
 * Initializes all lazy properties with an proxy object that loads the object on
 * demand.
 * 
 * @author Daniel Furtlehner
 * 
 */
public class EntityClassTransformer extends ClassTransformer {

	public static final String LAZY_PROPERTIES_KEY = "lazyProperties";
	public static final String SELF_LINK_KEY = "selfLink";
	public static final String REPO_REL_KEY = "repoRel";
	public static final String CLIENT_KEY = "client";

	private static final EntityClassTransformer INSTANCE = new EntityClassTransformer();

	public static EntityClassTransformer getInstance() {
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PlasticClassTransformer getClassTransformer(Class<?> type,
			Map<String, Object> parameters) {
		return new EntityPlasticClassTransformer(
				(Map<String, String>) getParameter(LAZY_PROPERTIES_KEY,
						parameters), (String) getParameter(SELF_LINK_KEY,
						parameters), (String) getParameter(REPO_REL_KEY,
						parameters), (DataRestClient) getParameter(CLIENT_KEY,
						parameters));
	}
}
