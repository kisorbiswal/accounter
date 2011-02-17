package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientDepreciationFixedAsset implements IAccounterCore {

	long id;
	String stringID;
	String accountName;
	double amount;
	String accumulatedDepreciationAccount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStringId() {
		return stringID;
	}

	public void setStringId(String stringId) {
		this.stringID = stringId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAccumulatedDepreciationAccount() {
		return accumulatedDepreciationAccount;
	}

	public void setAccumulatedDepreciationAccount(
			String accumulatedDepreciationAccount) {
		this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
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
		return stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

}
