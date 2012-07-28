package at.furti.springrest.client.repository;

import org.springframework.util.Assert;

public class RepositoryEntry {

	private Class<?> repoClass;
	private String repoId;
	private String repoRel;
	private Class<?> type;

	public RepositoryEntry(Class<?> repoClass, String repoId, String repoRel, Class<?> type) {
		super();
		
		Assert.notNull(repoClass, "RepoClass must not be null");
		Assert.notNull(repoId, "RepoId must not be null");
		Assert.notNull(repoRel, "RepoRel must not be null");
		Assert.notNull(type, "Type must not be null");
		
		this.repoClass = repoClass;
		this.repoId = repoId;
		this.repoRel = repoRel;
		this.type = type;
	}

	public Class<?> getRepoClass() {
		return repoClass;
	}

	public void setRepoClass(Class<?> repoClass) {
		this.repoClass = repoClass;
	}

	public String getRepoId() {
		return repoId;
	}

	public void setRepoId(String repoId) {
		this.repoId = repoId;
	}

	public String getRepoRel() {
		return repoRel;
	}

	public void setRepoRel(String repoRel) {
		this.repoRel = repoRel;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RepositoryEntry)) {
			return false;
		}

		return getRepoClass().equals(((RepositoryEntry) obj).getClass());
	}

	@Override
	public int hashCode() {
		return repoClass.hashCode();
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}
}
