package at.furti.springrest.client.json;

import org.apache.tapestry5.json.JSONObject;

public class JsonWorker {

	private JSONObject object;

	public JsonWorker(JSONObject object) {
		this.object = object;
	}

	public JSONObject getObject() {
		return object;
	}
}
