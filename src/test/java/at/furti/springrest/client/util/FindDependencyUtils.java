package at.furti.springrest.client.util;

import org.testng.Assert;

import at.furti.springrest.client.data.find.FindDependencyEntity;

public class FindDependencyUtils {

	public static FindDependencyEntity create(Integer id, String stringProperty) {
		FindDependencyEntity entity = new FindDependencyEntity();

		entity.setId(id);
		entity.setStringProperty(stringProperty);

		return entity;
	}

	public static FindDependencyEntity create1() {
		return create(1, "dependency1");
	}

	public static FindDependencyEntity create2() {
		return create(2, "dependency2");
	}

	public static FindDependencyEntity create3() {
		return create(3, "dependency3");
	}

	public static FindDependencyEntity create4() {
		return create(4, "dependency4");
	}

	/**
	 * @param actual
	 * @param expected
	 */
	public static void equals(FindDependencyEntity actual,
			FindDependencyEntity expected) {
		Assert.assertEquals(actual.getStringProperty(),
				expected.getStringProperty(), "String property not equals");
	}

	/**
	 * @param entity
	 * @return
	 */
	public static String createIdentifier(FindDependencyEntity entity) {
		if (entity == null || entity.getId() == null) {
			return "";
		}

		StringBuilder builder = new StringBuilder(TestUtils.HOST);
		builder.append("findDependency/").append(entity.getId());

		return builder.toString();
	}
}
