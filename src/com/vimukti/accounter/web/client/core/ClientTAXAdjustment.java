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

	String adjustmentAccount;

	String vatAccount;

	String taxItem;

	String journalEntry;

	String taxAgency;

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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
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
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getStringID()
	 */
	@Override
	public String getStringID() {

		return this.stringID;
	}

	/**
	 * @return the adjustmentAccount
	 */
	public String getAdjustmentAccount() {
		return adjustmentAccount;
	}

	/**
	 * @param adjustmentAccount
	 *            the adjustmentAccount to set
	 */
	public void setAdjustmentAccount(String adjustmentAccount) {
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
	public String getTaxItem() {
		return taxItem;
	}

	/**
	 * @param taxItem
	 *            the vatItem to set
	 */
	public void setTaxItem(String taxItem) {
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
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#setStringID(java
	 * .lang.String)
	 */
	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	public String getTaxAgency() {
		return taxAgency;
	}

	public void setTaxAgency(String taxAgency) {
		this.taxAgency = taxAgency;
	}

}
