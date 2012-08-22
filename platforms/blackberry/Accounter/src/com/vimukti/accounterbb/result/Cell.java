package com.vimukti.accounterbb.result;

public class Cell {
	private String title;
	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getTitle() {
		return title;
	}
}
