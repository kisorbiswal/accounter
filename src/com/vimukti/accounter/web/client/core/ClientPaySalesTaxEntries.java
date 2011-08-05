package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientPaySalesTaxEntries implements IAccounterCore {

	long id;

	long transaction;

	long taxItem;

	long taxAgency;

	long taxRateCalculation;

	long taxAdjustment;

	double amount;

	double balance;
	long transactionDate;
	int version;
	int status = 0;
	boolean isVoid;

	public long getTransactionDate() {
		return transactionDate;
	}

	public long getTransaction() {
		return transaction;
	}

	public void setTransaction(long transaction) {
		this.transaction = transaction;
	}

	public int getVersion() {
		return version;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getTaxItem() {
		return taxItem;
	}

	public void setTaxItem(long taxItem) {
		this.taxItem = taxItem;
	}

	public long getTaxAgency() {
		return taxAgency;
	}

	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String getDisplayName() {
		// /
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.PAYSALESTAX_ENTRIES;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientPaySalesTaxEntries";
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public void setTaxRateCalculation(long taxRateCalculation) {
		this.taxRateCalculation = taxRateCalculation;
	}

	public long getTaxRateCalculation() {
		return taxRateCalculation;
	}

	public void setTaxAdjustment(long taxAdjustment) {
		this.taxAdjustment = taxAdjustment;
	}

	public long getTaxAdjustment() {
		return taxAdjustment;
	}

	public ClientPaySalesTaxEntries clone() {
		return null;

	}

}
