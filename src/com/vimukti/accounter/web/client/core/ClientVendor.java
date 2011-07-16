package com.vimukti.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class ClientVendor extends ClientPayee {

	String accountno;

	String selectedAdress;

	long balanceAsOf;

	String expenseAccount;

	double creditLimit;

	String shippingMethod;

	String paymentTerms;

	String vendorGroup;

	String federalTaxId;
	
	//there  three bank details for  newsupplier details 
	
	// Balance due fields

	double current;
	double overDueOneToThirtyDays;
	double overDueThirtyOneToSixtyDays;
	double overDueSixtyOneToNintyDays;
	double overDueOverNintyDays;
	double overDueTotalBalance;

	// Sales Information

	double monthToDate;
	double yearToDate;
	double lastYear;
	double lifeTimePurchases;

	String openingBalanceAccount;

	public ClientVendor() {
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountno;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountno = accountNumber;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	/**
	 * @return the balance
	 */
	@Override
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	@Override
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the balanceAsOf
	 */
	public long getBalanceAsOf() {
		return balanceAsOf;
	}

	/**
	 * @param balanceAsOf
	 *            the balanceAsOf to set
	 */
	public void setBalanceAsOf(long balanceAsOf) {
		this.balanceAsOf = balanceAsOf;
	}

	/**
	 * @return the expenseAccount
	 */
	public String getExpenseAccount() {
		return this.expenseAccount;
	}

	/**
	 * @param expenseAccount
	 *            the expenseAccount to set
	 */
	public void setExpenseAccount(String expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	/**
	 * @return the creditLimit
	 */
	public double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit
	 *            the creditLimit to set
	 */
	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @return the shippingMethod
	 */
	public String getShippingMethod() {
		return shippingMethod != null ? shippingMethod : "";
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the paymentMethod
	 */
	// public long getPaymentMethod() {
	// return paymentMethod;
	// }
	/**
	 * @return the paymentTerms
	 */
	public String getPaymentTermsId() {
		return paymentTerms != null ? paymentTerms : "";
	}

	/**
	 * @param paymentTerms
	 *            the paymentTerms to set
	 */
	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	/**
	 * @return the vendorGroup
	 */
	public String getVendorGroup() {
		return this.vendorGroup;
	}

	/**
	 * @param vendorGroup
	 *            the vendorGroup to set
	 */
	public void setVendorGroup(String vendorGroup) {
		this.vendorGroup = vendorGroup;
	}

	/**
	 * @return the federalTaxId
	 */
	public String getFederalTaxId() {
		return federalTaxId;
	}

	/**
	 * @param federalTaxId
	 *            the federalTaxId to set
	 */
	public void setFederalTaxId(String federalTaxId) {
		this.federalTaxId = federalTaxId;
	}

	/**
	 * @return the current
	 */
	public double getCurrent() {
		return current;
	}

	/**
	 * @param current
	 *            the current to set
	 */
	public void setCurrent(double current) {
		this.current = current;
	}

	/**
	 * @return the overDueOneToThirtyDays
	 */
	public double getOverDueOneToThirtyDays() {
		return overDueOneToThirtyDays;
	}

	/**
	 * @param overDueOneToThirtyDays
	 *            the overDueOneToThirtyDays to set
	 */
	public void setOverDueOneToThirtyDays(double overDueOneToThirtyDays) {
		this.overDueOneToThirtyDays = overDueOneToThirtyDays;
	}

	/**
	 * @return the overDueThirtyOneToSixtyDays
	 */
	public double getOverDueThirtyOneToSixtyDays() {
		return overDueThirtyOneToSixtyDays;
	}

	/**
	 * @param overDueThirtyOneToSixtyDays
	 *            the overDueThirtyOneToSixtyDays to set
	 */
	public void setOverDueThirtyOneToSixtyDays(
			double overDueThirtyOneToSixtyDays) {
		this.overDueThirtyOneToSixtyDays = overDueThirtyOneToSixtyDays;
	}

	/**
	 * @return the overDueSixtyOneToNintyDays
	 */
	public double getOverDueSixtyOneToNintyDays() {
		return overDueSixtyOneToNintyDays;
	}

	/**
	 * @param overDueSixtyOneToNintyDays
	 *            the overDueSixtyOneToNintyDays to set
	 */
	public void setOverDueSixtyOneToNintyDays(double overDueSixtyOneToNintyDays) {
		this.overDueSixtyOneToNintyDays = overDueSixtyOneToNintyDays;
	}

	/**
	 * @return the overDueOverNintyDays
	 */
	public double getOverDueOverNintyDays() {
		return overDueOverNintyDays;
	}

	/**
	 * @param overDueOverNintyDays
	 *            the overDueOverNintyDays to set
	 */
	public void setOverDueOverNintyDays(double overDueOverNintyDays) {
		this.overDueOverNintyDays = overDueOverNintyDays;
	}

	/**
	 * @return the overDueTotalBalance
	 */
	public double getOverDueTotalBalance() {
		return overDueTotalBalance;
	}

	/**
	 * @param overDueTotalBalance
	 *            the overDueTotalBalance to set
	 */
	public void setOverDueTotalBalance(double overDueTotalBalance) {
		this.overDueTotalBalance = overDueTotalBalance;
	}

	/**
	 * @return the monthToDate
	 */
	public double getMonthToDate() {
		return monthToDate;
	}

	/**
	 * @param monthToDate
	 *            the monthToDate to set
	 */
	public void setMonthToDate(double monthToDate) {
		this.monthToDate = monthToDate;
	}

	/**
	 * @return the yearToDate
	 */
	public double getYearToDate() {
		return yearToDate;
	}

	/**
	 * @param yearToDate
	 *            the yearToDate to set
	 */
	public void setYearToDate(double yearToDate) {
		this.yearToDate = yearToDate;
	}

	/**
	 * @return the lastYear
	 */
	public double getLastYear() {
		return lastYear;
	}

	/**
	 * @param lastYear
	 *            the lastYear to set
	 */
	public void setLastYear(double lastYear) {
		this.lastYear = lastYear;
	}

	/**
	 * @return the lifeTimePurchases
	 */
	public double getLifeTimePurchases() {
		return lifeTimePurchases;
	}

	/**
	 * @param lifeTimePurchases
	 *            the lifeTimePurchases to set
	 */
	public void setLifeTimePurchases(double lifeTimePurchases) {
		this.lifeTimePurchases = lifeTimePurchases;
	}

	public String getPrimaryContactId() {

		String primaryContact = "";

		if (this.contacts != null) {
			for (ClientContact contact : contacts) {
				if (contact.isPrimary())
					primaryContact = contact.getStringID();
			}
		}

		return primaryContact;

	}

	public Set<String> getContactsPhoneList() {

		Set<String> phnos = new HashSet<String>();

		if (this.contacts != null) {

			for (ClientContact contact : contacts) {

				phnos.add(contact.getBusinessPhone());
			}

		}

		return phnos;

	}

	public ClientContact getPrimaryContact() {
		if (this.contacts != null) {
			for (ClientContact contact : contacts) {
				if (contact.isPrimary())
					return contact;
			}
		}

		return null;
	}

	@Override
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setVendorGroup(ClientVendorGroup selectVendorGroupFromDetailsTab) {
		this.vendorGroup = selectVendorGroupFromDetailsTab.getStringID();
	}

	public void setPaymentTerms(
			ClientPaymentTerms selectPaymentTermFromDetailsTab) {
		this.paymentTerms = selectPaymentTermFromDetailsTab.getStringID();
	}

	public void setShippingMethod(
			ClientShippingMethod selectShippingMethodFromDetailsTab) {
		this.shippingMethod = selectShippingMethodFromDetailsTab.getStringID();
	}

	public void setExpenseAccount(ClientAccount selectAccountFromDetailsTab) {
		this.expenseAccount = selectAccountFromDetailsTab.getStringID();
	}

	public void setAccountsPayable(String accountsPayableAccount) {
		// FIXME no need to setAccountsPayable , it is available in company
		// object
		// this.accountNumber = String.valueOf(accountsPayableAccount);
	}

	public void setAddress(ClientAddress address) {
		this.address.add(address);
	}

	public void setSelectedAddress(String addressId) {
		this.selectedAdress = addressId;
	}

	public ClientAddress getSelectedAddress() {
		for (ClientAddress address : this.address) {
			if (address.getStringID() == this.selectedAdress)
				return address;
		}
		return null;
	}

	public ClientContact getContact(String contactId) {
		return Utility.getObject(this.contacts, contactId);

	}

	public ClientAddress getAddress(String addressId) {
		return Utility.getObject(this.address, addressId);
	}

	public void setOpeningBalanceAccount(String openingBalancesAccountId) {
		this.openingBalanceAccount = openingBalancesAccountId;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.VENDOR;

	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return "ClientVendor";
	}
}
