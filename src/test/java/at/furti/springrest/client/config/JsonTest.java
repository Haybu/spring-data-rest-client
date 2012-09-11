package at.furti.springrest.client.config;

import java.util.GregorianCalendar;

import org.apache.tapestry5.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.AddressEntity;
import at.furti.springrest.client.data.CityEntity;
import at.furti.springrest.client.data.PersonEntity;
import at.furti.springrest.client.json.EntityToJsonWorker;

@Test(groups = { "all", "json" })
public class JsonTest {

	@Test
	public void testEntityToJson() throws Exception {
		PersonEntity person = new PersonEntity();
		GregorianCalendar birthdate = new GregorianCalendar(2000, 0, 1);

		person.setFirstName("Test");
		person.setLastName("Person");
		person.setBirthDate(birthdate.getTime());
		person.setAddress(new AddressEntity());

		person.getAddress().setAddressId(2);
		person.getAddress().setStreet("Teststraße");
		person.getAddress().setCity(new CityEntity());
		person.getAddress().getCity().setCityId(1);
		person.getAddress().getCity().setName("Teststadt");
		person.getAddress().getCity().setPlz("2345");

		EntityToJsonWorker worker = new EntityToJsonWorker(person, null);

		JSONObject json = worker.toJson();

		Assert.assertEquals(json.get("firstName"), person.getFirstName(),
				"Firstname not equals");
		Assert.assertEquals(json.get("lastName"), person.getLastName(),
				"Lastname not equals");
		Assert.assertEquals(json.get("birthDate"), person.getBirthDate()
				.getTime(), "Birthdate not equals");

		JSONObject address = json.getJSONObject("address");

		Assert.assertEquals(address.get("street"), "Teststraße");

		JSONObject city = address.getJSONObject("city");
		
		Assert.assertEquals(city.get("name"), "Teststadt");
		Assert.assertEquals(city.get("plz"), "2345");
	}
}
