package at.furti.springrest.client.data;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.time.DateUtils;
import org.testng.Assert;

@Entity(name = "Person")
public class PersonEntity {

	@Id
	@Column(name = "personId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer personId;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	@Column(name = "dayofbirth")
	private Date birthDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "addressId")
	private AddressEntity address;

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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public AddressEntity getAddress() {
		return address;
	}

	public void setAddress(AddressEntity address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PersonEntity)) {
			return false;
		}

		PersonEntity other = (PersonEntity) obj;

		Assert.assertEquals(other.getFirstName(), getFirstName(),
				"Firstname not equals");
		Assert.assertEquals(other.getLastName(), getLastName(),
				"Lastname not equals");
		Assert.assertTrue(
				DateUtils.isSameDay(other.getBirthDate(), getBirthDate()),
				"Birthdate not equals");
		Assert.assertEquals(other.getAddress(), getAddress(),
				"Address not equals");

		return true;
	}
}
