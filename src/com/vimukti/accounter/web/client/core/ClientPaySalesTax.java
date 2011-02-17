package com.vimukti.accounter.web.client.core;

import java.util.List;

@SuppressWarnings("serial")
public class ClientPaySalesTax extends ClientTransaction {

	String payFrom;

	long billsDueOnOrBefore;

	String taxAgency;

	// double amount;

	double endingBalance;

//	boolean isVoid = false;

	boolean isEdited = false;

	// List<ClientTransactionPaySalesTax> transactionPaySalesTax;

	public ClientPaySalesTax() {

	}

	public boolean getIsVoid() {
		return isVoid;
	}

	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public boolean getIsEdited() {
		return isEdited;
	}

	public void setIsEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	public String getPayFrom() {
		return this.payFrom;
	}

	public void setPayFrom(String payFrom) {
		this.payFrom = payFrom;
	}

	public long getBillsDueOnOrBefore() {
		return billsDueOnOrBefore;
	}

	public void setBillsDueOnOrBefore(long billsDueOnOrBefore) {
		this.billsDueOnOrBefore = billsDueOnOrBefore;
	}

	public String getTaxAgency() {
		return this.taxAgency;
	}

	public void setTaxAgency(String taxAgency) {
		this.taxAgency = taxAgency;
	}

	public double getEndingBalance() {
		return endingBalance;
	}

	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}

	@Override
	public List<ClientTransactionPaySalesTax> getTransactionPaySalesTax() {
		return transactionPaySalesTax;
	}

	@Override
	public void setTransactionPaySalesTax(
			List<ClientTransactionPaySalesTax> transactionPaySalesTax) {
		this.transactionPaySalesTax = transactionPaySalesTax;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientPaySalesTax";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAY_SALES_TAX;
	}
}
