package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientPaySalesTax extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long payFrom;

	long billsDueOnOrBefore;

	long taxAgency;

	// double amount;

	double endingBalance;

	// boolean isVoid = false;

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

	public long getPayFrom() {
		return this.payFrom;
	}

	public void setPayFrom(long payFrom) {
		this.payFrom = payFrom;
	}

	public long getBillsDueOnOrBefore() {
		return billsDueOnOrBefore;
	}

	public void setBillsDueOnOrBefore(long billsDueOnOrBefore) {
		this.billsDueOnOrBefore = billsDueOnOrBefore;
	}

	public long getTaxAgency() {
		return this.taxAgency;
	}

	public void setTaxAgency(long taxAgency) {
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
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientPaySalesTax";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAY_SALES_TAX;
	}

	public ClientPaySalesTax clone() {
		ClientPaySalesTax clientPaySalesTaxClone = (ClientPaySalesTax) this
				.clone();
		return clientPaySalesTaxClone;
	}
}
