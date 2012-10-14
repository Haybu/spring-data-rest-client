package at.furti.springrest.client.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import at.furti.springrest.client.util.RepositoryUtils;

public class RepositoryConfig {

	private List<RepositoryEntry> repositories;

	public void addRepository(Class<?> repoClass) {
		if (repoClass == null) {
			return;
		}

		if (repositories == null) {
			repositories = new ArrayList<RepositoryEntry>();
		}

		repositories.add(createEntry(repoClass));
	}

	public List<RepositoryEntry> getRepositories() {
		return repositories;
	}

	/**
	 * @param repoClass
	 * @return
	 */
	private RepositoryEntry createEntry(Class<?> repoClass) {
		return new RepositoryEntry(repoClass,
				RepositoryUtils.getRepositoryId(repoClass),
				RepositoryUtils.getRepositoryRel(repoClass),
				RepositoryUtils.extractEntryType(repoClass),
				RepositoryUtils.extractIdType(repoClass));
	}

	/**
	 * @return
	 */
	public boolean hasRepositories() {
		return !CollectionUtils.isEmpty(repositories);
	}
}