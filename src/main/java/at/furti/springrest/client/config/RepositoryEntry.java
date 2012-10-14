package at.furti.springrest.client.config;

import org.springframework.util.Assert;

public class RepositoryEntry {
	private Class<?> repoClass;
	private Class<?> entityType;
	private Class<?> idType;
	private String repoId;
	private String repoRel;

	public RepositoryEntry(Class<?> repoClass, String repoId, String repoRel,
			Class<?> entityType, Class<?> idType) {
		super();

		Assert.notNull(repoClass, "RepoClass must not be null");
		Assert.notNull(repoId, "RepoId must not be null");
		Assert.notNull(repoRel, "RepoRel must not be null");
		Assert.notNull(entityType, "EntityType must not be null");
		Assert.notNull(idType, "IdType must not be null");

		this.repoClass = repoClass;
		this.repoId = repoId;
		this.repoRel = repoRel;
		this.entityType = entityType;
		this.idType = idType;
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
		if (!(obj instanceof RepositoryConfig)) {
			return false;
		}

		return getRepoClass().equals(((RepositoryConfig) obj).getClass());
	}

	@Override
	public int hashCode() {
		return repoClass.hashCode();
	}

	public Class<?> getEntityType() {
		return entityType;
	}

	public void setEntityType(Class<?> entityType) {
		this.entityType = entityType;
	}

	public Class<?> getIdType() {
		return idType;
	}

	public void setIdType(Class<?> idType) {
		this.idType = idType;
	}
}
