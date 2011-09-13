package com.vimukti.accounter.admin.core;

import com.vimukti.accounter.admin.client.ClientAdminUser;
import com.vimukti.accounter.core.CreatableObject;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.INamedObject;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class AdminUser extends CreatableObject implements IAccounterServerCore ,INamedObject{

	public AdminUser() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5387196197710854416L;

	private String name;

	private String emailId;

	private String password;

	private int typeOfUser;

	private String status;

	boolean isPermissionsGiven;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTypeOfUser() {
		return typeOfUser;
	}

	public void setTypeOfUser(int typeOfUser) {
		this.typeOfUser = typeOfUser;
	}

	public boolean isPermission() {
		return isPermissionsGiven;
	}

	public void setPermission(boolean permission) {
		this.isPermissionsGiven = permission;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public AdminUser(ClientAdminUser clientuser) {
		super.id = clientuser.getID();
		this.name = clientuser.getName();
		this.emailId = clientuser.getEmailId();
		this.password = clientuser.getPassword();
		this.status = clientuser.getStatus();

	}
}
