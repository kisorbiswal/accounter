package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 *  This is a type of {@link Payee)  It refers to a real time entity, supplier or vendor, in accounting terms, to whom the company has debt to pay. This has a 'openingBalance' and 'balance' fields to note its corresponding opening balances and present balance. And this balance is recorded as per the balanceAsOf date provided while creation.
 * 
 * @author Chandan
 *
 */

public class Vendor extends Payee {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3859959561067390029L;

	String vendorNumber;

	/**
	 * The date up to which the specified Vendor
	 */
	FinanceDate balanceAsOf;

	/**
	 * This account defaults the Grid Accounts in any Transaction while
	 * selecting this Vendor.
	 */
	Account expenseAccount;

	double creditLimit;

	/**
	 * The way in which the Shipping of the goods has done for this Vendor.
	 */
	ShippingMethod shippingMethod;

	/**
	 * The terms in which we should pay the bills to the Vendor.
	 */
	PaymentTerms paymentTerms;

	/**
	 * This is to categorize this Vendor
	 */
	VendorGroup vendorGroup;

	String federalTaxId;

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

	String taxId;
	boolean isTrackPaymentsFor1099;
	boolean tdsApplicable;

	/**
	 * @return the instanceVersion
	 */
	public Vendor() {
	}

	/**
	 * @return the address
	 */
	@Override
	public Set<Address> getAddress() {
		return address;
	}

	/**
	 * @return the phoneNumbers
	 */
	@Override
	public Set<Phone> getPhoneNumbers() {
		return phoneNumbers;
	}

	/**
	 * @return the faxNumbers
	 */
	@Override
	public Set<Fax> getFaxNumbers() {
		return faxNumbers;
	}

	/**
	 * @return the webPageAddress
	 */
	@Override
	public String getWebPageAddress() {
		return webPageAddress;
	}

	/**
	 * @return the isActive
	 */
	@Override
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @return the accountNumber
	 */
	public String getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * @return the balance
	 */
	@Override
	public double getBalance() {
		return balance;
	}

	/**
	 * @return the balanceAsOf
	 */
	public FinanceDate getBalanceAsOf() {
		return balanceAsOf;
	}

	/**
	 * @return the contacts
	 */
	@Override
	public Set<Contact> getContacts() {
		return contacts;
	}

	/**
	 * @return the memo
	 */
	@Override
	public String getMemo() {
		return memo;
	}

	/**
	 * @return the expenseAccount
	 */
	public Account getExpenseAccount() {
		return expenseAccount;
	}

	/**
	 * @return the creditLimit
	 */
	public double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @return the paymentTerms
	 */
	public PaymentTerms getPaymentTerms() {
		return paymentTerms;
	}

	/**
	 * @return the vendorGroup
	 */
	public VendorGroup getVendorGroup() {
		return vendorGroup;
	}

	/**
	 * @return the federalTaxId
	 */
	public String getFederalTaxId() {
		return federalTaxId;
	}

	/**
	 * @return the current
	 */
	public double getCurrent() {
		return current;
	}

	/**
	 * @return the overDueOneToThirtyDays
	 */
	public double getOverDueOneToThirtyDays() {
		return overDueOneToThirtyDays;
	}

	/**
	 * @return the overDueThirtyOneToSixtyDays
	 */
	public double getOverDueThirtyOneToSixtyDays() {
		return overDueThirtyOneToSixtyDays;
	}

	/**
	 * @return the overDueSixtyOneToNintyDays
	 */
	public double getOverDueSixtyOneToNintyDays() {
		return overDueSixtyOneToNintyDays;
	}

	/**
	 * @return the overDueOverNintyDays
	 */
	public double getOverDueOverNintyDays() {
		return overDueOverNintyDays;
	}

	/**
	 * @return the overDueTotalBalance
	 */
	public double getOverDueTotalBalance() {
		return overDueTotalBalance;
	}

	/**
	 * @return the monthToDate
	 */
	public double getMonthToDate() {
		return monthToDate;
	}

	/**
	 * @return the yearToDate
	 */
	public double getYearToDate() {
		return yearToDate;
	}

