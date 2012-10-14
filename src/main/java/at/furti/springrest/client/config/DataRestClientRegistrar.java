package at.furti.springrest.client.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

public class DataRestClientRegistrar implements ImportBeanDefinitionRegistrar {

	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {
		Assert.notNull(importingClassMetadata);

	}

}
