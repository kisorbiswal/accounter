package com.vimukti.accounter.mobile;

import java.util.ArrayList;

public class ResultList extends ArrayList<Record>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private boolean isSingleSelect;


	public boolean isSingleSelect() {
		return isSingleSelect;
	}


	public void setSingleSelect(boolean isSingleSelect) {
		this.isSingleSelect = isSingleSelect;
	}

	
	
}
