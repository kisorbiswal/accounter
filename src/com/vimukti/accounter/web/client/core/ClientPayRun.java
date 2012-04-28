package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientPayRun implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long payableAccount;

	private List<ClientPayEmployee> payEmployee;

	private long payPeriodStartDate;

	private long payPeriodEndDate;

	/**
	 * @return the payableAccount
	 */
	public long getPayableAccount() {
		return payableAccount;
	}

	/**
	 * @param payableAccount
	 *            the payableAccount to set
	 */
	public void setPayableAccount(long payableAccount) {
		this.payableAccount = payableAccount;
	}

	/**
	 * @return the payEmployee
	 */
	public List<ClientPayEmployee> getPayEmployee() {
		return payEmployee;
	}

	/**
	 * @param payEmployee
	 *            the payEmployee to set
	 */
	public void setPayEmployee(List<ClientPayEmployee> payEmployee) {
		this.payEmployee = payEmployee;
	}

	/**
	 * @return the payPeriodStartDate
	 */
	public long getPayPeriodStartDate() {
		return payPeriodStartDate;
	}

	/**
	 * @param payPeriodStartDate
	 *            the payPeriodStartDate to set
	 */
	public void setPayPeriodStartDate(long payPeriodStartDate) {
		this.payPeriodStartDate = payPeriodStartDate;
	}

	/**
	 * @return the payPeriodEndDate
	 */
	public long getPayPeriodEndDate() {
		return payPeriodEndDate;
	}

	/**
	 * @param payPeriodEndDate
	 *            the payPeriodEndDate to set
	 */
	public void setPayPeriodEndDate(long payPeriodEndDate) {
		this.payPeriodEndDate = payPeriodEndDate;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
