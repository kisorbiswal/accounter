package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
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

public class Customer extends Payee implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The till date up to which the Specified Opening balance of the Customer
	 * is for.
	 */
	FinanceDate balanceAsOf;

	String number;

	// as we are not implementing Employee section, it does not have any
	// functionality.
	SalesPerson salesPerson;

	double creditLimit = 0D;
	/**
	 * The default Price Level which can be applied on the Items sales price
	 * when ever a customer being used in any Transaction.
	 * 
	 * @see PriceLevel
	 */
	PriceLevel priceLevel;

	CreditRating creditRating;

	/**
	 * The default Shipping method through which a Transaction should done.
	 * 
	 * @see ShippingMethod
	 */
	ShippingMethod shippingMethod;
	/**
	 * The default Payment Term which can be applied on any Transaction when
	 * ever a Customer being selected.
	 * 
	 * @see PaymentTerms
	 */
	PaymentTerms paymentTerm;

	/**
	 * The Customer Group through which this customer is categorised.
	 */
	CustomerGroup customerGroup;

	/**
	 * The default Tax which should be applied on a Transaction immediately
	 * after selecting a Customer.
	 * 
	 * @see TaxGroup
	 * @see TaxCode
	 * @see TaxAgency
	 */
	TAXGroup taxGroup;

	/*
	 * ========================================================================
	 * The following fields are not used yet. Because we have not implemented
	 * all tabs of the Customer creation in MS Accounting.
	 * ===========================================================
	 */
	// Balance due fields
	double current = 0D;
	double overDueOneToThirtyDays = 0D;
	double overDueThirtyOneToSixtyDays = 0D;
	double overDueSixtyOneToNintyDays = 0D;
	double overDueOverNintyDays = 0D;
	double overDueTotalBalance = 0D;

	// Payment Information

	int averageDaysToPay;
	int averageDaysToPayYTD;

	// Sales Information

	double monthToDate = 0D;
	double yearToDate = 0D;
	double lastYear = 0D;
	double lifeTimeSales = 0D;

	/*
	 * =================================================================
	 */
	// transient Customer previousCustomer;

	public Customer() {
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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
	 * @return the memo
	 */
	@Override
	public String getMemo() {
		return memo;
	}

	/**
	 * @return the salesPerson
	 */
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @return the creditLimit
	 */
	public double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @return the priceLevel
	 */
	public PriceLevel getPriceLevel() {
		return priceLevel;
	}

	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
	}

	public void setCreditRating(CreditRating creditRating) {
		this.creditRating = creditRating;
	}

	public void setPriceLevel(PriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the creditRating
	 */
	public CreditRating getCreditRating() {
		return creditRating;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the customerGroup
	 */
	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}

	/**
	 * @return the taxGroup
	 */
	public TAXGroup getTaxGroup() {
		return taxGroup;
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
	 * @return the averageDaysToPay
	 */
	public int getAverageDaysToPay() {
		return averageDaysToPay;
	}

	/**
	 * @return the averageDaysToPayYTD
	 */
	public int getAverageDaysToPayYTD() {
		return averageDaysToPayYTD;
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

	/**
	 * @return the lifeTimeSales
	 */
	public double getLifeTimeSales() {
		return lifeTimeSales;
	}

	/**
	 * @param openingBalanceAccount
	 *            the openingBalanceAccount to set
	 */
	// public void setOpeningBalanceAccount(Account openingBalanceAccount) {
	// this.openingBalanceAccount = openingBalanceAccount;
	// }
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
				"Customer with Name {0} and Balance {1} has been deleted",
				this.getName(), String.valueOf(this.getBalance()));

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.CUSTOMER);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// try {
		// this.previousCustomer = (Customer) this.clone();
		// } catch (CloneNotSupportedException e) {
		// //
		// e.printStackTrace();
		// }
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		super.onSave(session);
		isOnSaveProccessed = true;
		setType(TYPE_CUSTOMER);
		// SessionUtils.updateReferenceCount(null, this, session, true);

		// Logging the Customer info.

		FinanceLogger.log(
				"Opening Balance= {0}    Is Opening Balance Editable: {1}",
				String.valueOf(this.getOpeningBalance()),
				String.valueOf(this.isOpeningBalanceEditable()));

		return onUpdate(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		// if (previousCustomer != null) {
		// SessionUtils.updateReferenceCount(previousCustomer, this, session,
		// true);
		// }
		super.onUpdate(session);
		if (!DecimalUtil.isEquals(this.openingBalance, 0.0)
				&& isOpeningBalanceEditable) {

			FinanceLogger
					.log("Create Journal Entry if Opening Balance is not 0 to this Customer");

			this.isOpeningBalanceEditable = Boolean.FALSE;
			// Query query = session.getNamedQuery("getNextTransactionNumber");
			// query.setLong("type", Transaction.TYPE_JOURNAL_ENTRY);
			// List list = query.list();
			// long nextVoucherNumber = 1;
			// if (list != null && list.size() > 0) {
			// nextVoucherNumber = ((Long) list.get(0)).longValue() + 1;
			// }
			String nextVoucherNumber = NumberUtils
					.getNextTransactionNumber(Transaction.TYPE_JOURNAL_ENTRY);
			JournalEntry journalEntry = new JournalEntry(this,
					nextVoucherNumber, JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY);
			session.save(journalEntry);
		}

		// /*
		// * Is to update Memo in Entry if and only if customer Name was altered
		// */
		// this.updateEntryMemo(session);

		ChangeTracker.put(this);
		return false;
	}

	@Override
	public Account getAccount() {
		return getCompany().getAccountsReceivableAccount();
	}

	/**
	 * long id;
	 * 
	 * String name;
	 * 
	 * String fileAs;
	 * 
	 * Set<Address> address;
	 * 
	 * Set<Phone> phoneNumbers;
	 * 
	 * Set<Fax> faxNumbers;
	 * 
	 * Set<Email> emails;
	 * 
	 * String webPageAddress;
	 * 
	 * boolean isActive = Boolean.TRUE;
	 * 
	 * Date customerSince;
	 * 
	 * Date balanceAsOf;
	 * 
	 * Set<Contact> contacts;
	 * 
	 * String memo;
	 * 
	 * SalesPerson salesPerson;
	 * 
	 * double creditLimit = 0D;
	 * 
	 * PriceLevel priceLevel;
	 * 
	 * CreditRating creditRating;
	 * 
	 * ShippingMethod shippingMethod;
	 * 
	 * PaymentMethod paymentMethod;
	 * 
	 * PaymentTerms paymentTerm;
	 * 
	 * CustomerGroup customerGroup;
	 * 
	 * TaxGroup taxGroup;
	 * 
	 * // Balance due fields
	 * 
	 * double current = 0D;
	 * 
	 * double overDueOneToThirtyDays = 0D;
	 * 
	 * double overDueThirtyOneToSixtyDays = 0D;
	 * 
	 * double overDueSixtyOneToNintyDays = 0D;
	 * 
	 * double overDueOverNintyDays = 0D;
	 * 
	 * double overDueTotalBalance = 0D;
	 * 
	 * 
	 * 
	 * // Payment Information
	 * 
	 * int averageDaysToPay;
	 * 
	 * int averageDaysToPayYTD;
	 * 
	 * 
	 * // Sales Information
	 * 
	 * double monthToDate = 0D;
	 * 
	 * double yearToDate = 0D;
	 * 
	 * double lastYear = 0D;
	 * 
	 * double lifeTimeSales = 0D;
	 */

	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}

	public void setBalanceAsOf(FinanceDate balanceAsOf) {
		this.balanceAsOf = balanceAsOf;
	}

	// @Override
	public boolean equals(Customer cust) {

		if (this.id == cust.id
				&& this.address.size() == cust.address.size()
				&& this.address.equals(cust.address)
				&& this.phoneNumbers.size() == cust.phoneNumbers.size()
				&& this.phoneNumbers.equals(cust.phoneNumbers)
				&& this.faxNumbers.size() == cust.faxNumbers.size()
				&& this.faxNumbers.equals(cust.faxNumbers)
				&& this.contacts.size() == cust.contacts.size()
				&& this.contacts.equals(cust.contacts)
				&& this.isActive == cust.isActive
				&& DecimalUtil.isEquals(this.balance, cust.balance)
				&& DecimalUtil.isEquals(this.openingBalance,
						cust.openingBalance)
				&& this.VATRegistrationNumber == cust.VATRegistrationNumber
				&& DecimalUtil.isEquals(this.openingBalance,
						cust.openingBalance)
				&& DecimalUtil.isEquals(this.creditLimit, cust.creditLimit)
				&& (this.name != null && cust.name != null) ? (this.name
				.equals(cust.name))
				: true && (this.fileAs != null && cust.fileAs != null) ? (this.fileAs
						.equals(cust.fileAs))
						: true && (this.TAXCode != null && cust.TAXCode != null) ? (this.TAXCode == cust.TAXCode)
								: true && (this.webPageAddress != null && cust.webPageAddress != null) ? (this.webPageAddress
										.equals(cust.webPageAddress))
										: true && (this.balanceAsOf != null && cust.balanceAsOf != null) ? (this.balanceAsOf
												.equals(cust.balanceAsOf))
												: true && (this.shippingMethod != null && cust.shippingMethod != null) ? (this.shippingMethod
														.equals(cust.shippingMethod))
														: true && (this.priceLevel != null && cust.priceLevel != null) ? (this.priceLevel
																.equals(cust.priceLevel))
																: true && (this.creditRating != null && cust.creditRating != null) ? (this.creditRating
																		.equals(cust.creditRating))
																		: true && (this
																				.getPaymentMethod() != null && cust
																				.getPaymentMethod() != null) ? (this
																				.getPaymentMethod()
																				.equals(cust
																						.getPaymentMethod()))
																				: true && (this.paymentTerm != null && cust.paymentTerm != null) ? (this.paymentTerm
																						.equals(cust
																								.getPaymentMethod()))
																						: true && (this.customerGroup != null && cust.customerGroup != null) ? (this.customerGroup
																								.equals(cust.customerGroup))
																								: true && (this.taxGroup != null && cust.taxGroup != null) ? (this.taxGroup
																										.equals(cust.taxGroup))
																										: true && (this.salesPerson != null && cust.salesPerson != null) ? (this.salesPerson
																												.equals(cust.salesPerson))
																												: true) {
			return true;
		}
		return false;

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		Customer customer = (Customer) clientObject;

		// if (this.name.equals(customer.name)
		// && this.number.equals(customer.number)
		// && this.id == customer.id)
		// return true;
		//
		// else {

		// Query query = session
		// .createQuery(
		// "from com.vimukti.accounter.core.Customer C where C.number=?")
		// .setParameter(0, this.number);
		//
		// List list = query.list();
		//
		// if (list != null && list.size() > 0) {
		//
		// for (int i = 0; i < list.size(); i++) {
		// Customer newCustomer = (Customer) list.get(i);
		// if ((this.name.equals(newCustomer.name) ||
		// this.number.equals(newCustomer.number))
		// && this.id != newCustomer.id) {
		// throw new InvalidOperationException(
		// "Customer name or number is already in use Please enter Unique");
		// }
		// }
		// }

		Query query = session.getNamedQuery("getCustomers")
				.setString("name", this.name).setString("number", this.number)
				.setLong("id", this.id)
				.setParameter("companyId", customer.getCompany().getID());

		List list = query.list();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();

			if (this.name.equalsIgnoreCase((String) object[0])) {
				Iterator it2 = list.iterator();
				while (it2.hasNext()) {
					Object[] object2 = (Object[]) it2.next();
					if (this.number.equals((String) object2[1])) {
						throw new AccounterException(
								AccounterException.ERROR_NAME_CONFLICT);
						// "A Customer already exists with this name and number");
					}
				}
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "A Customer already exists with this name");
			} else if (this.number.equals((String) object[1])) {
				Iterator it2 = list.iterator();
				while (it2.hasNext()) {
					Object[] object2 = (Object[]) it2.next();
					if (this.name.equalsIgnoreCase((String) object2[0])) {
						throw new AccounterException(
								AccounterException.ERROR_NUMBER_CONFLICT);
						// "A Customer already exists with this name and number");
					}
				}
				throw new AccounterException(
						AccounterException.ERROR_NUMBER_CONFLICT);
				// "A Customer already exists with this number");
			}
		}

		// }
		return true;
	}

}
