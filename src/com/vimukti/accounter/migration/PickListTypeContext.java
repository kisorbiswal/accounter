package com.vimukti.accounter.migration;

import java.util.HashMap;
import java.util.Map;

public class PickListTypeContext {

	private Map<String, Long> picklistObjs = new HashMap<String, Long>();

	public boolean isEmpty() {
		return picklistObjs.isEmpty();
	}

	public void put(String indentity, String instanceIdentity, Long id) {
		picklistObjs.put(indentity + instanceIdentity, id);
	}

	public Long get(String objName, String value) {
		return picklistObjs.get(objName + value);
	}
}
