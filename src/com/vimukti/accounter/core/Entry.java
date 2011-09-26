package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.services.SessionUtils;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class Entry implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2447331278188267459L;

	public static final int TYPE_FINANCIAL_ACCOUNT = 1;

	public static final int TYPE_VENDOR = 2;

	public static final int TYPE_CUSTOMER = 3;

	public static final int TYPE_VAT = 4;

	public static final int JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT = 1;

	public static final int JOURNAL_ENTRY_TYPE_VENDOR = 2;

	public static final int JOURNAL_ENTRY_TYPE_CUSTOMER = 3;

	int version;

	long id;

	/**
	 * This will specify the type of Entry. If this Entry is Customer type then
	 * we will set Customer, if this Entry is vendor type then we will set
	 * Vendor, if the Entry is Account type then we will set Account othewise we
	 * will set TaxCode
	 * 
	 * @see Customer
	 * @see Vendor
	 * @see Account
	 * @see TaxCode
	 */
	int type;

	/**
	 * This will specify which type of Journal Entry it is. Either normal
	 * journal entry or cash basis journal Entry
	 */
	int journalEntryType;

	/**
	 * The account for which this Entry is being created.
	 */
	@ReffereredObject
	Account account;

	/**
	 * The vendor for which this Entry is being created.
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * The customer for which this Entry is being created.
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * The taxcode for which this Entry is being created.
	 */
	@ReffereredObject
	TAXItem taxItem;

	@ReffereredObject
	TAXCode taxCode;

	/**
	 * The user given text
	 */
	String memo;

	/**
	 * this will specify the debit amount.
	 */
	double debit = 0D;

	/**
	 * this will specify the credit amount.
	 */
	double credit = 0D;

	/**
	 * this is the automatically generated number for each entry, user may
	 * change it later.
	 */
	String voucherNumber;

	/**
	 * The Journal Entry to which it belongs to.
	 */
	@ReffereredObject
	JournalEntry journalEntry;

	/**
	 * The date given to the Entry while creating.
	 */
	FinanceDate entryDate;

	// @ReffereredObject
	// TAXItem vatItem;

	double total;

	transient private boolean isOnSaveProccessed;

	// For UK only
	// TaxAgency VATAgency;

	public Entry() {
	}

	/**
	 * Creates the entry in AccountTransaction table with the effect of
	 * JournalEntry.
	 * 
	 * @param account
	 * @param journalEntry
	 * @param amount
	 * @param closingFYEntry
	 * @param cashBasisEntry
	 * @param session
	 */

	private void createAccountTransaction(Account account,
			JournalEntry journalEntry, double amount, boolean closingFYEntry,
			boolean cashBasisEntry, Session session) {

		AccountTransaction accountTransaction = new AccountTransaction(account,
				journalEntry, amount, closingFYEntry, cashBasisEntry);

		session.saveOrUpdate(accountTransaction);

	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the credit
	 */
	public double getCredit() {
		return credit;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the debit
	 */
	public double getDebit() {
		return debit;
	}

	public FinanceDate getEntryDate() {
		return entryDate;
	}

	/**
	 * @return the journalEntry
	 */
	public JournalEntry getJournalEntry() {
		return journalEntry;
	}

	public int getJournalEntryType() {
		return journalEntryType;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	@Override
	public long getID() {

		return this.id;
	}

	/**
	 * @return the taxCode
	 */
	public TAXItem getTaxItem() {
		return taxItem;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the vatCode
	 */
	public TAXCode getTAXCode() {
		return taxCode;
	}

	/**
	 * @return the vatItem
	 */
	// public TAXItem getVatItem() {
	// return vatItem;
	// }

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the voucherNumber
	 */
	public String getVoucherNumber() {
		return voucherNumber;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// AccounterCore accounterCore = new AccounterCore();
		// accounterCore.setID(this.id);
		// accounterCore.setObjectType(AccounterCoreType.ACCOUNT);
		// ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	public void onLoad(Session arg0, Serializable arg1) {
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		// SessionUtils.updateReferenceCount(null, this, session, true);
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		SessionUtils.update(this, session);

		// String tempNameForLog = "";
		//
		// boolean isDebitforLog = true;
		//
		this.updateAccountBalances(session, false);

		// if (this.account != null) {
		//
		// tempNameForLog = this.account.getName();
		//
		// // Whether this Entry is for Normal Journal Entry or for Cash -
		// // Basis Journal Entry.
		// if (this.journalEntry.journalEntryType ==
		// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY) {
		//
		// if (this.debit != 0.0) {
		// this.total = this.debit;
		// Account account = this.account;
		// account
		// .updateCurrentBalance(this.journalEntry,
		// -this.debit);
		// if (this.account.getID() != 0l) {
		// session.update(account);
		// }
		// account.onUpdate(session);
		//
		// } else if (this.credit != 0.0) {
		// this.total = this.credit;
		// Account account = this.account;
		// account
		// .updateCurrentBalance(this.journalEntry,
		// this.credit);
		// if (this.account.getID() != 0l) {
		// session.update(account);
		// }
		// account.onUpdate(session);
		// isDebitforLog = false;
		// }
		// } else {// This block is for the cash-basis journal entry.
		//
		// AccountTransaction accountTransaction = null;
		// if (this.debit != 0.0) {
		// if (this.account.isIncrease == true) {
		// accountTransaction = new AccountTransaction(
		// this.account, this.journalEntry, -1
		// * this.debit, true, true);
		// } else {
		// accountTransaction = new AccountTransaction(
		// this.account, this.journalEntry, this.debit,
		// true, true);
		// }
		//
		// } else if (this.credit != 0.0) {
		// if (this.account.isIncrease == true) {
		// accountTransaction = new AccountTransaction(
		// this.account, this.journalEntry, this.credit,
		// true, true);
		// } else {
		// accountTransaction = new AccountTransaction(
		// this.account, this.journalEntry, -1
		// * this.credit, true, true);
		// }
		//
		// }
		// session.saveOrUpdate(accountTransaction);
		// }
		//
		// } else if (this.customer != null) {
		//
		// tempNameForLog = this.customer.getName();
		//
		// // Whether this Entry is for Normal Journal Entry or for Cash -
		// // Basis Journal Entry.
		// if (this.journalEntry.journalEntryType ==
		// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY) {
		// if (this.debit != 0.0) {
		//
		// this.customer.updateBalance(session, this.journalEntry,
		// -this.debit);
		//
		// } else if (this.credit != 0.0) {
		// this.customer.updateBalance(session, this.journalEntry,
		// this.credit);
		// isDebitforLog = false;
		// }
		//
		// } else {
		// AccountTransaction accountTransaction = null;
		// Account otherCashIncomeAccount =
		// Company.getCompany().otherCashIncomeAccount;
		// if (this.debit != 0.0) {
		// accountTransaction = new AccountTransaction(
		// otherCashIncomeAccount, this.journalEntry, -1
		// * this.debit, true, true);
		//
		// } else if (this.credit != 0.0) {
		// accountTransaction = new AccountTransaction(
		// otherCashIncomeAccount, this.journalEntry,
		// this.credit, true, true);
		// }
		// session.saveOrUpdate(accountTransaction);
		// }
		//
		// } else if (this.vendor != null
		// || this.VATAgency != null
		// ) {
		// tempNameForLog = this.vendor.getName();
		//
		// // Whether this Entry is for Normal Journal Entry or for Cash -
		// // Basis Journal Entry.
		// if (this.journalEntry.journalEntryType ==
		// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY) {
		// if (this.debit != 0.0) {
		//
		// this.vendor.updateBalance(session, this.journalEntry,
		// -this.debit);
		// } else if (this.credit != 0.0) {
		// this.vendor.updateBalance(session, this.journalEntry,
		// this.credit);
		// isDebitforLog = false;
		// }
		// } else {
		// AccountTransaction accountTransaction = null;
		// Account otherCashExpenseAccount =
		// Company.getCompany().otherCashExpenseAccount;
		// if (this.debit != 0.0) {
		// accountTransaction = new AccountTransaction(
		// otherCashExpenseAccount, this.journalEntry,
		// this.debit, true, true);
		//
		// } else if (this.credit != 0.0) {
		// accountTransaction = new AccountTransaction(
		// otherCashExpenseAccount, this.journalEntry, -1
		// * this.credit, true, true);
		// }
		// session.saveOrUpdate(accountTransaction);
		// }
		//
		// } else {
		//
		// // Whether this Entry is for Normal Journal Entry or for Cash -
		// // Basis Journal Entry.
		//
		// if (this.journalEntry.journalEntryType ==
		// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY) {
		// if (this.taxCode != null && this.debit != 0.0) {
		// tempNameForLog = this.taxCode.getName();
		// this.taxCode.getTaxAgency().updateBalance(session,
		// this.journalEntry, -this.debit);
		// } else if (this.taxCode != null && this.credit != 0.0) {
		// tempNameForLog = this.taxCode.getName();
		// this.taxCode.getTaxAgency().updateBalance(session,
		// this.journalEntry, this.credit);
		// isDebitforLog = false;
		// }
		//
		// } else {
		// AccountTransaction accountTransaction = null;
		// Account liabilityAccount = this.taxCode.getTaxAgency()
		// .getLiabilityAccount();
		// if (this.taxCode != null && this.debit != 0.0) {
		// if (liabilityAccount.isIncrease == true) {
		// accountTransaction = new AccountTransaction(
		// liabilityAccount, this.journalEntry,
		// this.debit, true, true);
		//
		// } else {
		// accountTransaction = new AccountTransaction(
		// liabilityAccount, this.journalEntry, -1
		// * this.debit, true, true);
		//
		// }
		// } else if (this.taxCode != null && this.credit != 0.0) {
		// if (liabilityAccount.isIncrease == true) {
		// accountTransaction = new AccountTransaction(
		// liabilityAccount, this.journalEntry, -1
		// * this.debit, true, true);
		//
		// } else {
		// accountTransaction = new AccountTransaction(
		// liabilityAccount, this.journalEntry,
		// this.debit, true, true);
		//
		// }
		// }
		// session.saveOrUpdate(accountTransaction);
		// }
		//
		// }

		// FinanceLogger.log("Entry with " + tempNameForLog + " "
		// + (isDebitforLog ? "Debit" : "Credit") + " with amount : "
		// + (isDebitforLog ? this.debit : this.credit));
		//
		// if (this.credit != 0)
		// this.journalEntry.creditTotal += this.total = this.credit;
		// else
		// this.journalEntry.debitTotal += this.total = this.debit;
		//
		// if (this.credit != 0) {
		// this.total = this.credit;
		// this.journalEntry.creditTotal += this.total = this.credit;
		// } else {
		// this.total = this.debit;
		// this.journalEntry.debitTotal += this.debit;
		// }
		//
		// this.journalEntry.total += this.debit;
		// // ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		// SessionUtils.update(this, session);

		// if (this.journalEntry.isBecameVoid()) {

		// this.updateAccountBalances(session, true);

		// this.logEntry();

		// FinanceLogger.log("Entry is going to void...");
		// String tempNameForLog = "";
		//
		// boolean isDebitforLog = true;
		//
		// if (this.account != null) {
		// tempNameForLog = this.account.getName();
		//
		// if (this.journalEntry.journalEntryType ==
		// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY) {
		// if (this.debit != 0.0) {
		// Account account = this.account;
		// account.updateCurrentBalance(this.journalEntry,
		// this.debit);
		// if (this.account.getID() != 0l) {
		// session.update(account);
		// }
		// account.onUpdate(session);
		//
		// } else if (this.credit != 0.0) {
		// Account account = this.account;
		// account.updateCurrentBalance(this.journalEntry,
		// -this.credit);
		// if (this.account.getID() != 0l) {
		// session.update(account);
		// }
		// account.onUpdate(session);
		// isDebitforLog = false;
		// }
		// if (!this.account.getName().equals(
		// AccounterConstants.OPENING_BALANCE)) {
		// this.account.openingBalance -= this.total;
		// session.update(account);
		// account.onUpdate(session);
		// }
		// } else {// This block is for the cash-basis journal entry.
		//
		// // here code for void of cash basis journal entry
		// }
		//
		// } else if (this.customer != null) {
		// tempNameForLog = this.customer.getName();
		// if (this.journalEntry.journalEntryType ==
		// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY) {
		// if (this.debit != 0.0) {
		// this.customer.updateBalance(session, this.journalEntry,
		// this.debit);
		// } else if (this.credit != 0.0) {
		// this.customer.updateBalance(session, this.journalEntry,
		// -this.credit);
		// isDebitforLog = false;
		// }
		//
		// this.customer.openingBalance -= this.total;
		// session.saveOrUpdate(this.customer);
		// } else {// This block is for the cash-basis journal entry.
		//
		// // here code for void of cash basis journal entry
		// }
		//
		// } else if (this.vendor != null) {
		// tempNameForLog = this.vendor.getName();
		// if (this.journalEntry.journalEntryType ==
		// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY) {
		// if (this.debit != 0.0) {
		// this.vendor.updateBalance(session, this.journalEntry,
		// this.debit);
		// } else if (this.credit != 0.0) {
		// this.vendor.updateBalance(session, this.journalEntry,
		// -this.credit);
		// isDebitforLog = false;
		// }
		// this.vendor.openingBalance -= this.total;
		// session.saveOrUpdate(this.vendor);
		// } else {// This block is for the cash-basis journal entry.
		//
		// // here code for void of cash basis journal entry
		// }
		//
		// } else {
		//
		// if (this.journalEntry.journalEntryType ==
		// JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY) {
		// if (this.debit != 0.0) {
		// this.taxCode.getTaxAgency().updateBalance(session,
		// this.journalEntry, this.debit);
		// } else if (this.credit != 0.0) {
		// this.taxCode.getTaxAgency().updateBalance(session,
		// this.journalEntry, -this.credit);
		// isDebitforLog = false;
		// }
		// } else {// This block is for the cash-basis journal entry.
		//
		// // here code for void of cash basis journal entry
		// }
		//
		// }
		// FinanceLogger.log("Entry with " + tempNameForLog + " "
		// + (isDebitforLog ? "Debit" : "Credit") + " with amount : "
		// + (isDebitforLog ? this.debit : this.credit)
		// + " has been voided");
		// }
		// ChangeTracker.put(this);
		return false;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @param credit
	 *            the credit to set
	 */
	public void setCredit(double credit) {
		this.credit = credit;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @param debit
	 *            the debit to set
	 */
	public void setDebit(double debit) {
		this.debit = debit;
	}

	public void setEntryDate(FinanceDate entryDate) {
		this.entryDate = entryDate;
	}

	/**
	 * @param journalEntry
	 *            the journalEntry to set
	 */
	public void setJournalEntry(JournalEntry journalEntry) {
		this.journalEntry = journalEntry;
	}

	public void setJournalEntryType(int journalEntryType) {
		this.journalEntryType = journalEntryType;

		// FinanceLogger.log("Entry with "
		// + tempNameForLog
		// + " "
		// + (isDebitforLog ? "Debit" : "Credit")
		// + " with amount : "
		// + (isDebitforLog ? this.journalEntry.debitTotal
		// : this.journalEntry.debitTotal));
		// // ChangeTracker.put(this);
		// return false;

	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param vatCode
	 *            the vatCode to set
	 */
	public void setTaxCode(TAXCode taxCode) {
		this.taxCode = taxCode;
	}

	/**
	 * @param vatItem
	 *            the vatItem to set
	 */
	public void setTaxItem(TAXItem taxItem) {
		this.taxItem = taxItem;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * @param voucherNumber
	 *            the voucherNumber to set
	 */
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	protected void updateAccount(Entry entry, Session session, double amount) {

		Account account;

		if (id == 0l) {
			account = entry.account;

		} else {
			account = (Account) session.get(Account.class, entry.account.id);
		}

		account.updateCurrentBalance(entry.journalEntry, amount);
		if (entry.account.getID() != 0l) {
			session.update(account);
			account.onUpdate(session);
		} else
			account.onUpdate(session);
	}

	protected void updateEntryAccountBalances(Session session, boolean isOld) {

		if (isOld) {
			this.credit = !DecimalUtil.isEquals(this.credit, 0) ? -this.credit
					: 0.0;
			this.debit = !DecimalUtil.isEquals(this.debit, 0) ? -this.debit
					: 0.0;
		}

		this.updateAccountBalances(session, true);
	}

	/**
	 * Calculates the amount by which the entity has to be updated. At first,
	 * initialise the variable amount with -debit or credit, whichever is
	 * non-zero. If entry is voiding, reverse its sign. Check the JournalEntry
	 * type (Normal or Cash basis) and call the corresponding method.
	 * 
	 * @param entry
	 * @param session
	 * @param isVoid
	 */
	protected void updateAccountBalances(Session session, boolean isVoid) {

		if (!(!DecimalUtil.isEquals(this.debit, 0.0) ^ !DecimalUtil.isEquals(
				this.credit, 0.0)))
			return;

		double amount = !DecimalUtil.isEquals(this.debit, 0.0) ? -(this.total = this.debit)
				: (this.total = this.credit);

		if (!isVoid) {
			this.journalEntry.creditTotal += this.credit;
			this.journalEntry.debitTotal += this.debit;

		} else if (!this.journalEntry.isEdited) {
			amount = -amount;
		}

		if (this.journalEntry.journalEntryType == JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY) {
			this.createNormalJournalEntries(session, amount);
		} else {
			this.createCashBasisJournalEntries(session, amount);
		}

	}

	/**
	 * For the Cash basis Journal Entries we need to create entries in
	 * AccountTransaction. The Account used in it is varied depending on the
	 * type of the entry used. For Account type, the account is the account
	 * itself, for Customer type it is 'Other Cash Income Account', for Vendor
	 * it is 'Other Cash Expense Account' and for TaxCode it is the TaxAgency's
	 * liability Account. The value of the amount changes according to this
	 * account.
	 * 
	 * @param entry
	 * @param session
	 * @param amount
	 */
	private void createCashBasisJournalEntries(Session session, double amount) {

		Account account = null;
		if (this.account != null) {

			account = this.account;
			amount = account.isIncrease ? amount : -amount;

		} else if (this.customer != null) {

			account = customer.getCompany().otherCashIncomeAccount;

		} else if (this.vendor != null) {

			account = vendor.getCompany().otherCashExpenseAccount;
			amount = -amount;

		} else if (this.taxItem != null) {

			account = this.taxItem.getTaxAgency().getSalesLiabilityAccount();
			amount = account.isIncrease ? -amount : amount;
		}

		createAccountTransaction(account, this.journalEntry, amount, true,
				true, session);

	}

	/**
	 * A general purpose Journal Entry is called a Normal Journal Entry. When a
	 * normal journal entry is created we need to update the corresponding
	 * entry's balances with the amount passed as argument.
	 * 
	 * @param entry
	 * @param session
	 * @param amount
	 */

	protected void createNormalJournalEntries(Session session, double amount) {

		if (this.account != null) {

			updateAccount(this, session, amount);

			// if (this.account.getID() != 0l) {
			// session.update(account);
			// }
			// account.onUpdate(session);

		} else if (this.customer != null) {

			if (this.journalEntry.creditsAndPayments == null
					&& !DecimalUtil.isEquals(this.credit, 0.0) && id != 0l) {
				this.journalEntry.creditsAndPayments = new CreditsAndPayments(
						this.journalEntry);
				session.save(this.journalEntry.creditsAndPayments);

			} else if (this.journalEntry.creditsAndPayments != null
					&& DecimalUtil.isEquals(
							this.journalEntry.creditsAndPayments.creditAmount,
							0.0d)) {
				this.journalEntry.creditsAndPayments.update(this.journalEntry);
			} else if (this.journalEntry.creditsAndPayments != null && id != 0l) {

				CreditsAndPayments creditsAndPayments = (CreditsAndPayments) session
						.get(CreditsAndPayments.class,
								this.journalEntry.creditsAndPayments.id);
				// creditsAndPayments.payee = null;
				creditsAndPayments.balance = 0.0;
				session.saveOrUpdate(creditsAndPayments);
				this.journalEntry.creditsAndPayments = null;

			}

			if (!DecimalUtil.isEquals(this.debit, 0.0) && id != 0l)
				this.journalEntry.balanceDue += -1 * amount;

			if (id == 0l) {
				this.customer.updateBalance(session, this.journalEntry, amount);
			} else {

				Customer customer = (Customer) session.get(Customer.class,
						this.customer.id);
				customer.updateBalance(session, this.journalEntry, amount);
			}

		} else if (this.vendor != null) {

			if (this.journalEntry.creditsAndPayments == null
					&& !DecimalUtil.isEquals(this.debit, 0.0) && id != 0l) {
				this.journalEntry.creditsAndPayments = new CreditsAndPayments(
						this.journalEntry);
				session.save(this.journalEntry.creditsAndPayments);

			} else if (this.journalEntry.creditsAndPayments != null
					&& DecimalUtil.isEquals(
							this.journalEntry.creditsAndPayments.creditAmount,
							0.0d)) {
				this.journalEntry.creditsAndPayments.update(this.journalEntry);
				session.save(this.journalEntry.creditsAndPayments);
			} else if (this.journalEntry.creditsAndPayments != null && id != 0l) {

				CreditsAndPayments creditsAndPayments = (CreditsAndPayments) session
						.get(CreditsAndPayments.class,
								this.journalEntry.creditsAndPayments.id);

				creditsAndPayments.balance = 0.0;
				session.saveOrUpdate(creditsAndPayments);
				this.journalEntry.creditsAndPayments = null;
				// creditsAndPayments.payee = null;
			}

			if (!DecimalUtil.isEquals(this.credit, 0.0) && id != 0l)
				this.journalEntry.balanceDue += amount;

			if (id == 0l) {
				this.vendor.updateBalance(session, this.journalEntry, amount);
			} else {

				Vendor vendor = (Vendor) session.get(Vendor.class,
						this.vendor.id);
				vendor.updateBalance(session, this.journalEntry, amount);
			}

		} else if (this.taxItem != null) {

			TAXItem taxItem = (TAXItem) session.get(TAXItem.class,
					this.taxItem.id);
			taxItem.getTaxAgency().updateBalance(session, this.journalEntry,
					amount);

		}

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * Is to update Memo in Entry if and only if payee Name or account Name was
	 * altered
	 */
	public static void updateEntryMemo(Company company, Session session,
			String oldName, String newName) {
		session.getNamedQuery("update.Entry.oldNameTo.newName")
				.setString("newName", newName).setString("oldName", oldName)
				.setEntity("company", company).executeUpdate();
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

}
