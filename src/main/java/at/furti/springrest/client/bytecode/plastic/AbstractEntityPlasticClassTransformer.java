package at.furti.springrest.client.bytecode.plastic;

import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticField;

import at.furti.springrest.client.util.IdentifierUtils;

public class AbstractEntityPlasticClassTransformer {

	/**
	 * @param plasticClass
	 * @param selfLink
	 */
	protected PlasticField appendSelfUriField(PlasticClass plasticClass,
			String selfLink) {
		PlasticField selfUriField = plasticClass.introduceField(String.class,
				IdentifierUtils.IDENTIFIER_NAME);

		// Set the selfLink if not null
		if (selfLink != null) {
			selfUriField.inject(selfLink);
		}

		return selfUriField;
	}
}
