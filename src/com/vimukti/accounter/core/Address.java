package com.vimukti.accounter.core;

import java.util.LinkedHashMap;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class Address implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6945021735662392287L;
	public static final int TYPE_BUSINESS = 1;
	public static final int TYPE_BILL_TO = 2;
	public static final int TYPE_SHIP_TO = 3;
	public static final int TYPE_WAREHOUSE = 4;
	public static final int TYPE_LEGAL = 5;
	public static final int TYPE_POSTAL = 6;
	public static final int TYPE_OTHER = 7;
	public static final int TYPE_HOME = 8;
	public static final int TYPE_COMPANY = 9;
	public static final int TYPE_COMPANY_REGISTRATION = 10;

	int type = 0;
	String address1 = "";
	String street = "";
	String city = "";
	String stateOrProvinence = "";
	String zipOrPostalCode = "";
	String countryOrRegion = "";
	boolean isSelected = false;
	public long id;
	private int version;

	public Address() {
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	// public void setType(int type) {
	// this.type = type;
	// }
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	// public void setStreet(String street) {
	// this.street = street;
	// }
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	// public void setCity(String city) {
	// this.city = city;
	// }
	/**
	 * @return the stateOrProvinence
	 */
	public String getStateOrProvinence() {
		return stateOrProvinence;
	}

	/**
	 * @param stateOrProvinence
	 *            the stateOrProvinence to set
	 */
	// public void setStateOrProvinence(String stateOrProvinence) {
	// this.stateOrProvinence = stateOrProvinence;
	// }
	/**
	 * @return the zipOrPostalCode
	 */
	public String getZipOrPostalCode() {
		return zipOrPostalCode;
	}

	/**
	 * @param zipOrPostalCode
	 *            the zipOrPostalCode to set
	 */
	// public void setZipOrPostalCode(String zipOrPostalCode) {
	// this.zipOrPostalCode = zipOrPostalCode;
	// }
	/**
	 * @return the countryOrRegion
	 */
	public String getCountryOrRegion() {
		return countryOrRegion;
	}

	/**
	 * @param countryOrRegion
	 *            the countryOrRegion to set
	 */
	// public void setCountryOrRegion(String countryOrRegion) {
	// this.countryOrRegion = countryOrRegion;
	// }
	/**
	 * @return the isSelected
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	// public void setIsSelected(boolean isSelected) {
	// this.isSelected = isSelected;
	// }
	public LinkedHashMap<String, String> getAddressTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(TYPE_BUSINESS + "", "Business");
		valueMap.put(TYPE_BILL_TO + "", "Bill to");
		valueMap.put(TYPE_SHIP_TO + "", "Ship to");
		valueMap.put(TYPE_WAREHOUSE + "", "Warehouse");
		valueMap.put(TYPE_LEGAL + "", "Legal");
		valueMap.put(TYPE_POSTAL + "", "Postal");
		valueMap.put(TYPE_HOME + "", "Home");
		valueMap.put(TYPE_OTHER + "", "Other");
		return valueMap;

	}

	public boolean equals(Address address) {

		if (this.type == address.type && this.isSelected == address.isSelected
				&& this.street.equals(address.street)
				&& this.city.equals(address.city)
				&& this.stateOrProvinence.equals(address.stateOrProvinence)
				&& this.zipOrPostalCode.equals(address.zipOrPostalCode)
				&& this.countryOrRegion.equals(address.countryOrRegion))
			return true;
		return false;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountryOrRegion(String countryOrRegion) {
		this.countryOrRegion = countryOrRegion;

	}

	public void setStateOrProvinence(String stateOrProvinence) {
		this.stateOrProvinence = stateOrProvinence;
	}

	public void setStreet(String street2) {
		this.street = street2;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}
}
