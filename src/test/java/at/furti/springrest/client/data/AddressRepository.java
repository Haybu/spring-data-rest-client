package at.furti.springrest.client.data;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Daniel
 *
 */
public interface AddressRepository extends
		CrudRepository<AddressEntity, Integer> {

}
