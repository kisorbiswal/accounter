package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class Location extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	private static final long serialVersionUID = 1L;
	private String locationName;
	private String title;
	private String companyName;

	private Address address;
	private String email;
	private String phone;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return false;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Override
	public String getName() {
		return locationName;
	}

	@Override
	public void setName(String name) {
		this.locationName = name;
	}

}
