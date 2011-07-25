package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientTransactionPaySalesTax implements IAccounterCore {

	long id;

	long taxItem;

	long taxAgency;

	double taxDue;

	double amountToPay;

	ClientTransaction transaction;

	long taxAdjustment;

	long taxRateCalculation;

	ClientPaySalesTaxEntries paySalesTaxEntry;

	int version;

	public int getVersion() {
		return version;
	}

	public ClientPaySalesTaxEntries getPaySalesTaxEntry() {
		return paySalesTaxEntry;
	}

	public void setPaySalesTaxEntry(ClientPaySalesTaxEntries paySalesTaxEntry) {
		this.paySalesTaxEntry = paySalesTaxEntry;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getTaxItem() {
		return taxItem;
	}

	public void setTaxItem(long taxItemId) {
		this.taxItem = taxItemId;
	}

	public long getTaxAgency() {
		return taxAgency;
	}

	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	public double getTaxDue() {
		return taxDue;
	}

	public void setTaxDue(double taxDue) {
		this.taxDue = taxDue;
	}

	public double getAmountToPay() {
		return amountToPay;
	}

	public void setAmountToPay(double amountToPay) {
		this.amountToPay = amountToPay;
	}

	public ClientTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
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

		return "ClientTransactionPaySalesTax";
	}

	public void setTaxAdjustment(long taxAdjustment) {
		this.taxAdjustment = taxAdjustment;
	}

	public long getTaxAdjustment() {
		return taxAdjustment;
	}

	public void setTaxRateCalculation(long taxRateCalculation) {
		this.taxRateCalculation = taxRateCalculation;
	}

	public long getTaxRateCalculation() {
		return taxRateCalculation;
	}

}
