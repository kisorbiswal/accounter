package com.vimukti.accounterbb.result;

import java.util.Vector;

public class ResultList {
	private Vector records = new Vector();
	private boolean isMultiSelection;
	private String title;

	

	public void setMultiSelection(boolean isMultiSelection) {
		this.isMultiSelection = isMultiSelection;
	}

	public boolean isMultiSelection() {
		return isMultiSelection;
	}

	public void setRecords(Vector records) {
		this.records = records;
	}

	public Vector getRecords() {
		return records;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	
}
