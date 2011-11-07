package com.vimukti.accounter.mobile.xtream;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.ResultList;

public class JResultList {
	List<JRecord> records = new ArrayList<JRecord>();
	boolean isMultiSelection;
	String name;

	public int addAll(ResultList object, int code) {
		name = object.getName();
		isMultiSelection = object.isMultiSelection();
		for (Record record : object) {
			record.setCode(code++);
			JRecord jRecord = new JRecord();
			jRecord.addAll(record);
			records.add(jRecord);
		}
		return code;
	}
}
