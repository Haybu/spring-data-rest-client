package at.furti.springrest.client.util;

import org.testng.Assert;

import at.furti.springrest.client.data.find.NoRepositoryEntity;

public class NoRepoUtils {

	/**
	 * @param id
	 * @param stringProperty
	 * @return
	 */
	public static NoRepositoryEntity create(Integer id, String stringProperty) {
		NoRepositoryEntity entity = new NoRepositoryEntity();

		entity.setId(id);
		entity.setStringProperty(stringProperty);

		return entity;
	}

	/**
	 * @return
	 */
	public static NoRepositoryEntity create1() {
		return create(1, "noRepo1");
	}

	/**
	 * @return
	 */
	public static NoRepositoryEntity create2() {
		return create(2, "noRepo2");
	}

	/**
	 * @return
	 */
	public static NoRepositoryEntity create3() {
		return create(3, "noRepo3");
	}

	/**
	 * @return
	 */
	public static NoRepositoryEntity create4() {
		return create(4, "noRepo4");
	}

	/**
	 * @param actual
	 * @param expected
	 */
	public static void equals(NoRepositoryEntity actual, NoRepositoryEntity expected) {
		Assert.assertEquals(actual.getId(), expected.getId(), "Id not equals");
		Assert.assertEquals(actual.getStringProperty(),
				expected.getStringProperty(), "stringProperty not equals");
	}
}
