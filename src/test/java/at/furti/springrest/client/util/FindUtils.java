package at.furti.springrest.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.TimeZone;

import org.testng.Assert;

import at.furti.springrest.client.data.find.FindDependencyEntity;
import at.furti.springrest.client.data.find.FindEntity;

public class FindUtils {

	/**
	 * @return
	 */
	public static FindEntity create(String stringProperty,
			Boolean booleanProperty, GregorianCalendar dateProperty,
			Double doubleProperty, Integer intProperty, Long longProperty) {
		FindEntity entity = new FindEntity();

		entity.setStringProperty(stringProperty);
		entity.setBooleanProperty(booleanProperty);

		if (dateProperty != null) {
			dateProperty.setTimeZone(TimeZone.getTimeZone("UTC"));
			entity.setDateProperty(dateProperty.getTime());
		}

		entity.setDoubleProperty(doubleProperty);
		entity.setIntProperty(intProperty);
		entity.setLongProperty(longProperty);

		return entity;
	}

	/**
	 * @return
	 */
	public static FindEntity create1() {
		FindEntity entity = create("find1", Boolean.TRUE,
				new GregorianCalendar(1990, 0, 1), new Double(111.0),
				new Integer(1), new Long(11l));

		entity.setNoRepo(NoRepoUtils.create1());
		entity.setFindDependency(FindDependencyUtils.create1());

		FindDependencyEntity dep2 = FindDependencyUtils.create2();
		FindDependencyEntity dep4 = FindDependencyUtils.create4();

		entity.setList(new ArrayList<FindDependencyEntity>());
		entity.getList().add(dep2);
		entity.getList().add(dep4);

		entity.setSet(new HashSet<FindDependencyEntity>());
		entity.getSet().add(dep2);
		entity.getSet().add(dep4);

		return entity;
	}

	/**
	 * @return
	 */
	public static FindEntity create2() {
		FindEntity entity = create("find2", Boolean.FALSE,
				new GregorianCalendar(1990, 0, 2), new Double(222.0),
				new Integer(2), new Long(22l));

		entity.setNoRepo(NoRepoUtils.create2());
		entity.setFindDependency(FindDependencyUtils.create2());

		FindDependencyEntity dep2 = FindDependencyUtils.create2();
		FindDependencyEntity dep3 = FindDependencyUtils.create3();

		entity.setList(new ArrayList<FindDependencyEntity>());
		entity.getList().add(dep2);
		entity.getList().add(dep3);

		entity.setSet(new HashSet<FindDependencyEntity>());
		entity.getSet().add(dep2);
		entity.getSet().add(dep3);

		return entity;
	}

	/**
	 * @return
	 */
	public static FindEntity create3() {
		FindEntity entity = create("find3", Boolean.FALSE,
				new GregorianCalendar(1990, 0, 3), new Double(333.0),
				new Integer(3), new Long(33l));

		entity.setNoRepo(NoRepoUtils.create3());
		entity.setFindDependency(FindDependencyUtils.create3());

		FindDependencyEntity dep1 = FindDependencyUtils.create1();
		FindDependencyEntity dep2 = FindDependencyUtils.create2();
		FindDependencyEntity dep4 = FindDependencyUtils.create4();

		entity.setList(new ArrayList<FindDependencyEntity>());
		entity.getList().add(dep1);
		entity.getList().add(dep2);
		entity.getList().add(dep4);

		entity.setSet(new HashSet<FindDependencyEntity>());
		entity.getSet().add(dep1);
		entity.getSet().add(dep2);
		entity.getSet().add(dep4);

		return entity;
	}

	/**
	 * @return
	 */
	public static FindEntity create4() {
		FindEntity entity = create("find4", Boolean.TRUE,
				new GregorianCalendar(1990, 0, 4), new Double(444.0),
				new Integer(4), new Long(44l));

		entity.setFindDependency(FindDependencyUtils.create4());

		FindDependencyEntity dep1 = FindDependencyUtils.create1();
		FindDependencyEntity dep3 = FindDependencyUtils.create3();

		entity.setList(new ArrayList<FindDependencyEntity>());
		entity.getList().add(dep1);
		entity.getList().add(dep3);

		entity.setSet(new HashSet<FindDependencyEntity>());
		entity.getSet().add(dep1);
		entity.getSet().add(dep3);

		return entity;
	}

	/**
	 * @param actual
	 * @param expected
	 */
	public static void equals(FindEntity actual, FindEntity expected) {
		Assert.assertEquals(actual.getStringProperty(),
				expected.getStringProperty(), "String Property not equals");
		Assert.assertEquals(actual.getBooleanProperty(),
				expected.getBooleanProperty(), "Boolean Property not equals");
		Assert.assertEquals(actual.getDateProperty(),
				expected.getDateProperty(), "Date Property not equals");
		Assert.assertEquals(actual.getDoubleProperty(),
				expected.getDoubleProperty(), "Double Property not equals");
		Assert.assertEquals(actual.getIntProperty(), expected.getIntProperty(),
				"Int Property not equals");
		Assert.assertEquals(actual.getLongProperty(),
				expected.getLongProperty(), "Long Property not equals");

		if (expected.getNoRepo() != null) {
			Assert.assertNotNull(actual.getNoRepo(), "NoRepo not set");
			NoRepoUtils.equals(actual.getNoRepo(), expected.getNoRepo());
		} else {
			Assert.assertNull(actual.getNoRepo(), "NoRepo is set");
		}

		if (expected.getFindDependency() != null) {
			Assert.assertNotNull(actual.getFindDependency(),
					"FindDependency not set");
			FindDependencyUtils.equals(actual.getFindDependency(),
					expected.getFindDependency());
		} else {
			Assert.assertNull(actual.getFindDependency(),
					"FindDependency is set");
		}

		checkDependencies(actual.getList(), expected.getList(), "List");
		checkDependencies(actual.getSet(), expected.getSet(), "Set");
	}

	/**
	 * @param actual
	 * @param expected
	 */
	private static void checkDependencies(
			Collection<FindDependencyEntity> actual,
			Collection<FindDependencyEntity> expected, String property) {
		if (expected != null) {
			Assert.assertNotNull(actual, property + " is null");
			Assert.assertEquals(actual.size(), expected.size(), "Size of"
					+ property + "not equals");

			for (FindDependencyEntity expectedEntity : expected) {
				boolean found = false;
				String identifier = FindDependencyUtils
						.createIdentifier(expectedEntity);

				for (FindDependencyEntity actualEntity : actual) {
					if (identifier.equals(IdentifierUtils
							.getIdentifier(actualEntity))) {
						found = true;
						
						FindDependencyUtils
								.equals(actualEntity, expectedEntity);
					}
				}

				Assert.assertTrue(found, "Dependency [" + identifier
						+ "] was not found in " + property);
			}
		} else {
			Assert.assertNull(actual, property + "is not null");
		}
	}
}
