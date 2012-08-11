package at.furti.springrest.client.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.AddressEntity;
import at.furti.springrest.client.data.AddressRepository;
import at.furti.springrest.client.util.IdentifierUtils;

@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class AddressrepositoryTest extends AbstractTestNGSpringContextTests {

	@Autowired(required = false)
	private AddressRepository repository;

	@Test
	public void addressRepositoryInject() {
		Assert.assertNotNull(repository, "Repository was not injected");
	}

	@Test(dependsOnMethods = "addressRepositoryInject")
	public void findOne1() {
		AddressEntity address = repository.findOne(new Integer(1));

		Assert.assertNotNull(address, "Person with id [1] not found");

		Assert.assertEquals(address.getStreet(), "Musterstrasse 1",
				"Street not equals");

		Assert.assertNotNull(address.getCity(), "City was null");
		Assert.assertEquals(address.getCity().getCityId(), new Integer(1),
				"CityId not equals");
		Assert.assertEquals(address.getCity().getPlz(), "1234",
				"PLZ not equals");
		Assert.assertEquals(address.getCity().getName(), "Musterstadt",
				"Name not equals");

		Assert.assertEquals(IdentifierUtils.getIdentifier(address),
				"http://furti.springrest.cloudfoundry.com/address/1",
				"Self Link not equals");
	}

	@Test(dependsOnMethods = "addressRepositoryInject")
	public void findOne0() {
		AddressEntity address = repository.findOne(new Integer(0));

		Assert.assertNull(address, "Address with id [0] was found");
	}
}
