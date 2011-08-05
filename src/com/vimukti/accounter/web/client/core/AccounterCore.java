package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class AccounterCore implements IAccounterCore {
	public long id;
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
	public long getID() {

		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "AccounterCore";
	}

	public Object clone() {
		return null;

	}

}
