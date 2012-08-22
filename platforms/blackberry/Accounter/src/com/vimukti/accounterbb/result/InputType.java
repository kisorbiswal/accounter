package com.vimukti.accounterbb.result;

public class InputType {
	private int type;
	private String name;
	private String value;

	public InputType(int type) {
		this.type = type;
	}

	public InputType(int type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
