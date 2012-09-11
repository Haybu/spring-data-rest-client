package at.furti.springrest.client.data.find;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "Find")
public class FindEntity {

	@Id
	@Column
	private Integer id;

	@Column
	private String stringProperty;

	@Column
	private Integer intProperty;

	@Column
	private Date dateProperty;

	@Column
	private Boolean booleanProperty;

	@Column
	private Long longProperty;

	@Column
	private Double doubleProperty;

	@ManyToOne
	@JoinColumn(name = "dependencyId", referencedColumnName = "id")
	private FindDependencyEntity findDependency;

	@ManyToOne
	@JoinColumn(name = "noRepoId", referencedColumnName = "id")
	private NoRepositoryEntity noRepo;

	@OneToMany
	@JoinTable(name = "find_finddependency", joinColumns = @JoinColumn(name = "findId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "finddependencyId", referencedColumnName = "id"))
	private Set<FindDependencyEntity> set;

	@OneToMany
	@JoinTable(name = "find_finddependency", joinColumns = @JoinColumn(name = "findId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "finddependencyId", referencedColumnName = "id"))
	private List<FindDependencyEntity> list;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStringProperty() {
		return stringProperty;
	}

	public void setStringProperty(String stringProperty) {
		this.stringProperty = stringProperty;
	}

	public Integer getIntProperty() {
		return intProperty;
	}

	public void setIntProperty(Integer intProperty) {
		this.intProperty = intProperty;
	}

	public Date getDateProperty() {
		return dateProperty;
	}

	public void setDateProperty(Date dateProperty) {
		this.dateProperty = dateProperty;
	}

	public Boolean getBooleanProperty() {
		return booleanProperty;
	}

	public void setBooleanProperty(Boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	public Long getLongProperty() {
		return longProperty;
	}

	public void setLongProperty(Long longProperty) {
		this.longProperty = longProperty;
	}

	public Double getDoubleProperty() {
		return doubleProperty;
	}

	public void setDoubleProperty(Double doubleProperty) {
		this.doubleProperty = doubleProperty;
	}

	public FindDependencyEntity getFindDependency() {
		return findDependency;
	}

	public void setFindDependency(FindDependencyEntity findDependency) {
		this.findDependency = findDependency;
	}

	public NoRepositoryEntity getNoRepo() {
		return noRepo;
	}

	public void setNoRepo(NoRepositoryEntity noRepo) {
		this.noRepo = noRepo;
	}

	public Set<FindDependencyEntity> getSet() {
		return set;
	}

	public void setSet(Set<FindDependencyEntity> set) {
		this.set = set;
	}

	public List<FindDependencyEntity> getList() {
		return list;
	}

	public void setList(List<FindDependencyEntity> list) {
		this.list = list;
	}
}
