package at.furti.springrest.client.data.find;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="FindDependency")
public class FindDependencyEntity {

	@Id
	@Column
	private Integer id;
	
	@Column
	private String stringProperty;

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
}
