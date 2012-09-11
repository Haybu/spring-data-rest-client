package at.furti.springrest.client.repositories.find;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.find.FindEntity;
import at.furti.springrest.client.data.find.FindRepository;
import at.furti.springrest.client.util.TestUtils;

@Test(groups = { "all", "find" })
@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class FindAllTest extends AbstractTestNGSpringContextTests {

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
	public void findAll() {
		Iterable<FindEntity> entities = repository.findAll();

		Assert.assertNotNull(entities, "Entities not found");
		Assert.assertTrue(TestUtils.getSize(entities) == 4, "Size not equals 4");
	}
}
