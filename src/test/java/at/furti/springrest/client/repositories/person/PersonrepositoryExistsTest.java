package at.furti.springrest.client.repositories.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.PersonRepository;

@Test(groups = { "all", "person" })
@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class PersonrepositoryExistsTest extends
		AbstractTestNGSpringContextTests {

	@Autowired(required = false)
	private PersonRepository repository;

	@Test
	public void personRepositoryInject() {
		Assert.assertNotNull(repository, "Repository was not injected");
	}

	@Test(dependsOnMethods = "personRepositoryInject")
	public void exists1() {
		Assert.assertTrue(repository.exists(new Integer(1)),
				"Person [1] does not exist");
	}

	@Test(dependsOnMethods = "personRepositoryInject")
	public void exists0() {
		Assert.assertFalse(repository.exists(new Integer(0)),
				"Person [0] does exist");
	}
}
