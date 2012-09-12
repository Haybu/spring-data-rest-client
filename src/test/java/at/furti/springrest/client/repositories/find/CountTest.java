package at.furti.springrest.client.repositories.find;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.find.FindRepository;

@Test(groups = { "all", "find" })
@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class CountTest extends AbstractTestNGSpringContextTests {

	@Autowired(required = false)
	private FindRepository repository;

	/**
	 * 
	 */
	@Test
	public void repositoryInject() {
		Assert.assertNotNull(repository, "Repository was not injected");
	}

	/**
	 * 
	 */
	@Test(dependsOnMethods = "repositoryInject")
	public void count() {
		Assert.assertEquals(repository.count(), 4,
				"Count not equals 4");
	}
}
