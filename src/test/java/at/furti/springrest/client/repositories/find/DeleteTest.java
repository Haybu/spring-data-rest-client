package at.furti.springrest.client.repositories.find;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.find.FindRepository;
import at.furti.springrest.client.repository.exception.NotExportedException;

@Test(groups = { "all", "find" })
@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class DeleteTest extends AbstractTestNGSpringContextTests {

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
	@Test(dependsOnMethods = "repositoryInject", expectedExceptions = NotExportedException.class)
	public void deleteEntity() throws Throwable {
		try {
			repository.delete(repository.findOne(new Integer(1)));
		} catch (Exception ex) {
			throw ex.getCause();
		}
	}

	@Test(dependsOnMethods = "repositoryInject", expectedExceptions = NotExportedException.class)
	public void deleteId() throws Throwable {
		try {
			repository.delete(new Integer(0));
		} catch (Exception ex) {
			throw ex.getCause();
		}
	}

	@Test(dependsOnMethods = "repositoryInject", expectedExceptions = NotExportedException.class)
	public void deleteAll() throws Throwable {
		try {
			repository.deleteAll();
		} catch (Exception ex) {
			throw ex.getCause();
		}
	}

	@Test(dependsOnMethods = "repositoryInject", expectedExceptions = NotExportedException.class)
	public void deleteIds() throws Throwable {
		try {
			repository.delete(repository.findAll());
		} catch (Exception ex) {
			throw ex.getCause();
		}
	}
}
