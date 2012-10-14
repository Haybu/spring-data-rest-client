package at.furti.springrest.client.config;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(classes = ApplicationConfig.class)
public class AnnotationConfigTest extends AbstractTestNGSpringContextTests {

	@Test
	public void repositoryInject() {

	}
}
