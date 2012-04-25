package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PaySlipDetail extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long payheadId;
	private String name;
	private boolean isEarning;
	private int periodType;
	private double amount;

	public long getPayheadId() {
		return payheadId;
	}

	public void setPayheadId(long payheadId) {
		this.payheadId = payheadId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEarning() {
		return isEarning;
	}

	public void setEarning(boolean isEarning) {
		this.isEarning = isEarning;
	}

	public int getPeriodType() {
		return periodType;
	}

	public void setPeriodType(int periodType) {
		this.periodType = periodType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
