package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class AccounterCore implements IAccounterCore {
	public String stringID;
	private AccounterCoreType type;

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return this.type;
	}

	public void setObjectType(AccounterCoreType type) {
		this.type = type;
	}

	@Override
	public String getStringID() {

		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {

		return "AccounterCore";
	}

}
