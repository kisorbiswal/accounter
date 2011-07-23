package com.vimukti.accounter.web.client.core;

public class ClientCustomerPrePayment extends ClientTransaction {

	String customer;

	String depositIn;

	ClientAddress address;

	double balanceDue = 0d;

	private double customerBalance = 0D;

	private double endingBalance;

	private boolean isToBePrinted;

	private String checkNumber;

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	public double getCustomerBalance() {
		return customerBalance;
	}

	public void setCustomerBalance(double customerBalance) {
		this.customerBalance = customerBalance;
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientCustomerPrePayment";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CUSTOMERPREPAYMENT;
	}

	@Override
	public long getID(){
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getDepositIn() {
		return depositIn;
	}

	public void setDepositIn(String depositIn) {
		this.depositIn = depositIn;
	}

	public ClientAddress getAddress() {
		return address;
	}

	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}

	public double getEndingBalance() {
		return endingBalance;
	}

	public void setToBePrinted(boolean isToBePrinted) {
		this.isToBePrinted = isToBePrinted;
	}

	public boolean isToBePrinted() {
		return isToBePrinted;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

}
