package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to keep track of what views we have opened last. We will keep the size
 * of this limited;
 * 
 * @author rajesh
 * 
 */
public class HistoryList {
	private int size;
	List<HistoryItem> list = new ArrayList<HistoryItem>();

	public HistoryList(int size) {
		this.size = size;
	}

	public HistoryList() {
		this(10);
	}

	public void add(HistoryItem val) {
		if (list.contains(val)) {
			list.remove(val);
		}
		list.add(val);
		if (list.size() > size) {
			list.remove(0);
		}
	}

	public static class HistoryItem {
		HistoryItem(String token, ParentCanvas<?> view) {
			this.token = token;
			this.view = view;
		}

		String token;
		ParentCanvas<?> view;
	}

	public ParentCanvas<?> getView(String token) {
		for (HistoryItem item : list) {
			if (item.token.equals(token)) {
				return item.view;
			}
		}
		return null;
	}

	/**
	 * Return previous view
	 * 
	 * @param i
	 */
	public HistoryItem get(int index) {
		return list.get(list.size() - 1 - index);

	}
}
