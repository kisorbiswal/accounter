/**
 * 
 */
package com.vimukti.accounter.web.client.core;

/**
 * @author vimukti5
 * 
 */
@SuppressWarnings("serial")
public class ClientTAXAdjustment extends ClientTransaction implements
		IAccounterCore {

	// ClientJournalEntry journalEntry;

	long adjustmentDate;

	boolean increaseVATLine;

	boolean isFiled;

	long adjustmentAccount;

	String vatAccount;

	long taxItem;

	String journalEntry;

	long taxAgency;

	/**
	 * @return the journalEntry
	 */
	public String getJournalEntry() {
		return journalEntry;
	}

	/**
	 * @param journalEntry
	 *            the journalEntry to set
	 */
	public void setJournalEntry(String journalEntry) {
		this.journalEntry = journalEntry;
	}

	/**
	 * @return the adjustmentDate
	 */
	public Long getAdjustmentDate() {
		return adjustmentDate;
	}

	/**
	 * @param adjustmentDate
	 *            the adjustmentDate to set
	 */
	public void setAdjustmentDate(Long adjustmentDate) {
		this.adjustmentDate = adjustmentDate;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the increaseVATLine
	 */
	public boolean isIncreaseVATLine() {
		return increaseVATLine;
	}

	/**
	 * @param increaseVATLine
	 *            the increaseVATLine to set
	 */
	public void setIncreaseVATLine(boolean increaseVATLine) {
		this.increaseVATLine = increaseVATLine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getClientClassSimpleName
	 * ()
	 */
	@Override
	public String getClientClassSimpleName() {
		return "ClientTAXAdjustment";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getName()
	 */
	@Override
	public String getName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TAXADJUSTMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getID()
	 */
	@Override
	public long getID() {

		return this.id;
	}

	/**
	 * @return the adjustmentAccount
	 */
	public long getAdjustmentAccount() {
		return adjustmentAccount;
	}

	/**
	 * @param adjustmentAccount
	 *            the adjustmentAccount to set
	 */
	public void setAdjustmentAccount(long adjustmentAccount) {
		this.adjustmentAccount = adjustmentAccount;
	}

	/**
	 * @return the vatAccount
	 */
	public String getVatAccount() {
		return vatAccount;
	}

	/**
	 * @param vatAccount
	 *            the vatAccount to set
	 */
	public void setVatAccount(String vatAccount) {
		this.vatAccount = vatAccount;
	}

	/**
	 * @return the vatItem
	 */
	public long getTaxItem() {
		return taxItem;
	}

	/**
	 * @param taxItem
	 *            the vatItem to set
	 */
	public void setTaxItem(long taxItem) {
		this.taxItem = taxItem;
	}

	/**
	 * @param adjustmentDate
	 *            the adjustmentDate to set
	 */
	public void setAdjustmentDate(long adjustmentDate) {
		this.adjustmentDate = adjustmentDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#setID(java
	 * .lang.String)
	 */
	@Override
	public void setID(long id) {
		this.id = id;

	}

	public long getTaxAgency() {
		return taxAgency;
	}

	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}


	public ClientTAXAdjustment clone() {
		return null;
	}

}
