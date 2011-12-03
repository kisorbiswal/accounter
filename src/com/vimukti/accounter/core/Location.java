package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

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

	@Override
	public int getObjType() {
		return IAccounterCore.LOCATION;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		
		w.put(messages.type(), messages.location()).gap();
		w.put(messages.name(), this.locationName);
		w.put(messages.companyName(), this.companyName).gap().gap();
		w.put(messages.address(), this.address.address1);
		w.put(messages.email(), this.email);
	}

}
