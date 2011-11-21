package com.vimukti.accounter.mobile;

public class InputType {
	private int type;
	private String name;

	public InputType(int type) {
		this(type, "");
	}

	public InputType(int type, String name) {
		this.setType(type);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
