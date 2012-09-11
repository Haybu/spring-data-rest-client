package at.furti.springrest.client.data.find;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="NoRepository")
public class NoRepositoryEntity {

	@Id
	@Column
	private Integer id;
	
	@Column
	private String stringProperty;

	public String getStringProperty() {
		return stringProperty;
	}

	public void setStringProperty(String stringProperty) {
		this.stringProperty = stringProperty;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
