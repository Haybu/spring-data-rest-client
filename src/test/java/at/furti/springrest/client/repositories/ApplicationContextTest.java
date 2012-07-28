package at.furti.springrest.client.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.PersonRepository;

@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class ApplicationContextTest extends AbstractTestNGSpringContextTests {

	@Autowired(required = false)
	private PersonRepository repository;

	@Test
	public void containsPersonRepository() {
		Assert.assertTrue(applicationContext.containsBean("personRepository"),
				"Bean personRepository not found");
		Assert.assertNotNull(repository, "Repository was not injected");

		System.out.println("**********************************************");
		System.out.println("FindOne");
		System.out.println("return value = " + repository.findOne(new Integer(2)).getPersonId());
		
		System.out.println("**********************************************");
	}
}
