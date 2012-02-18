package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class JobEstimatesVsActualsSummary extends BaseReport implements
		Serializable, IsSerializable {

	private static final long serialVersionUID = 1L;

	private String customerName;
	private String jobName;
	private int customerId;
	private int jobId;
	private double actualCost;
	private double actualRevenue;
	private double estimatedCost;
	private double estimatedRevenue;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public double getActualCost() {
		return actualCost;
	}

	public void setActualCost(double actualCost) {
		this.actualCost = actualCost;
	}

	public double getActualRevenue() {
		return actualRevenue;
	}

	public void setActualRevenue(double actualRevenue) {
		this.actualRevenue = actualRevenue;
	}

	public double getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public double getEstimatedRevenue() {
		return estimatedRevenue;
	}

	public void setEstimatedRevenue(double estimatedRevenue) {
		this.estimatedRevenue = estimatedRevenue;
	}

}
