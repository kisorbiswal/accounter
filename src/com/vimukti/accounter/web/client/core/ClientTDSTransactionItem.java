package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientTDSTransactionItem implements IAccounterCore {

	/**
	 * this class contains information of all the transaction happened using any
	 * tax.
	 */
	private static final long serialVersionUID = 1L;
	private int version;
	private long id;

	private boolean boxSelected;

	private long vendor;

	private double taxAmount;

	private double surchargeAmount;

	private double eduCess;

	private double totalAmount;

	private long transactionDate;

	private double tdsTotal;

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
		return Accounter.messages().clientTDSTransactionItem();
	}

	@Override
	public String getDisplayName() {
		return Accounter.messages().clientTDSTransactionItem();
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


	public long getVendorID() {
		return vendor;
	}

	public void setVendorID(long vendorID) {
		this.vendor = vendorID;
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

	public double getSurchargeAmount() {
		return surchargeAmount;
	}

	public void setSurchargeAmount(double surchargeAmount) {
		this.surchargeAmount = surchargeAmount;
	}

	public double getEduCess() {
		return eduCess;
	}

	public void setEduCess(double eduCess) {
		this.eduCess = eduCess;
	}

	public double getTdsTotal() {
		return tdsTotal;
	}

	public void setTdsTotal(double tdsTotal) {
		this.tdsTotal = tdsTotal;
	}

	public boolean isBoxSelected() {
		return boxSelected;
	}

	public void setBoxSelected(boolean boxSelected) {
		this.boxSelected = boxSelected;
	}

}
