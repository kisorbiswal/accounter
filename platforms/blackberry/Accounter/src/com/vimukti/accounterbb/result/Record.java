package com.vimukti.accounterbb.result;

import java.util.Vector;

public class Record {
	private String code;
	private Vector cells = new Vector();

	public void setCells(Vector cells) {
		this.cells = cells;
	}

	public Vector getCells() {
		return cells;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
