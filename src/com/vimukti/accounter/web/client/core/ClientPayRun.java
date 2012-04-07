package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientPayRun implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientAccount payableAccount;

	private List<ClientPayEmployee> payEmployee;

	private ClientFinanceDate payPeriodStartDate;

	private ClientFinanceDate payPeriodEndDate;

	public ClientAccount getPayableAccount() {
		return payableAccount;
	}

	public void setPayableAccount(ClientAccount payableAccount) {
		this.payableAccount = payableAccount;
	}

	public List<ClientPayEmployee> getPayEmployee() {
		return payEmployee;
	}

	public void setPayEmployee(List<ClientPayEmployee> payEmployee) {
		this.payEmployee = payEmployee;
	}

	public ClientFinanceDate getPayPeriodStartDate() {
		return payPeriodStartDate;
	}

	public void setPayPeriodStartDate(ClientFinanceDate payPeriodStartDate) {
		this.payPeriodStartDate = payPeriodStartDate;
	}

	public ClientFinanceDate getPayPeriodEndDate() {
		return payPeriodEndDate;
	}

	public void setPayPeriodEndDate(ClientFinanceDate payPeriodEndDate) {
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
