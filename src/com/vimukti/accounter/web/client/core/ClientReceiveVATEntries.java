package com.vimukti.accounter.web.client.core;

public class ClientReceiveVATEntries implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long vatAgency;

	double amount;

	double balance;

	private long vatReturn;

	private long id;

	private int version;

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
		// its not using any where
		return null;
	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.RECEIVEVAT;
	}

	@Override
	public long getID() {

		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public void setVatReturn(long vatReturn) {
		this.vatReturn = vatReturn;
	}

	public long getVatReturn() {
		return vatReturn;
	}

	public ClientReceiveVATEntries clone() {
		ClientReceiveVATEntries receiveVATEntries = (ClientReceiveVATEntries) this
				.clone();
		return receiveVATEntries;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}

}