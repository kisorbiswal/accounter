package com.vimukti.accounter.result;

import java.util.ArrayList;
import java.util.List;

public class ResultList {
	private List<Record> records = new ArrayList<Record>();
	private boolean isMultiSelection;
	private String title;

	public void setMultiSelection(boolean isMultiSelection) {
		this.isMultiSelection = isMultiSelection;
	}

	public boolean isMultiSelection() {
		return isMultiSelection;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	public List<Record> getRecords() {
		return records;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
