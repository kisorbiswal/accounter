package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientDepreciationFixedAsset implements IAccounterCore {

	long id;
	String accountName;
	double amount;
	String accumulatedDepreciationAccount;




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
	public long getID(){
		return id;
	}

	@Override
	public void setID(long id){
		this.id=id;
	}

}
