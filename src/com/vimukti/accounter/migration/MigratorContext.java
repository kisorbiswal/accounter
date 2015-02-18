package com.vimukti.accounter.migration;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MigratorContext {

	private Map<String, Long> ids = new HashMap<String, Long>();

	public void put(String name, Map<Long, Long> migrateAccounts) {
		for (Entry<Long, Long> oldId : migrateAccounts.entrySet()) {
			ids.put(name + oldId.getKey(), oldId.getValue());
		}
	}

	public Long get(String name, long id) {
		return ids.get(name + id);
	}
}
