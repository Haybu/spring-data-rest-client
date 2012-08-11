package at.furti.springrest.client.config;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.data.PersonRepository;
import at.furti.springrest.client.http.DataRestClient;
import at.furti.springrest.client.http.Request;
import at.furti.springrest.client.http.Response;
import at.furti.springrest.client.http.link.LinkManager;

@ContextConfiguration(locations = "classpath:at/furti/springrest/client/applicationContext.xml")
public class ApplicationContextTest extends AbstractTestNGSpringContextTests {

	@Autowired(required = false)
	private PersonRepository repository;

	@Autowired(required = false)
	private DataRestClient client;

	@Test
	public void containsPersonRepositoryId() {
		Assert.assertTrue(applicationContext.containsBean("personRepository"),
				"Bean [personRepository] not found");
	}

	@Test
	public void personRepositoryInject() {
		Assert.assertNotNull(repository, "Repository was not injected");
	}

	@Test
	public void testClient() throws IOException {
		Assert.assertNotNull(client, "Client should not be null");

		Response response = client.executeGet(new Request());

		String s = IOUtils.toString(response.getStream());

		Assert.assertTrue(StringUtils.isNotBlank(s), "Blank response");
	}

	@Test
	public void testLinkManager() {
		LinkManager linkManager = new LinkManager(client);

		String href = linkManager.getHref("person");

		Assert.assertEquals(href,
				"http://furti.springrest.cloudfoundry.com/person",
				"PersonRepository href not equals");

		href = linkManager.getHref("address");

		Assert.assertEquals(href,
				"http://furti.springrest.cloudfoundry.com/address",
				"AddressRepository href not equals");
	}
}
