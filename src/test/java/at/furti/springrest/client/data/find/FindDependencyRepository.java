package at.furti.springrest.client.data.find;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.repository.annotation.RestResource;

public interface FindDependencyRepository extends
		CrudRepository<FindDependencyEntity, Integer> {

	@RestResource(exported = false)
	<S extends FindDependencyEntity> S save(S entity);

	@RestResource(exported = false)
	<S extends FindDependencyEntity> Iterable<S> save(Iterable<S> entities);

	void delete(Integer id);

	@RestResource(exported = false)
	void delete(FindDependencyEntity entity);

	@RestResource(exported = false)
	void delete(Iterable<? extends FindDependencyEntity> entities);

	@RestResource(exported = false)
	void deleteAll();
}
