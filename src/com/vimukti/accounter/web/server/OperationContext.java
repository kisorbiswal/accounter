package com.vimukti.accounter.web.server;

import java.sql.Timestamp;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * @author Prasanna Kumar G
 * 
 */
public class OperationContext {

	/** User who is Doing Operation */
	protected long userID;

	/** Operation Data */
	protected IAccounterCore data;
	
	protected AccounterCoreType coreType;

	private String arg2;
	private String arg1;

	private Timestamp date;

	private ClientCompanyPreferences companyPreferences;

	private ClientCompany company;

	private long newStartDate;

	/**
	 * Creates new Instance
	 */
	public OperationContext(IAccounterCore data, long userID) {
		this.userID = userID;
		this.data = data;
	}

	/**
	 * Creates new Instance
	 */
	public OperationContext(IAccounterCore data, long userID, String arg1,
			String arg2) {
		this(data, userID);
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	public OperationContext(AccounterCoreType type, long id) {
		this.coreType = type;
		this.userID = id;
	}

	public OperationContext(ClientCompanyPreferences preferences) {
		this.companyPreferences = preferences;
	}

	public OperationContext(ClientCompany clientCompany) {
		this.company = clientCompany;
	}

	public OperationContext(long newStartDate) {
		this.newStartDate = newStartDate;
	}

	/**
	 * @return the user
	 */
	public long getUserID() {
		return userID;
	}

	/**
	 * @return the data
	 */
	public IAccounterCore getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(IAccounterCore data) {
		this.data = data;
	}

	/**
	 * @return
	 */
	public Timestamp getDate() {
		return this.date;
	}

	/**
	 * @return
	 */
	public String getArg2() {
		return this.arg2;
	}

	/**
	 * @return
	 */
	public String getArg1() {
		return this.arg1;
	}

	/**
	 * @param arg2
	 *            the arg2 to set
	 */
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}

	/**
	 * @param arg1
	 *            the arg1 to set
	 */
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}
}
