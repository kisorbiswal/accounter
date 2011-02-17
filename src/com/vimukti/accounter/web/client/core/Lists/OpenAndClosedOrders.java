package com.vimukti.accounter.web.client.core.Lists;


import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class OpenAndClosedOrders extends BaseReport implements IsSerializable {

	String transactionID;

	long id;

	int transactionType;

	String vendorOrCustomerName;

	ClientFinanceDate transactionDate;

	String description;

	double quantity;

	double amount;

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getVendorOrCustomerName() {
		return vendorOrCustomerName;
	}

	public void setVendorOrCustomerName(String vendorOrCustomerName) {
		this.vendorOrCustomerName = vendorOrCustomerName;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
