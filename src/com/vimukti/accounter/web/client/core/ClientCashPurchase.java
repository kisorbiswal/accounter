package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientCashPurchase extends ClientTransaction {

	public static final int EMPLOYEE_EXPENSE_STATUS_SAVE = 0;
	public static final int EMPLOYEE_EXPENSE_STATUS_DELETE = 1;
	public static final int EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL = 2;
	public static final int EMPLOYEE_EXPENSE_STATUS_APPROVED = 3;
	public static final int EMPLOYEE_EXPENSE_STATUS_DECLINED = 4;

	String vendor;

	ClientContact contact;

	ClientAddress vendorAddress;

	String employeeName;

	String phone;

	String payFrom;

	String checkNumber;

	ClientAccount cashExpenseAccount;

	long deliveryDate;
	
	int expenseStatus;

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public ClientContact getContact() {
		return contact;
	}

	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	public ClientAddress getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(ClientAddress vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(String payFrom) {
		this.payFrom = payFrom;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public long getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
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
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientCashPurchase";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CASHPURCHASE;
	}

	public String getEmployee() {
		return employeeName;
	}

	public void setEmployee(String employee) {
		this.employeeName = employee;
	}

	public ClientAccount getCashExpenseAccount() {
		return cashExpenseAccount;
	}

	public void setCashExpenseAccount(ClientAccount cashExpenseAccount) {
		this.cashExpenseAccount = cashExpenseAccount;
	}

	public int getExpenseStatus() {
		return expenseStatus;
	}

	public void setExpenseStatus(int expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

}
