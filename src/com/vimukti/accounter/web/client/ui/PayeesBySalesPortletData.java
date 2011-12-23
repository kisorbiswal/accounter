package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PayeesBySalesPortletData implements IsSerializable {
	private String name;
	private int noOfTrans;
	private double amount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoOfTrans() {
		return noOfTrans;
	}

	public void setNoOfTrans(int noOfTrans) {
		this.noOfTrans = noOfTrans;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