	/**
	 * @return the lastYear
	 */
	public double getLastYear() {
		return lastYear;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public boolean isTrackPaymentsFor1099() {
		return isTrackPaymentsFor1099;
	}

	public void setTrackPaymentsFor1099(boolean isTrackPaymentsFor1099) {
		this.isTrackPaymentsFor1099 = isTrackPaymentsFor1099;
	}

	/**
	 * @return the lifeTimePurchases
	 */
	public double getLifeTimePurchases() {
		return lifeTimePurchases;
	}

	public Contact getPrimaryContact() {

		Contact primaryContact = null;

		if (this.contacts != null) {
			for (Contact contact : contacts) {
				if (contact.isPrimary())
					primaryContact = contact;
			}
		}

		return primaryContact;

	}

	public Set<String> getContactsPhoneList() {

		Set<String> phnos = new HashSet<String>();

		if (this.contacts != null) {

			for (Contact contact : contacts) {

				phnos.add(contact.getBusinessPhone());
			}

		}

		return phnos;

	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		FinanceLogger.log(
				"Vendor with Name {0} and Balance {1} has been deleted",
				this.getName(), String.valueOf(this.getBalance()));

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.VENDOR);
		ChangeTracker.put(accounterCore);
		return false;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;
		setType(Payee.TYPE_VENDOR);
		return onUpdate(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		if (!DecimalUtil.isEquals(this.openingBalance, 0.0)
				&& isOpeningBalanceEditable) {
			FinanceLogger
					.log("Create Journal Entry if Opening Balance is not 0 to This Supplier");

			this.isOpeningBalanceEditable = Boolean.FALSE;
			// Query query = session.getNamedQuery("getNextTransactionNumber");
			// query.setLong("type", Transaction.TYPE_JOURNAL_ENTRY);
			// List list = query.list();
			// long nextVoucherNumber = 1;
			// if (list != null && list.size() > 0) {
			// nextVoucherNumber = ((Long) list.get(0)).longValue() + 1;
			// }
			String nextVoucherNumber = NumberUtils.getNextTransactionNumber(
					Transaction.TYPE_JOURNAL_ENTRY, getCompany());
			JournalEntry journalEntry = new JournalEntry(this,
					nextVoucherNumber, JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY);
			session.save(journalEntry);
		}

		// /*
		// * Is to update Memo in Entry if and only if vendor Name was altered
		// */
		// this.updateEntryMemo(session);

		ChangeTracker.put(this);
		return false;
	}

	@Override
	public Account getAccount() {
		return getCompany().getAccountsPayableAccount();

	}

	@Override
	public void setAddress(Set<Address> address) {
		this.address = address;
	}

	@Override
	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public void setBalanceAsOf(FinanceDate balanceAsOf) {
		this.balanceAsOf = balanceAsOf;
	}

	@Override
	public long getID() {

		return this.id;
	}

	public boolean equals(Vendor vendor) {
		if (this.id == vendor.id
				&& this.address.size() == vendor.address.size()
				&& this.address.equals(vendor.address)
				&& this.phoneNumbers.size() == vendor.phoneNumbers.size()
				&& this.phoneNumbers.equals(vendor.phoneNumbers)
				&& this.faxNumbers.size() == vendor.faxNumbers.size()
				&& this.faxNumbers.equals(vendor.faxNumbers)
				&& this.contacts.size() == vendor.contacts.size()
				&& this.contacts.equals(vendor.contacts)
				&& this.isActive == vendor.isActive
				&& DecimalUtil.isEquals(this.balance, vendor.balance)
				&& DecimalUtil.isEquals(this.openingBalance,
						vendor.openingBalance)
				&& this.VATRegistrationNumber == vendor.VATRegistrationNumber
				&& DecimalUtil.isEquals(this.openingBalance,
						vendor.openingBalance)
				&& DecimalUtil.isEquals(this.creditLimit, vendor.creditLimit)
				&& (this.name != null && vendor.name != null) ? (this.name
				.equals(vendor.name))
				: true && (this.fileAs != null && vendor.fileAs != null) ? (this.fileAs
						.equals(vendor.fileAs))
						: true && (this.TAXCode != null && vendor.TAXCode != null) ? (this.TAXCode == vendor.TAXCode)
								: true && (this.webPageAddress != null && vendor.webPageAddress != null) ? (this.webPageAddress
										.equals(vendor.webPageAddress))
										: true && (this.balanceAsOf != null && vendor.balanceAsOf != null) ? (this.balanceAsOf
												.equals(vendor.balanceAsOf))
												: true && (this.shippingMethod != null && vendor.shippingMethod != null) ? (this.shippingMethod
														.equals(vendor.shippingMethod))

														: true && (this.paymentMethod != null && vendor.paymentMethod != null) ? (this.paymentMethod
																.equals(vendor.paymentMethod))

																: true && (this.vendorGroup != null && vendor.vendorGroup != null) ? (this.vendorGroup
																		.equals(vendor.vendorGroup))

																		: true) {
			return true;
		}
		return false;

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Vendor vendor = (Vendor) clientObject;
		Query query = session.getNamedQuery("getVendor.by.name").setParameter(
				0, vendor.name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			Vendor newVendor = (Vendor) list.get(0);
			if (vendor.id != newVendor.id) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "A Supplier already exists with this name");
			}
		}
		return true;
	}

	@Override
	public String toString() {

		return this.name;
	}

	public boolean isTdsApplicable() {
		return tdsApplicable;
	}

	public void setTdsApplicable(boolean tdsApplicable) {
		this.tdsApplicable = tdsApplicable;
	}
}
