package at.furti.springrest.client.json;

import java.io.IOException;

import org.apache.tapestry5.json.JSONObject;
import org.springframework.util.StringUtils;

public class JsonUtils {

	public static final String CONTENT = "content";

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static JSONObject toJsonObject(byte[] data) throws IOException {
		String jsonstring = new String(data, "UTF-8");

		// If empty response or no json object --> null
		if (!StringUtils.hasText(jsonstring) || jsonstring.charAt(0) != '{') {
			return null;
		}

		return new JSONObject(jsonstring);
	}
}
