package com.vimukti.accounter.admin.core;

import java.util.List;

import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.INamedObject;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Admin implements IAccounterServerCore,INamedObject {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private List<AdminUser> adminUsers;

	private String emailID;

	/**
	 * @return the id
	 */
	public void setID(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setAdminUsers(List<AdminUser> adminUsers) {
		this.adminUsers = adminUsers;
	}

	public List<AdminUser> getAdminUsers() {
		return adminUsers;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getEmailID() {
		return emailID;
	}

}
