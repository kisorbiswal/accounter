package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientTAXReturn extends ClientAbstractTAXReturn {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<ClientTAXReturnEntry> taxEntries;

	/**
	 * @return the taxEntries
	 */
	public List<ClientTAXReturnEntry> getTaxEntries() {
		return taxEntries;
	}

	/**
	 * @param taxEntries
	 *            the taxEntries to set
	 */
	public void setTaxEntries(List<ClientTAXReturnEntry> taxEntries) {
		this.taxEntries = taxEntries;
	}

}
