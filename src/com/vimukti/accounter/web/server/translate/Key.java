package com.vimukti.accounter.web.server.translate;

import java.io.Serializable;

public class Key implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;
	String key;

	private int usageCount;

	private int usageOrder;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the usageCount
	 */
	public int getUsageCount() {
		return usageCount;
	}

	/**
	 * @param usageCount
	 *            the usageCount to set
	 */
	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}

	/**
	 * @return the usageOrder
	 */
	public int getUsageOrder() {
		return usageOrder;
	}

	/**
	 * @param usageOrder
	 *            the usageOrder to set
	 */
	public void setUsageOrder(int usageOrder) {
		this.usageOrder = usageOrder;
	}

}
