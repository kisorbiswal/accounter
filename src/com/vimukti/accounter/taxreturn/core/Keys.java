package com.vimukti.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

public class Keys {
	/**
	 * 0..∞ type+
	 */
	private List<Key> keys = new ArrayList<Key>();

	public Keys() {
		getKeys().add(new Key());
	}

	public List<Key> getKeys() {
		return keys;
	}

	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}

}
