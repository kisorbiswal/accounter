package com.vimukti.accounter.mobile;

import java.util.ArrayList;

public class Record extends ArrayList<Cell> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private Object object;

	public Record(Object object) {
		this.object = object;
	}

	public void add(String name, Object value) {
		this.add(new Cell(name, value));
	}

	public String getCode() {
		return this.code;
	}

	public Object getObject() {
		return object;
	}
}
