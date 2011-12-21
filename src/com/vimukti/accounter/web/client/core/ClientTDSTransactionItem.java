package com.vimukti.accounter.web.client.core;

public class ClientTDSTransactionItem implements IAccounterCore {

	/**
	 * this class contains information of all the transaction happened using any
	 * tax.
	 */
	private static final long serialVersionUID = 1L;
	private int version;
	private long id;

	private long vendorID;

	private double taxAmount;

	private double totalAmount;

	private long transactionDate;

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return "ClientTDSTransactionItem";
	}

	@Override
	public String getDisplayName() {
		return "ClientTDSTransactionItem";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TDSCHALANTRANSACTIONITEM;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientTDSTransactionItem";
	}

	public long getVendorID() {
		return vendorID;
	}

	public void setVendorID(long vendorID) {
		this.vendorID = vendorID;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

}
