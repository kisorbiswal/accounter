package com.vimukti.accounter.core;

public enum ActivityType {
	LOGIN(0), LOGOUT(1), ADD(2), EDIT(3), DELETE(4);

	private int type;

	ActivityType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
