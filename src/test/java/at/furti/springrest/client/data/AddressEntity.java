package at.furti.springrest.client.data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.testng.Assert;

@Entity(name = "Address")
public class AddressEntity {

	@Id
	@Column(name = "addressid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer addressId;

	@Column(name = "street")
	private String street;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "cityId")
	private CityEntity city;

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public CityEntity getCity() {
		return city;
	}

	public void setCity(CityEntity city) {
		this.city = city;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AddressEntity)) {
			return false;
		}

		AddressEntity other = (AddressEntity) obj;

		Assert.assertEquals(other.getStreet(), getStreet(), "Street not equals");
		Assert.assertEquals(other.getCity(), getCity(), "City not equals");

		return true;
	}
}
