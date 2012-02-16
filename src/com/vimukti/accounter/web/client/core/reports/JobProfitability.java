package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class JobProfitability extends BaseReport implements Serializable,
		IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long transactionId;
	private int transactionType;
	private int customerId;
	private int jobId;
	private String name;
	private double costAmount;
	private double revenueAmount;

	public double getRevenueAmount() {
		return revenueAmount;
	}

	public void setRevenueAmount(double revenueAmount) {
		this.revenueAmount = revenueAmount;
	}

	public double getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(double costAmount) {
		this.costAmount = costAmount;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int cusotmerId) {
		this.customerId = cusotmerId;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

}
