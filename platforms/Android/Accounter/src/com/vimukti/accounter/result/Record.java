package com.vimukti.accounter.result;

import java.util.ArrayList;
import java.util.List;

public class Record {
	private String code;
	private List<Cell> cells = new ArrayList<Cell>();

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
