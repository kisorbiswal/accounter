package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.SpecialReference;

public class TransactionMakeDeposit implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6328259772074349465L;
	public static final int TYPE_FINANCIAL_ACCOUNT = 3;
	public static final int TYPE_VENDOR = 2;
	public static final int TYPE_CUSTOMER = 1;

	int version;
	long id;
	/**
	 * The date at which this TransactionMakeDeposit is created.
	 */
	FinanceDate date;
	/**
	 * The type of the TransactionMakeDeposit
	 */
	int type;

	/**
	 * This is used to store the account type of TransactionMakeDeposit
	 */
	Account account;

	/**
	 * This is used to store the vendor type of TransactionMakeDeposit
	 */
	Vendor vendor;

	/**
	 * This is used to store the customer type of TransactionMakeDeposit
	 */
	Customer customer;

	String reference;

	/**
	 * The amount by which this TransactionMakeDeposit is created.
	 */
	double amount;

	/**
	 * To indicate whether this TransactionMakeDeposit is a new one or a old
	 * one.
	 */
	boolean isNewEntry = false;

	/**
	 * This is the reference used to indicate which cash Account is caused this
	 * TransactionMakeDeposit to be created. Generally it will be 'Un Deposited
	 * Funds'
	 */
	Account cashAccount;

	/**
	 * The reference to MakeDeposit which holds all the entries of
	 * TransactionMakeDeposit
	 */
	MakeDeposit makeDeposit;

	/**
	 * The amount which is being paid through this TransactionMakeDeposit
	 */
	double payments = 0D;

	/**
	 * The amount which has to be paid still.
	 */
	double balanceDue = 0D;

	/**
	 * This reference to the Accounts Receivable account is used when we are
	 * creating a new entry to TransactionMakeDeposit of Customer type
	 */
	Account accountsReceivable;

	/**
	 * This reference to the Accounts Receivable account is used when we are
	 * creating a new entry to TransactionMakeDeposit of Vendor type
	 */
	Account accountsPayable;

	// Transaction depositedTransaction;
	/**
	 * This Transaction reference is used to indicate from which MakeDeposit
	 * this TransactionMakeDeposit is called.
	 */
	Transaction depositedTransaction;

	/**
	 * The number of TransactionMakeDeposit.
	 */
	String number;

	/**
	 * To indicate whether this transaction is voided or not.
	 */
	boolean isVoid;

	/**
	 * Every TransactionMakeDeposit consists of a set of
	 * {@link TransactionPayBill}s. This is used because whenever a new entry of
	 * Vendor type is created, an entry to TransactionPayBill is created.
	 */
	Set<TransactionPayBill> transactionPayBills;

	/**
	 * Every TransactionMakeDeposit consists of a set of
	 * {@link CreditsAndPayments}. This is used because whenever a new entry of
	 * {@link Customer} type is created, an entry to {@link CreditsAndPayments}
	 * is created.
	 */
	@SpecialReference
	@ReffereredObject
	CreditsAndPayments creditsAndPayments;

	transient private boolean isOnSaveProccessed;

	public TransactionMakeDeposit() {
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the date
	 */
	public FinanceDate getDate() {
		return date;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the isNewEntry
	 */
	public boolean getIsNewEntry() {
		return isNewEntry;
	}

	/**
	 * @return the cashAccount
	 */
	public Account getCashAccount() {
		return cashAccount;
	}

	public MakeDeposit getMakeDeposit() {
		return makeDeposit;
	}

	public double getPayments() {
		return payments;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public Account getAccountsReceivable() {
		return accountsReceivable;
	}

	public Account getAccountsPayable() {
		return accountsPayable;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the depositedTransaction
	 */
	public Transaction getDepositedTransaction() {
		return depositedTransaction;
	}

	public CreditsAndPayments getCreditsAndPayments() {
		return creditsAndPayments;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isNewEntry) {
			this.cashAccount.updateCurrentBalance(this.makeDeposit, -1
					* this.amount);
			this.depositedTransaction.setIsDeposited(Boolean.FALSE);
		} else {
			switch (this.type) {
			case TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:

				this.account.updateCurrentBalance(this.makeDeposit, -1
						* this.amount);
				this.account.onUpdate(session);
				break;
			case TransactionMakeDeposit.TYPE_CUSTOMER:
				this.customer.updateBalance(session, this.makeDeposit, -1
						* this.amount);
				// this.creditsAndPayments.delete(session);
				this.creditsAndPayments = null;
				break;
			case TransactionMakeDeposit.TYPE_VENDOR:
				this.vendor.updateBalance(session, this.makeDeposit, -1
						* this.amount);
				this.balanceDue -= this.amount;
				for (TransactionPayBill transactionPayBill : this.transactionPayBills) {
					transactionPayBill.makeVoid(session);
				}
				break;
			}
		}
		return false;
	}

	@Override
	public void onLoad(Session session, Serializable serializable) {

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;

		if (!this.isNewEntry) {
			// Update the Undeposited Funds Account of the Entry
			this.cashAccount
					.updateCurrentBalance(this.makeDeposit, this.amount);
			this.cashAccount.onUpdate(session);

			// Makeing the corresponding Transaction as Deposited.

			this.depositedTransaction.setIsDeposited(Boolean.TRUE);

		} else {

			switch (this.type) {

			case TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:

				this.account
						.updateCurrentBalance(this.makeDeposit, this.amount);
				this.account.onUpdate(session);

				break;
			case TransactionMakeDeposit.TYPE_CUSTOMER:

				// this.accountsReceivable.updateCurrentBalance(this.makeDeposit,
				// this.amount);
				// this.accountsReceivable.onUpdate(session);

				// Updating the customer balance.
				// this.customer.updateBalance(session, this.makeDeposit,
				// this.amount);

				if (DecimalUtil.isGreaterThan(this.amount, 0.0)) {

					// Add this Deposit Entry to CreditsAndPayments Table
					creditsAndPayments = new CreditsAndPayments(this);
					session.save(creditsAndPayments);
				}

				break;
			case TransactionMakeDeposit.TYPE_VENDOR:

				// Here Amount is Negative
				// Updating the current and total balances of Accounts
				// Payable
				// account.

				// this.accountsPayable.updateCurrentBalance(this.makeDeposit,
				// this.amount);
				// this.accountsPayable.onUpdate(session);

				// updating the Vendor Balance

				this.vendor.updateBalance(session, this.makeDeposit,
						this.amount);
				this.balanceDue = this.amount;
				break;

			}

		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		if (this.isVoid) {
			if (!this.isNewEntry) {
				this.cashAccount.updateCurrentBalance(this.makeDeposit, -1
						* this.amount);
				this.depositedTransaction.setIsDeposited(Boolean.FALSE);
			} else {
				switch (this.type) {
				case TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:

					this.account.updateCurrentBalance(this.makeDeposit, -1
							* this.amount);
					this.account.onUpdate(session);
					break;
				case TransactionMakeDeposit.TYPE_CUSTOMER:
					this.customer.updateBalance(session, this.makeDeposit, -1
							* this.amount);
					// this.creditsAndPayments.delete(session);
					this.creditsAndPayments = null;
					break;
				case TransactionMakeDeposit.TYPE_VENDOR:
					this.vendor.updateBalance(session, this.makeDeposit, -1
							* this.amount);
					this.balanceDue -= this.amount;
					for (TransactionPayBill transactionPayBill : this.transactionPayBills) {
						transactionPayBill.makeVoid(session);
					}
					break;
				}
			}
		}
		// ChangeTracker.put(this);
		return false;
	}

	public void updatePaymentsAndBalanceDue(double amount) {

		this.payments -= amount;
		this.balanceDue += amount;

	}

	public void setIsNewEntry(boolean isNewEntry) {
		this.isNewEntry = isNewEntry;
	}

	public void setCashAccount(Account cashAccount) {
		this.cashAccount = cashAccount;
	}

	public void setDepositedTransaction(EnterBill depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setDepositedTransaction(CustomerRefund depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setDepositedTransaction(Invoice depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setMakeDeposit(MakeDeposit makeDeposit) {
		this.makeDeposit = makeDeposit;
	}

	public void setDepositedTransaction(PayTAX depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setDepositedTransaction(ReceivePayment depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setDepositedTransaction(CashPurchase depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setDepositedTransaction(CashSales depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setDepositedTransaction(CreditCardCharge depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setDepositedTransaction(TransferFund depositedTransferFund) {
		this.depositedTransaction = depositedTransferFund;
	}

	public void setDepositedTransaction(Estimate estimate) {
		this.depositedTransaction = estimate;

	}

	public void setDepositedTransaction(CustomerCreditMemo depositedTransaction) {
		this.depositedTransaction = depositedTransaction;

	}

	public void setDepositedTransaction(WriteCheck depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setDepositedTransaction(VendorCreditMemo depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setDepositedTransaction(PayBill depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public void setDepositedTransaction(IssuePayment depositedTransaction) {
		this.depositedTransaction = depositedTransaction;
	}

	public void setPayments(double payments) {
		this.payments = payments;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	@Override
	public long getID() {

		return this.id;
	}

	public boolean equals(TransactionMakeDeposit obj) {
		if ((this.account != null & obj.account != null) ? (this.account
				.equals(obj.account))
				: true && (this.vendor.id != 0 && obj.vendor.id != 0) ? (this.vendor.id == obj.vendor.id)
						: true && (this.customer.id != 0 && obj.customer.id != 0) ? (this.customer.id == obj.customer.id)
								: true && (this.cashAccount != null && obj.cashAccount != null) ? (this.cashAccount
										.equals(obj.cashAccount))
										: true && (this.makeDeposit != null && obj.makeDeposit != null) ? (this.makeDeposit
												.equals(obj.makeDeposit))
												: true && (this.accountsPayable != null && obj.accountsPayable != null) ? (this.accountsPayable
														.equals(obj.accountsPayable))
														: true && (this.accountsReceivable != null && obj.accountsReceivable != null) ? (this.accountsReceivable
																.equals(obj.accountsReceivable))
																: true && (this.creditsAndPayments != null && obj.creditsAndPayments != null) ? (this.creditsAndPayments
																		.equals(obj.creditsAndPayments))
																		: true && (this.depositedTransaction != null && obj.depositedTransaction != null) ? (this.depositedTransaction
																				.equals(obj.depositedTransaction))
																				: true
																						&& DecimalUtil
																								.isEquals(
																										this.balanceDue,
																										obj.balanceDue)
																						&& DecimalUtil
																								.isEquals(
																										this.payments,
																										obj.payments)
																						&& (!DecimalUtil
																								.isEquals(
																										this.amount,
																										0) && !DecimalUtil
																								.isEquals(
																										obj.amount,
																										0)) ? DecimalUtil
																						.isEquals(
																								this.amount,
																								obj.amount)
																						: true) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return true;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	public Account getEffectingAccount() {
		if (!this.isNewEntry) {
			return cashAccount;
		} else {
			switch (this.type) {
			case TransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
				return account;
			case TransactionMakeDeposit.TYPE_CUSTOMER:
				return creditsAndPayments.getPayee().getAccount();
			case TransactionMakeDeposit.TYPE_VENDOR:
				return this.vendor.getAccount();
			default:
				return null;
			}
		}
	}

}
