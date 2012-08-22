package com.vimukti.accounter.result;

public class InputType {

	public static final int INPUT_TYPE_NONE = 0;
	public static final int INPUT_TYPE_STRING = 1;
	public static final int INPUT_TYPE_NUMBER = 2;
	public static final int INPUT_TYPE_AMOUNT = 3;
	public static final int INPUT_TYPE_PASSWORD = 4;
	public static final int INPUT_TYPE_EMAIL = 5;
	public static final int INPUT_TYPE_PHONE = 6;
	public static final int INPUT_TYPE_URL = 7;
	public static final int INPUT_TYPE_DATE = 8;

	private int type;
	private String name;
	private String value;

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

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
