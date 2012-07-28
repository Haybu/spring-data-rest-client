package at.furti.springrest.client.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class RestRepositoryNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("repositories",
				new RestRepositoryBeanDefinitionParser());
	}
}
