package com.vimukti.accounter.web.client.core;

public class ClientPayVATEntries implements IAccounterCore {
	
	private static final long serialVersionUID = 1L;

	long vatAgency;

	double amount;

	double balance;
	
	private long vatReturn;

	public long getVatAgency() {
		return vatAgency;
	}

	public void setVatAgency(long vatAgency) {
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
	public long getID(){
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setID(long id){
		// TODO Auto-generated method stub

	}

	public void setVatReturn(long vatReturn) {
		this.vatReturn = vatReturn;
	}

	public long getVatReturn() {
		return vatReturn;
	}

}
