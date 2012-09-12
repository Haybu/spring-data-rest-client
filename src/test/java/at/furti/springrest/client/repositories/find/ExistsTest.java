package at.furti.springrest.client.repositories.find;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.find.FindRepository;

@Test(groups = { "all", "find" })
@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class ExistsTest extends AbstractTestNGSpringContextTests {

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
	public void exists1() {
		Assert.assertTrue(repository.exists(new Integer(1)),
				"FindEntity 1 should exist");
	}
	
	/**
	 * 
	 */
	@Test(dependsOnMethods = "repositoryInject")
	public void exists2() {
		Assert.assertTrue(repository.exists(new Integer(2)),
				"FindEntity 2 should exist");
	}
	
	/**
	 * 
	 */
	@Test(dependsOnMethods = "repositoryInject")
	public void exists3() {
		Assert.assertTrue(repository.exists(new Integer(3)),
				"FindEntity 3 should exist");
	}
	
	/**
	 * 
	 */
	@Test(dependsOnMethods = "repositoryInject")
	public void exists4() {
		Assert.assertTrue(repository.exists(new Integer(4)),
				"FindEntity 4 should exist");
	}

	/**
	 * 
	 */
	@Test(dependsOnMethods = "repositoryInject")
	public void exists0() {
		Assert.assertFalse(repository.exists(new Integer(0)),
				"FindEntity 0 should not exist");
	}
}
