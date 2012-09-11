package at.furti.springrest.client.data.find;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.repository.annotation.RestResource;

public interface FindRepository extends CrudRepository<FindEntity, Integer> {

	@RestResource(exported = false)
	<S extends FindEntity> S save(S entity);

	@RestResource(exported = false)
	<S extends FindEntity> Iterable<S> save(Iterable<S> entities);

	void delete(Integer id);

	@RestResource(exported = false)
	void delete(FindEntity entity);

	@RestResource(exported = false)
	void delete(Iterable<? extends FindEntity> entities);

	@RestResource(exported = false)
	void deleteAll();
}
