package at.furti.springrest.client.repository;

import java.util.Iterator;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticClassTransformer;
import org.apache.tapestry5.plastic.PlasticField;

import at.furti.springrest.client.util.IdentifierUtils;
import at.furti.springrest.client.util.JsonUtils;

public class EntityClassTransformer implements PlasticClassTransformer {

	private JSONObject data;

	public EntityClassTransformer(JSONObject data) {
		super();
		this.data = data;
	}

	public void transform(PlasticClass plasticClass) {
		PlasticField selfUriField = plasticClass.introduceField(String.class,
				IdentifierUtils.IDENTIFIER_NAME);

		selfUriField.inject(getSelfLink());

		// TODO: add lazy initializing objects
	}

	/**
	 * @param data2
	 * @return
	 */
	private Object getSelfLink() {
		if (data == null || !data.has(JsonUtils.LINKS)
				|| data.isNull(JsonUtils.LINKS)) {
			return null;
		}

		JSONArray links = data.getJSONArray(JsonUtils.LINKS);

		Iterator<Object> it = links.iterator();

		while (it.hasNext()) {
			JSONObject link = (JSONObject) it.next();

			if (link.getString(JsonUtils.REL).equals("self")) {
				return link.getString(JsonUtils.HREF);
			}
		}

		return null;
	}
}
