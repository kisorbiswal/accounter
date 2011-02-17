package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class AccounterCommand implements IAccounterCore {

	public static final int CREATION_SUCCESS = 777;

	public static final int UPDATION_SUCCESS = 888;

	public static final int DELETION_SUCCESS = 999;

	public int command;

	IAccounterCore data;

	public IAccounterCore getData() {
		return data;
	}

	public void setData(IAccounterCore data) {
		this.data = data;
	}

	String stringID;
	private AccounterCoreType type;

	@Override
	public String getClientClassSimpleName() {
		return null;
	}

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

	/**
	 * @return the command
	 */
	public int getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(int command) {
		this.command = command;
	}

}
