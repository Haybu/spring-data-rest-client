package at.furti.springrest.client.http;

import org.testng.Assert;
import org.testng.annotations.Test;

import at.furti.springrest.client.http.Request.ParameterType;

@Test
public class RequestTest {

	@Test
	public void testPath() {
		String path = "test/path";

		Request request = new Request(path);

		Assert.assertEquals(request.getUrl(), path, "Path not equals");
	}

	@Test
	public void testQueryParameter() {
		String path = "test/all";

		Request request = new Request(path).addParameter(ParameterType.QUERY,
				"query", "queryvalue").addParameter(ParameterType.QUERY,
				"query2", "queryvalue2");

		String url = request.getUrl();

		checkQuery(url, path);
	}

	private void checkQuery(String url, String path) {
		Assert.assertTrue(url.startsWith(path + "?"),
				"url does not start with [ " + path + "?]");

		int questionMarkPosition = url.indexOf("?");

		String queryString = url.substring(questionMarkPosition + 1);
		Assert.assertTrue(queryString.contains("query=queryvalue"));
		Assert.assertTrue(queryString.contains("query2=queryvalue2"));
	}

	@Test
	public void testPathParameter() {
		String path = "test/{test}/all/{again}";

		Request request = new Request(path).addParameter(ParameterType.PATH,
				"test", "value").addParameter(ParameterType.PATH, "again",
				"othervalue");

		Assert.assertEquals(request.getUrl(), "test/value/all/othervalue");
	}

	@Test
	public void testAll() {
		String path = "test/{test}/all/{again}";

		Request request = new Request(path)
				.addParameter(ParameterType.PATH, "test", "value")
				.addParameter(ParameterType.PATH, "again", "othervalue")
				.addParameter(ParameterType.QUERY, "query", "queryvalue")
				.addParameter(ParameterType.QUERY, "query2", "queryvalue2");

		checkQuery(request.getUrl(), "test/value/all/othervalue");
	}
}
