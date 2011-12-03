package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class PayeeStatementsList extends BaseReport implements IsSerializable,
		Serializable {
	private long transactionId;
	private int transactiontype;
	private String transactionNumber;
	private ClientFinanceDate transactionDate;
	private ClientFinanceDate dueDate;
	private double total = 0D;
	private double balance = 0D;
	private ClientAddress billingAddress;
	private String payeeName;
	private String salesPerson;
	private String shippingMethod;
	private String shippingTerms;
	private String paymentTerm;
	private long ageing;
	private int category;
	private long currency;
	private double currencyFactor;

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactiontype(int transactiontype) {
		this.transactiontype = transactiontype;
	}

	public int getTransactiontype() {
		return transactiontype;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setDueDate(ClientFinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	public ClientFinanceDate getDueDate() {
		return dueDate;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getTotal() {
		return total;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setBillingAddress(ClientAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public ClientAddress getBillingAddress() {
		return billingAddress;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingTerm(String shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	public String getShippingTerm() {
		return shippingTerms;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setAgeing(long ageing) {
		this.ageing = ageing;
	}

	public long getAgeing() {
		return ageing;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getCategory() {
		return category;
	}

	public void setCurrencyFactor(double currencyFactor) {
		this.currencyFactor = currencyFactor;
	}

	public double getCurrencyFactor() {
		return this.currencyFactor;
	}
	
	public void setCurrency(long currency) {
		this.currency = currency;
	}

	public long getCurrency() {
		return this.currency;
	}

}
