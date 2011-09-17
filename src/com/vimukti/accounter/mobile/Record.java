package com.vimukti.accounter.mobile;

import java.util.ArrayList;

public class Record extends ArrayList<Cell> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Record(Object object) {
		// TODO Auto-generated constructor stub
	}

	public void add(String name, Object value) {
		this.add(new Cell(name, value));
	}
}
