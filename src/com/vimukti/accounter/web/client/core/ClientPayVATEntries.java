package com.vimukti.accounter.web.client.core;

public class ClientPayVATEntries implements IAccounterCore {
	
	private static final long serialVersionUID = 1L;

	String vatAgency;

	double amount;

	double balance;
	
	private String vatReturn;

	public String getVatAgency() {
		return vatAgency;
	}

	public void setVatAgency(String vatAgency) {
		this.vatAgency = vatAgency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStringID(String stringID) {
		// TODO Auto-generated method stub

	}

	public void setVatReturn(String vatReturn) {
		this.vatReturn = vatReturn;
	}

	public String getVatReturn() {
		return vatReturn;
	}

}
