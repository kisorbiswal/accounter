package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PayHeadDetails extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long employee;

	private long periodEndDate;

	private String transactionNumber;

	private double amount;

	public long getEmployee() {
		return employee;
	}

	public void setEmployee(long employee) {
		this.employee = employee;
	}

	public long getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(long periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
