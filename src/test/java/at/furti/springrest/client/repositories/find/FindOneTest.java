package at.furti.springrest.client.repositories.find;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.find.FindDependencyEntity;
import at.furti.springrest.client.data.find.FindDependencyRepository;
import at.furti.springrest.client.data.find.FindEntity;
import at.furti.springrest.client.data.find.FindRepository;
import at.furti.springrest.client.util.FindDependencyUtils;
import at.furti.springrest.client.util.FindUtils;
import at.furti.springrest.client.util.IdentifierUtils;

@Test(groups = { "all", "find" })
@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class FindOneTest extends AbstractTestNGSpringContextTests {

	@Autowired(required = false)
	private FindRepository repository;

	@Autowired(required = false)
	private FindDependencyRepository dependencyRepository;

	/**
	 * 
	 */
	@Test
	public void repositoryInject() {
		Assert.assertNotNull(repository, "Repository was not injected");
		Assert.assertNotNull(dependencyRepository,
				"DependencyRepository was not injected");
	}

	/**
	 * 
	 */
	@Test(dependsOnMethods = "repositoryInject")
	public void findDependency1() {
		FindDependencyEntity dependency = dependencyRepository
				.findOne(new Integer(1));

		Assert.assertNotNull(dependency, "dependency with id [1] not found");

		FindDependencyUtils.equals(dependency, FindDependencyUtils.create1());

		Assert.assertEquals(IdentifierUtils.getIdentifier(dependency),
				"http://furti-springrest.cloudfoundry.com/findDependency/1",
				"Self Link not equals");
	}

	/**
	 * 
	 */
	@Test(dependsOnMethods = "repositoryInject")
	public void findDependency0() {
		FindDependencyEntity dependency = dependencyRepository
				.findOne(new Integer(0));

		Assert.assertNull(dependency, "dependency with id [0] was found");
	}

	/**
	 * 
	 */
	@Test(dependsOnMethods = "findDependency1")
	public void findOne1() {
		FindEntity entity = repository.findOne(new Integer(1));

		Assert.assertNotNull(entity, "Entity with id [1] not found");
		FindUtils.equals(entity, FindUtils.create1());

		Assert.assertEquals(IdentifierUtils.getIdentifier(entity),
				"http://furti-springrest.cloudfoundry.com/find/1",
				"Self link not equals");
	}

	/**
	 * 
	 */
	@Test(dependsOnMethods = "findDependency1")
	public void findOne4() {
		FindEntity entity = repository.findOne(new Integer(4));

		Assert.assertNotNull(entity, "Entity with id [4] not found");
		FindUtils.equals(entity, FindUtils.create4());

		Assert.assertEquals(IdentifierUtils.getIdentifier(entity),
				"http://furti-springrest.cloudfoundry.com/find/4",
				"Self link not equals");
	}

	/**
	 * 
	 */
	@Test(dependsOnMethods = "repositoryInject")
	public void findOne0() {
		FindEntity entity = repository.findOne(new Integer(0));

		Assert.assertNull(entity, "dependency with id [0] was found");
	}
}
