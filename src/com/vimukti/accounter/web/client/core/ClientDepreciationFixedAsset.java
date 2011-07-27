package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.ui.Accounter;

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
		return null;
	}

	@Override
	public String getDisplayName() {
		return Accounter.getCompany().getDisplayName();
	}

	@Override
	public String getName() {
		return Accounter.getCompany().getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.COMPANY_PREFERENCES;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

}
