package com.vimukti.accounter.core;

import java.util.List;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class TAXReturn extends AbstractTAXReturn {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<TAXReturnEntry> taxEntries;

	private double salesTotal;

	private double purchaseTotal;

	/**
	 * @return the taxEntries
	 */
	public List<TAXReturnEntry> getTaxEntries() {
		return taxEntries;
	}

	/**
	 * @param taxEntries
	 *            the taxEntries to set
	 */
	public void setTaxEntries(List<TAXReturnEntry> taxEntries) {
		this.taxEntries = taxEntries;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return true;
	}

}
