package com.vimukti.accounter.mobile;

public class Cell {
	Object value;

	String name;

	Cell(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
