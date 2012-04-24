package com.vimukti.accounter.web.client.core;

public class ClientEmployeePayHeadComponent implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientPayHead payHead;

	private double rate;

	private long id;

	private String employee;

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
		return employee;
	}

	@Override
	public String getDisplayName() {
		return employee;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE_PAYHEAD_COMPONENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public ClientPayHead getPayHead() {
		return payHead;
	}

	public void setPayHead(ClientPayHead payHead) {
		this.payHead = payHead;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}
}
