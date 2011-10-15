package com.vimukti.accounter.mobile;

import java.util.ArrayList;
import java.util.List;

public class Result {
	List<Object> resultParts = new ArrayList<Object>();

	/**
	 * Creates new Instance
	 */
	public Result() {
	}

	/**
	 * Creates new Instance
	 */
	public Result(String message) {
		resultParts.add(message);
	}

	public void add(String message) {
		this.resultParts.add(message);
	}

	public void add(ResultList list) {
		this.resultParts.add(list);
	}

	public void add(CommandList list) {
		this.resultParts.add(list);
	}

	/**
	 * Returns the ResiltParts
	 */
	public List<Object> getResultParts() {
		return resultParts;
	}

	public void addAll(List<Object> resultParts2) {
		resultParts.addAll(resultParts2);
	}

}
