package at.furti.springrest.client.repositories;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.PersonEntity;
import at.furti.springrest.client.data.PersonRepository;
import at.furti.springrest.client.util.IdentifierUtils;

@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class PersonrepositoryTest extends AbstractTestNGSpringContextTests {

	@Autowired(required = false)
	private PersonRepository repository;

	@Test
	public void personRepositoryInject() {
		Assert.assertNotNull(repository, "Repository was not injected");
	}

	@Test(dependsOnMethods = "personRepositoryInject")
	public void findOne1() {
		PersonEntity person = repository.findOne(new Integer(1));

		Assert.assertNotNull(person, "Person with id [1] not found");

		Calendar expectedBirthDate = new GregorianCalendar(1990, 9, 10);

		Assert.assertEquals(person.getFirstName(), "Max",
				"Firstname not equals");
		Assert.assertEquals(person.getLastName(), "Mustermann",
				"Lastname not equals");
		Assert.assertTrue(
				DateUtils.isSameDay(person.getBirthDate(),
						expectedBirthDate.getTime()), "Birthdate not equals");

		Assert.assertEquals(IdentifierUtils.getIdentifier(person),
				"http://furti.springrest.cloudfoundry.com/person/1",
				"Self Link not equals");
	}

	@Test(dependsOnMethods = "personRepositoryInject")
	public void findOne0() {
		PersonEntity person = repository.findOne(new Integer(0));

		Assert.assertNull(person, "Person with id [0] was found");
	}
}
