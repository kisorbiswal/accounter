package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientPayBill extends ClientTransaction {

	public static final int TYPE_PAYBILL = 1;

	public static final int TYPE_VENDOR_PAYMENT = 2;

	String name;

	String fileAs;

	String payFrom;

	private long billDueOnOrBefore;

	String vendor;
	boolean isToBePrinted;

	double endingBalance;

	String accountsPayable;

	private double unusedAmount = 0D;

	private double UnUsedCredits = 0D;

	private double vendorBalance = 0D;

	int payBillType;

	ClientAddress address;

	String checkNumber;

	// ClientTaxCode VATCode;
	//
	// double VATFraction;

	// public ClientTaxCode getVATCode() {
	// return VATCode;
	// }
	//
	// public void setVATCode(ClientTaxCode code) {
	// VATCode = code;
	// }
	//
	// public double getVATFraction() {
	// return VATFraction;
	// }
	//
	// public void setVATFraction(double fraction) {
	// VATFraction = fraction;
	// }

	public String getFileAs() {
		return fileAs;
	}

	public boolean isToBePrinted() {
		return isToBePrinted;
	}

	public void setToBePrinted(boolean isToBePrinted) {
		this.isToBePrinted = isToBePrinted;
	}

	public void setFileAs(String fileAs) {
		this.fileAs = fileAs;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ClientPayBill";
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

	public void setPayBillType(int typePaybill) {

		this.payBillType = typePaybill;

	}

	public void setAccountsPayable(String accountsPayableAccount) {
		// this.accountsPayable ;
		// TODO

	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * @param checkNumber
	 *            the checkNumber to set
	 */
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public void setPayFrom(ClientAccount payFromAccount) {
		this.payFrom = payFromAccount.getStringID();
	}

	public void setBillDueOnOrBefore(ClientFinanceDate enteredDate) {
		this.billDueOnOrBefore = enteredDate.getTime();

	}

	public void setVendor(ClientVendor vendor2) {
		this.vendor = vendor2.getStringID();

	}

	public void setEndingBalance(double amount) {
		this.endingBalance = amount;

	}

	public double getEndingBalance() {
		return this.endingBalance;
	}

	public int getPayBillType() {
		return payBillType;

	}

	public String getVendor() {
		return this.vendor;
	}

	public ClientAddress getAddress() {
		return this.address;
	}

	public String getPayFrom() {
		return this.payFrom;
	}

	public String getAccountsPayable() {
		return this.accountsPayable;
	}

	public void setBillDueOnOrBefore(long billDueOnOrBefore) {
		this.billDueOnOrBefore = billDueOnOrBefore;
	}

	public long getBillDueOnOrBefore() {
		return billDueOnOrBefore;
	}

	public void setVendorBalance(double vendorBalance) {
		this.vendorBalance = vendorBalance;
	}

	public double getVendorBalance() {
		return vendorBalance;
	}

	public void setUnusedAmount(double unusedAmount) {
		this.unusedAmount = unusedAmount;
	}

	public double getUnusedAmount() {
		return unusedAmount;
	}

	public ClientAddress setAddress(ClientAddress billingAddress) {
		return address;

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

		return "ClientPayBill";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAYBILL;
	}

	public double getUnUsedCredits() {
		return UnUsedCredits;
	}

	public void setUnUsedCredits(double UnUsedCredits) {
		this.UnUsedCredits = UnUsedCredits;

	}
}
