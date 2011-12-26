package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;

public class YearOverYearPortletData implements IsSerializable {
	private String name;
	private double amount;
	private long currency;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

}
