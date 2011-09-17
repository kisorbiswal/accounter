package com.vimukti.accounter.mobile;

import java.util.ArrayList;

public class ResultList extends ArrayList<Record> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isMultiSelection;

	public boolean isMultiSelection() {
		return isMultiSelection;
	}

	public void setMultiSelection(boolean isMultiSelection) {
		this.isMultiSelection = isMultiSelection;
	}

}
