package at.furti.springrest.client.json;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.json.JSONObject;
import org.springframework.util.StringUtils;

public class JsonUtils {

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static JSONObject toJsonObject(InputStream in) throws IOException {
		String jsonstring = IOUtils.toString(in, "UTF-8");
		in.close();

		// If empty response or no json object --> null
		if (!StringUtils.hasText(jsonstring) || jsonstring.charAt(0) != '{') {
			return null;
		}

		return new JSONObject(jsonstring);
	}
}
