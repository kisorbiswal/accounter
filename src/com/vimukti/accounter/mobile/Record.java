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

	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @param i
	 */
	public void setCode(int i) {
		this.code = String.valueOf(i);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (!isEmpty()) {
			builder.append(get(0).toString());
			for (int i = 1; i < size(); i++) {
				Cell cell = get(i);
				builder.append(":\t\t").append(cell.toString());
			}
		}
		return "\n" + code + ". " + builder.toString();
	}
}
