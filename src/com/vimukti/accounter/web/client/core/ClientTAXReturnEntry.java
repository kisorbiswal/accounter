package com.vimukti.accounter.web.client.core;

public class ClientTAXReturnEntry implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	long transaction;

	int transactionType;

	long taxItem;

	long taxAgency;

	long taxRateCalculation;

	long taxAdjustment;

	int version;

	double netAmount;

	double grassAmount;

	double taxAmount;

	public long getTransaction() {
		return transaction;
	}

	public void setTransaction(long transaction) {
		this.transaction = transaction;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getTaxItem() {
		return taxItem;
	}

	public void setTaxItem(long taxItem) {
		this.taxItem = taxItem;
	}

	public long getTaxAgency() {
		return taxAgency;
	}

	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	@Override
	public String getDisplayName() {
		// /
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FILE_TAX_ENTRY;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientFileTAXEntry";
	}

	public void setTaxRateCalculation(long taxRateCalculation) {
		this.taxRateCalculation = taxRateCalculation;
	}

	public long getTaxRateCalculation() {
		return taxRateCalculation;
	}

	public void setTaxAdjustment(long taxAdjustment) {
		this.taxAdjustment = taxAdjustment;
	}

	public long getTaxAdjustment() {
		return taxAdjustment;
	}

	public ClientTAXReturnEntry clone() {
		ClientTAXReturnEntry paySalesTaxEntries = (ClientTAXReturnEntry) this
				.clone();
		return paySalesTaxEntries;

	}

	/**
	 * @return the transactionType
	 */
	public int getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the netAmount
	 */
	public double getNetAmount() {
		return netAmount;
	}

	/**
	 * @param netAmount
	 *            the netAmount to set
	 */
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return the grassAmount
	 */
	public double getGrassAmount() {
		return grassAmount;
	}

	/**
	 * @param grassAmount
	 *            the grassAmount to set
	 */
	public void setGrassAmount(double grassAmount) {
		this.grassAmount = grassAmount;
	}

	/**
	 * @return the taxAmount
	 */
	public double getTaxAmount() {
		return taxAmount;
	}

	/**
	 * @param taxAmount
	 *            the taxAmount to set
	 */
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

}
