package at.furti.springrest.client.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="Person")
public class PersonEntity {

	@Id
	@Column(name="personId")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer personId;
	
	@Column(name="vorname")
	private String firstName;
	
	@Column(name="nachname")
	private String lastName;
	
	@Column(name="geburtsdatum")
	private String birthDate;

	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
