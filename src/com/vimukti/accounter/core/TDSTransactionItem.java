package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class TDSTransactionItem extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	private long vendor;

	private double taxAmount;

	private double totalAmount;

	private double surchargeAmount;

	private double eduCess;

	private long transactionDate;

	private long transactionID;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "TDSTransactionItem";
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public int getObjType() {
		return IAccounterCore.TDSTRANSACTIONITEM;
	}

	public long getVendor() {
		return vendor;
	}

	public void setVendor(long vendor) {
		this.vendor = vendor;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

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

	/**
	 * @return the transactionID
	 */
	public long getTransactionID() {
		return transactionID;
	}

	/**
	 * @param transactionID
	 *            the transactionID to set
	 */
	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

}
