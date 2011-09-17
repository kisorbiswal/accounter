package com.vimukti.accounter.mobile;

import java.util.ArrayList;

public class Record extends ArrayList<Cell>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void add(String name,Object value){
		this.add(new Cell(name, value));
	}
}
