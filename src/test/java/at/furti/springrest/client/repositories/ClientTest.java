package at.furti.springrest.client.repositories;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import at.furti.springrest.client.http.DataRestClientBase;
import at.furti.springrest.client.http.commonsclient.CommonsDataRestClient;
import at.furti.springrest.client.util.RestCollectionUtils;

@Test(groups = "client")
public class ClientTest {

	private DataRestClientBase client;

	@BeforeClass
	public void setupClient() {
		client = new CommonsDataRestClient("http://127.0.0.1:8080/managerserver/");
	}

	@Test
	public void testGet() throws IOException {
		InputStream stream = client.executeGet(null);
		System.out.println(IOUtils.toString(stream, "UTF-8"));

		stream = client.executeGet(RestCollectionUtils.toCollection("events"));
		System.out.println(IOUtils.toString(stream, "UTF-8"));
		stream.close();

		stream = client.executeGet(RestCollectionUtils
				.toCollection("personKleidung/search/findKleidungForPerson"),
				"personId", "1");
		System.out.println(IOUtils.toString(stream, "UTF-8"));
		stream.close();
	}
}
