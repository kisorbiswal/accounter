package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class Location implements IAccounterServerCore, INamedObject {

	private static final long serialVersionUID = 1L;
	private String locationName;
	long id;
	private String title;
	private String companyName;
	private int version;

	// private Address address;
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

	// public Address getAddress() {
	// return address;
	// }
	//
	// public void setAddress(Address address) {
	// this.address = address;
	// }

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
	public long getID() {
		return this.id;
	}

	public void setId(long ID) {
		this.id = ID;
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
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
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
