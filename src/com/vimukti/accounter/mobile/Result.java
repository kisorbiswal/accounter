package com.vimukti.accounter.mobile;

import java.util.ArrayList;
import java.util.List;

public class Result {
	List<Object> resultParts = new ArrayList<Object>();

	public void add(String message){
		this.resultParts.add(message);
	}
	public void add(ResultList list){
		this.resultParts.add(list);
	}
}
