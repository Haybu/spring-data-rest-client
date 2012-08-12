package at.furti.springrest.client.repositories.person;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.PersonEntity;
import at.furti.springrest.client.data.PersonRepository;
import at.furti.springrest.client.util.IdentifierUtils;

@Test(groups = { "all", "person" })
@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class PersonrepositoryFindAllTest extends
		AbstractTestNGSpringContextTests {

	@Autowired(required = false)
	private PersonRepository repository;

	@Test
	public void personRepositoryInject() {
		Assert.assertNotNull(repository, "Repository was not injected");
	}

	@Test(dependsOnMethods = "personRepositoryInject")
	public void findAll() {
		Iterable<PersonEntity> persons = repository.findAll();

		Assert.assertNotNull(persons, "No Personsn found");
		checkSize(persons);
		checkContent(persons);
	}

	@Test(dependsOnMethods = "personRepositoryInject")
	public void findAllIds() {
		Iterable<PersonEntity> persons = repository
				.findAll(Arrays.asList(1, 0));

		Assert.assertNotNull(persons, "No Personsn found");
		checkSize(persons);

		Iterator<PersonEntity> it = persons.iterator();

		boolean nullFound = false;

		while (it.hasNext()) {
			PersonEntity p = it.next();

			if (p == null) {
				nullFound = true;
			} else {
				check1(p);
			}
		}

		Assert.assertTrue(nullFound, "No Null entry was found");
	}

	/**
	 * @param persons
	 */
	private void checkSize(Iterable<PersonEntity> persons) {
		Iterator<PersonEntity> it = persons.iterator();

		int size = 0;

		while (it.hasNext()) {
			it.next();
			size++;
		}

		Assert.assertEquals(size, 2, "Size not equals 2");
	}

	/**
	 * @param persons
	 */
	private void checkContent(Iterable<PersonEntity> persons) {
		Iterator<PersonEntity> it = persons.iterator();

		while (it.hasNext()) {
			PersonEntity p = it.next();

			if (IdentifierUtils.getIdentifier(p).equals(
					"http://furti.springrest.cloudfoundry.com/person/1")) {
				check1(p);
			} else {
				check2(p);
			}
		}
	}

	private void check1(PersonEntity person) {
		Assert.assertNotNull(person, "Person with id [1] not found");

		Calendar expectedBirthDate = new GregorianCalendar(1990, 9, 10);

		Assert.assertEquals(person.getFirstName(), "Max",
				"Firstname not equals");
		Assert.assertEquals(person.getLastName(), "Mustermann",
				"Lastname not equals");
		Assert.assertTrue(
				DateUtils.isSameDay(person.getBirthDate(),
						expectedBirthDate.getTime()), "Birthdate not equals");

		Assert.assertNotNull(person.getAddress(), "Address is null");
		Assert.assertEquals(person.getAddress().getStreet(), "Musterstrasse 1",
				"Street not equals");

		Assert.assertNotNull(person.getAddress().getCity(), "City was null");
		Assert.assertEquals(person.getAddress().getCity().getCityId(),
				new Integer(1), "CityId not equals");
		Assert.assertEquals(person.getAddress().getCity().getPlz(), "1234",
				"PLZ not equals");
		Assert.assertEquals(person.getAddress().getCity().getName(),
				"Musterstadt", "Name not equals");

		person.setAddress(null);
		Assert.assertNull(person.getAddress(), "Address is not null");

		Assert.assertEquals(IdentifierUtils.getIdentifier(person),
				"http://furti.springrest.cloudfoundry.com/person/1",
				"Self Link not equals");
	}

	private void check2(PersonEntity person) {
		Assert.assertNotNull(person, "Person with id [2] not found");

		Calendar expectedBirthDate = new GregorianCalendar(1980, 0, 1);

		Assert.assertEquals(person.getFirstName(), "Franz",
				"Firstname not equals");
		Assert.assertEquals(person.getLastName(), "Test", "Lastname not equals");
		Assert.assertTrue(
				DateUtils.isSameDay(person.getBirthDate(),
						expectedBirthDate.getTime()), "Birthdate not equals");

		Assert.assertNotNull(person.getAddress(), "Address is null");
		Assert.assertEquals(person.getAddress().getStreet(), "Teststrasse 2",
				"Street not equals");

		Assert.assertNotNull(person.getAddress().getCity(), "City was null");
		Assert.assertEquals(person.getAddress().getCity().getCityId(),
				new Integer(2), "CityId not equals");
		Assert.assertEquals(person.getAddress().getCity().getPlz(), "5678",
				"PLZ not equals");
		Assert.assertEquals(person.getAddress().getCity().getName(),
				"Teststadt", "Name not equals");

		person.setAddress(null);
		Assert.assertNull(person.getAddress(), "Address is not null");

		Assert.assertEquals(IdentifierUtils.getIdentifier(person),
				"http://furti.springrest.cloudfoundry.com/person/2",
				"Self Link not equals");
	}
}
