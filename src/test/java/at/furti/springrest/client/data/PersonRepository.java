package at.furti.springrest.client.data;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<PersonEntity, Integer> {

//	/**
//	 * @return
//	 */
//	@Query(
//			"SELECT p " +
//			"FROM at.furti.jrk.manager.data.person.PersonEntity p " +
//			"LEFT OUTER JOIN FETCH p.state")
//	public List<PersonEntity> findPersonsOverview();
//	
//	/**
//	 * @param personId
//	 * @return
//	 */
//	@Query(
//			"SELECT p " +
//			"FROM at.furti.jrk.manager.data.person.PersonEntity p " +
//			"LEFT OUTER JOIN FETCH p.state " +
//			"WHERE p.personId = :personId")
//	public PersonEntity findPersonOverview(
//			@Param("personId") Integer personId);
//	
//	@Query(
//			"SELECT p " +
//			"FROM at.furti.jrk.manager.data.person.PersonEntity p " +
//			"LEFT JOIN FETCH p.address " +
//			"WHERE p.personId = :personId")
//	public PersonEntity findAddressForPerson(
//			@Param("personId") Integer personId);
//	
//	/**
//	 * @param name
//	 * @return
//	 */
//	@Query(
//			"SELECT p " +
//			"FROM at.furti.jrk.manager.data.person.PersonEntity p " +
//			"WHERE p.username = :username")
//	public PersonEntity findPersonForLogin(
//			@Param("username") String name);
}
