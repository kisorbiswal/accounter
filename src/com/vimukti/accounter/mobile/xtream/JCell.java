package com.vimukti.accounter.mobile.xtream;

import com.vimukti.accounter.mobile.Cell;

public class JCell {
	String name;
	String value;

	public void set(Cell cell) {
		name = cell.getName();
		value = cell.toString();
	}

}
