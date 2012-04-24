package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientPayRun extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long payableAccount;

	private List<ClientEmployeePaymentDetails> payEmployee;

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
	public List<ClientEmployeePaymentDetails> getPayEmployee() {
		return payEmployee;
	}

	/**
	 * @param payEmployee
	 *            the payEmployee to set
	 */
	public void setPayEmployee(List<ClientEmployeePaymentDetails> payEmployee) {
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
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAY_RUN;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}
}
