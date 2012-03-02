package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.SpecialReference;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Suresh Garikapati <br>
 *         <b>Transaction</b> is the basic term which reflects any type of
 *         operation through which one can deal with their Customers or
 *         Suppliers in any Accounting system. In any Business one should
 *         require to Invoicing their Customers, Receiving Payments, Billing
 *         their Vendors, Payments to their Vendors etc all these comes under
 *         Transactions. In our Accounting Software, Transaction is the Super
 *         class for all type of Transactions. We have several Type of
 *         Transactions like <i>CASH SALE, INVOICE, CUSTOMER CREDIT MEMO,
 *         CUSTOMER PAYMENT, CUSTOMER REFUND, CASH PURCHASE, VENDOR BILL, VENDOR
 *         CREDIT MEMO, VENDOR PAYMENT, PAY BILL, CREDIT CARD CHARGE, MAKE
 *         DEPOSIT, WRITE CHECK, ISSUE PAYMENT, PAY SALES TAX, PAY VAT, VAT
 *         RETURN</i>.
 * 
 */

public abstract class Transaction extends CreatableObject implements
		IAccounterServerCore, Cloneable {

	Logger log = Logger.getLogger(Transaction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -8540918828515524878L;
	public static final int TYPE_CASH_SALES = 1;
	public static final int TYPE_CASH_PURCHASE = 2;
	public static final int TYPE_CREDIT_CARD_CHARGE = 3;
	public static final int TYPE_CUSTOMER_CREDIT_MEMO = 4;
	public static final int TYPE_CUSTOMER_REFUNDS = 5;
	public static final int TYPE_ENTER_BILL = 6;
	public static final int TYPE_ESTIMATE = 7;
	public static final int TYPE_INVOICE = 8;
	public static final int TYPE_ISSUE_PAYMENT = 9;
	public static final int TYPE_TRANSFER_FUND = 10;
	public static final int TYPE_PAY_BILL = 11;
	public static final int TYPE_VENDOR_PAYMENT = 25;
	public static final int TYPE_RECEIVE_PAYMENT = 12;
	public static final int TYPE_VENDOR_CREDIT_MEMO = 14;
	public static final int TYPE_WRITE_CHECK = 15;
	public static final int TYPE_JOURNAL_ENTRY = 16;
	public static final int TYPE_PAY_TAX = 17;
	public static final int TYPE_EXPENSE = 18;
	public static final int TYPE_PAY_EXPENSE = 19;
	public static final int TYPE_TAX_RETURN = 20;

	// public static final int TYPE_SALES_ORDER = 21;
	public static final int TYPE_PURCHASE_ORDER = 22;
	public static final int TYPE_ITEM_RECEIPT = 23;

	public static final int TYPE_ADJUST_VAT_RETURN = 24;

	public static final int TYPE_CASH_EXPENSE = 26;
	public static final int TYPE_CREDIT_CARD_EXPENSE = 27;
	public static final int TYPE_EMPLOYEE_EXPENSE = 28;

	public static final int TYPE_CUSTOMER_PRE_PAYMENT = 29;
	public static final int TYPE_RECEIVE_TAX = 31;
	public static final int TYPE_TDS_CHALLAN = 34;
	public static final int TYPE_MAKE_DEPOSIT = 35;

	public static final int TYPE_STOCK_ADJUSTMENT = 36;
	public static final int TYPE_BUILD_ASSEMBLY = 37;
	public static final int TYPE_SALES_ORDER = 38;

	public static final int STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED = 0;
	public static final int STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED = 1;
	public static final int STATUS_PAID_OR_APPLIED_OR_ISSUED = 2;

	public static final int STATUS_DRAFT = 201;
	public static final int STATUS_TEMPLATE = 202;
	public static final int STATUS_APPROVE = 203;
	public static final int STATUS_VOID = 204;

	public static final int STATUS_OPEN = 101;
	public static final int STATUS_COMPLETED = 102;
	public static final int STATUS_CANCELLED = 103;

	public static final int CATEGORY_CUSTOMER = 1;
	public static final int CATEGORY_VENDOR = 2;
	public static final int CATEGORY_BANKING = 3;

	public static final int UPDATE_TYPE_PLUS = 100;
	public static final int UPDATE_TYPE_MINUS = 101;
	public static final int TYPE_ADJUST_SALES_TAX = 32;
	public static final int TYPE_MISC_SAMPLE_FORM = 33;
	public static final int ALL = 1000;

	public static final int VIEW_ALL = 0;
	public static final int VIEW_VOIDED = 3;
	public static final int VIEW_OVERDUE = 2;
	public static final int VIEW_OPEN = 1;
	public static final int VIEW_DRAFT = 4;
	private Job job;
	int type;
	FinanceDate transactionDate;
	String number = "0";
	boolean isDefault;
	private Location location;
	private Currency currency;

	private RecurringTransaction recurringTransaction;

	protected double currencyFactor = 1D;

	private StatementRecord statementRecord;

	/**
	 * Many transaction consists of List of {@link TransactionItem}s
	 */
	List<TransactionItem> transactionItems = new ArrayList<TransactionItem>();

	Set<Attachment> attachments = new HashSet<Attachment>();

	@ReffereredObject
	protected Set<TAXRateCalculation> taxRateCalculationEntriesList = new HashSet<TAXRateCalculation>();

	/**
	 * Some of the transactions are able to create a {@link CreditsAndPayments}.
	 */

	@SpecialReference
	@ReffereredObject
	CreditsAndPayments creditsAndPayments;

	/**
	 * To set the status of the transaction
	 * (Unapplied/Notissued/Accepted/Issued/NotAccepted..............)
	 */
	int status = 0;

	/**
	 * To set the status of transaction whether it is approved or saved as draft
	 * or voided or recurring template.
	 */
	private int saveStatus = 0;

	/**
	 * To indicate whether the transaction can be voided/edited or not
	 */
	boolean canVoidOrEdit = true;

	/**
	 * To know that the transaction is deposited to bank or not.
	 */
	boolean isDeposited = false;

	/**
	 * To know that whether the transaction is voided or not.
	 */
	// boolean isVoid;

	transient boolean isEdited;

	/**
	 * This field is used internally for the logical functionality to know that
	 * the transaction is voided before or not.
	 */
	boolean isVoidBefore;

	/**
	 * The total of the transaction with which it is created and saved.
	 */
	double total;

	double netAmount;

	double previousTotal;

	/**
	 * The method of payment by which the payee wants to pay.
	 */
	String paymentMethod;

	/**
	 * Any memo for the particular transaction is entered here
	 */
	String memo;

	/**
	 * The references for the payee are mentioned here
	 */
	String reference;

	// For Sales Tax Liability Report

	double subTotal;
	double totalTaxableAmount;
	double totalNonTaxableAmount;

	private Set<ReconciliationItem> reconciliationItems = new HashSet<ReconciliationItem>();

	// Last Activity
	@Exempted
	private Activity lastActivity;

	/**
	 * This is temporary variable used to delete the reminder.
	 */
	private Reminder tobeDeleteReminder;

	/**
	 * For every Transaction there are one or more {@link AccountTransaction}
	 * entries. So a set of AccountTransaction are to be maintained here.
	 */
	Set<AccountTransaction> accountTransactionEntriesList = new HashSet<AccountTransaction>();

	/**
	 * Some transaction such as {@link CashSales}, {@link Invoice},
	 * {@link CustomerCreditMemo}, ... results in SalesTax payments. So these
	 * are to be stored in PaySalesTaxEntriesList
	 */
	/**
	 * Not using now, this property has been shifted to Comapny.
	 */
	Set<ReceiveVATEntries> receiveVATEntriesList = new HashSet<ReceiveVATEntries>();

	TransactionMakeDepositEntries transactionMakeDepositEntries;

	private boolean isValidated;

	List<WareHouseAllocation> wareHouseAllocations;

	transient protected boolean isOnSaveProccessed;

	private AccounterClass accounterClass;
	private List<TransactionLog> history;

	/**
	 * This will be true when a transaction created automatically by recurring
	 * or anything else.
	 */
	private boolean isAutomaticTransaction;
	public transient double previousCurrencyFactor;

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public CreditsAndPayments getCreditsAndPayments() {
		return creditsAndPayments;
	}

	public void setCreditsAndPayments(CreditsAndPayments creditsAndPayments) {
		this.creditsAndPayments = creditsAndPayments;
	}

	public double getPreviousTotal() {
		return previousTotal;
	}

	public void setPreviousTotal(double previousTotal) {
		this.previousTotal = previousTotal;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public double getTotalTaxableAmount() {
		return totalTaxableAmount;
	}

	public void setTotalTaxableAmount(double totalTaxableAmount) {
		this.totalTaxableAmount = totalTaxableAmount;
	}

	public double getTotalNonTaxableAmount() {
		return totalNonTaxableAmount;
	}

	public void setTotalNonTaxableAmount(double totalNonTaxableAmount) {
		this.totalNonTaxableAmount = totalNonTaxableAmount;
	}

	public Set<AccountTransaction> getAccountTransactionEntriesList() {
		return accountTransactionEntriesList;
	}

	public void setAccountTransactionEntriesList(
			Set<AccountTransaction> accountTransactionEntriesList) {
		this.accountTransactionEntriesList = accountTransactionEntriesList;
	}

	public Set<ReceiveVATEntries> getReceiveVATEntriesList() {
		return receiveVATEntriesList;
	}

	public void setReceiveVATEntriesList(
			Set<ReceiveVATEntries> receiveVATEntriesList) {
		this.receiveVATEntriesList = receiveVATEntriesList;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public TransactionMakeDepositEntries getTransactionMakeDepositEntries() {
		return transactionMakeDepositEntries;
	}

	public void setTransactionMakeDepositEntries(
			TransactionMakeDepositEntries transactionMakeDepositEntries) {
		this.transactionMakeDepositEntries = transactionMakeDepositEntries;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return the netAmount
	 */
	public double getNetAmount() {
		return netAmount;
	}

	/**
	 * @param netAmount
	 *            the netAmount to set
	 */
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return the isVoid
	 */
	public boolean isVoid() {
		return saveStatus == STATUS_VOID;
	}

	/**
	 * @return the isVoidBefore
	 */
	public boolean isVoidBefore() {
		return isVoidBefore;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the FinanceDate
	 */
	public FinanceDate getDate() {
		return transactionDate;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(FinanceDate date) {
		this.transactionDate = date;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return the transactionItems
	 */
	public List<TransactionItem> getTransactionItems() {
		return transactionItems;
	}

	/**
	 * @param transactionItems
	 *            the transactionItems to set
	 */
	public void setTransactionItems(List<TransactionItem> transactionItems) {
		this.transactionItems = transactionItems;

		if (transactionItems == null)
			return;

		for (TransactionItem transactionItem : transactionItems) {

			transactionItem.setTransaction(this);
		}
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the isDeposited
	 */
	public boolean getIsDeposited() {
		return isDeposited;
	}

	/**
	 * @param isDeposited
	 *            the isDeposited to set
	 */
	public void setIsDeposited(boolean isDeposited) {
		this.isDeposited = isDeposited;
	}

	/**
	 * @return the canVoidOrEdit
	 */
	public boolean getCanVoidOrEdit() {
		return canVoidOrEdit;
	}

	/**
	 * @param canVoidOrEdit
	 *            the canVoidOrEdit to set
	 */
	public void setCanVoidOrEdit(boolean canVoidOrEdit) {
		this.canVoidOrEdit = canVoidOrEdit;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * Gives the sum of the total of the discount that has been involved in the
	 * transaction. It is clear that it applies only when the involved
	 * {@link TransactionItem} is of the type 'Item' as for others discount is
	 * not applied.
	 * 
	 * @return double
	 */
	protected double getDiscountTotalSum() {

		double discountAmt = 0.00D;

		if (transactionItems == null) {
			return discountAmt;
		}

		for (TransactionItem item : transactionItems) {

			if (item.getType() != TransactionItem.TYPE_ITEM)
				continue;

			discountAmt += item.getDiscount();

		}

		return discountAmt;
	}

	/**
	 * Gives the sum of the total of the taxable line total that has been
	 * involved in the transaction. It is clear that it applies only when the
	 * involved {@link TransactionItem} is of the type 'Item' or 'Account' as
	 * for others, there will be no line total.
	 * 
	 * @return double
	 */
	protected double getTaxableLineTotalSum() {

		double amount = 0.00D;

		if (transactionItems == null) {
			return amount;
		}

		for (TransactionItem txItem : transactionItems) {

			if (txItem.getType() == TransactionItem.TYPE_COMMENT)
				continue;

			if (txItem.isTaxable()) {

				amount += txItem.getLineTotal();

			}

		}

		return amount;

	}

	/**
	 * Gives the sum of the total of the line total that has been involved in
	 * the transaction. It is clear that it applies only when the involved
	 * {@link TransactionItem} is of the type 'Item' or 'Account' as for others,
	 * there will be no line total.
	 * 
	 * @return double
	 */
	protected double getLineTotalSum() {

		double amount = 0.00D;

		if (transactionItems == null) {
			return amount;
		}

		for (TransactionItem txItem : transactionItems) {

			if (txItem.getType() == TransactionItem.TYPE_COMMENT)
				continue;

			amount += txItem.getLineTotal();

		}

		return amount;

	}

	public boolean isInvoice() {

		return this != null && this instanceof Invoice;
	}

	public boolean isJournalEntry() {
		return this != null && this instanceof JournalEntry;
	}

	public boolean isQuote() {
		return false;
	}

	public boolean isCashSale() {

		return this != null && this instanceof CashSales;
	}

	public boolean isCashPurchase() {

		return this != null && this instanceof CashPurchase;
	}

	public boolean isEnterBill() {

		return this != null && this instanceof EnterBill;
	}

	public boolean isVendorCreditMemo() {

		return this != null && this instanceof VendorCreditMemo;
	}

	public boolean isPayBill() {

		return this != null && this instanceof PayBill;
	}

	public boolean isCreditCardCharge() {

		return this != null && this instanceof CreditCardCharge;
	}

	public boolean isWriteCheck() {

		return this != null && this instanceof WriteCheck;

	}

	public boolean isTransferFund() {

		return this != null && this instanceof TransferFund;

	}

	public boolean isCustomerPrePayment() {

		return this != null && this instanceof CustomerPrePayment;
	}

	public boolean isCustomerCreditMemo() {
		return this != null && this instanceof CustomerCreditMemo;
	}

	public boolean isStockAdjustment() {
		return this instanceof StockAdjustment;
	}

	public boolean isBuildAssembly() {
		return this instanceof BuildAssembly;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.classic.Lifecycle#onLoad(org.hibernate.Session,
	 *      java.io.Serializable)
	 */
	@Override
	public void onLoad(Session session, Serializable arg1) {
		this.previousTotal = total;
		this.isVoidBefore = isVoid();
		this.previousCurrencyFactor = currencyFactor;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.classic.Lifecycle#onSave(org.hibernate.Session)
	 */
	@Override
	public boolean onSave(Session session) throws CallbackException {
		super.onSave(session);
		if (getStatementRecord() != null) {
			getStatementRecord().getTransactionsLists().add(this);
			session.saveOrUpdate(getStatementRecord());
		}
		if (!isDraftOrTemplate()) {
			doCreateEffect(session);
		}
		addCreateHistory();
		if (currency == null) {
			currency = getCompany().getPrimaryCurrency();
			currencyFactor = 1;
		}
		return false;
	}

	protected void checkNullValues() throws AccounterException {

	}

	protected void addCreateHistory() {
		TransactionLog log = new TransactionLog(TransactionLog.TYPE_CREATE);
		if (this.getHistory() == null) {
			this.setHistory(new ArrayList<TransactionLog>());
		}
		this.getHistory().add(log);
	}

	protected void addUpdateHistory() {
		TransactionLog log = new TransactionLog(TransactionLog.TYPE_EDIT);
		if (this.getHistory() == null) {
			this.setHistory(new ArrayList<TransactionLog>());
		}
		this.getHistory().add(log);
	}

	protected void addVoidHistory() {
		TransactionLog log = new TransactionLog(TransactionLog.TYPE_VOID);
		if (this.getHistory() == null) {
			this.setHistory(new ArrayList<TransactionLog>());
		}
		this.getHistory().add(log);
	}

	private void doCreateEffect(Session session) {

		setTransactionType();

		/**
		 * These lines are commented as this condition is already checked in all
		 * the subclasses of this class
		 */

		/**
		 * Updating the Transaction amount according to the type of Transaction.
		 */

		double amount = (isDebitTransaction() ? 1d : -1d) * this.total;

		/**
		 * Some Transactions may have linked Account. This linked Account need
		 * to update with the Transaction amount.
		 */
		this.updateEffectedAccount(amount);
		/**
		 * Each Transaction must have one Payee linked with it. But for Some
		 * transactions we need to update the linked Payee Balance depending on
		 * the type of Transaction.
		 */
		// amount = (isDebitTransaction() ? 1d : -1d)
		// * (type == Transaction.TYPE_PAY_BILL ? this.subTotal
		// : this.total);
		// if (!DecimalUtil.isEquals(amount, 0))
		this.updatePayee(true);

		/**
		 * The following code is particularly for Sales Tax Liability Report
		 */
		// if (getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
		this.updateTaxAndNonTaxableAmounts();
		// }
		// }
		if (tobeDeleteReminder != null) {
			RecurringTransaction recurringTransaction = tobeDeleteReminder
					.getRecurringTransaction();
			recurringTransaction.getReminders().remove(tobeDeleteReminder);
			session.saveOrUpdate(recurringTransaction);
			new FinanceTool()
					.deleteRecurringTask(session, getCompany().getId());
		}
	}

	private void updateTaxAndNonTaxableAmounts() {
		// this.subTotal = 0.0;
		this.totalTaxableAmount = 0.0;
		this.totalNonTaxableAmount = 0.0;

		if (this.transactionItems != null) {
			for (TransactionItem ti : this.transactionItems) {
				// this.subTotal += ti.lineTotal;
				if (ti.isTaxable) {
					this.totalTaxableAmount += ti.lineTotal;
				} else {
					this.totalNonTaxableAmount += ti.lineTotal;
				}
			}
		}
	}

	private void setTransactionType() {
		// if (this.isCashPurchase())
		// this.type = Transaction.TYPE_CASH_PURCHASE;
		// else
		if (this.isCashSale())
			this.type = Transaction.TYPE_CASH_SALES;
		// else if (this.isCreditCardCharge())
		// this.type = Transaction.TYPE_CREDIT_CARD_CHARGE;
		else if (this.isEnterBill())
			this.type = Transaction.TYPE_ENTER_BILL;
		else if (this.isInvoice())
			this.type = Transaction.TYPE_INVOICE;
		else if (this.isTransferFund())
			this.type = Transaction.TYPE_TRANSFER_FUND;
		else if (this.isPayBill())
			this.type = Transaction.TYPE_PAY_BILL;
		else if (this.isPayTax())
			this.type = Transaction.TYPE_PAY_TAX;
		else if (this.isReceiveVAT())
			this.type = Transaction.TYPE_RECEIVE_TAX;
		else if (this.isQuote())
			this.type = Transaction.TYPE_ESTIMATE;
		else if (this.isReceivePayment())
			this.type = Transaction.TYPE_RECEIVE_PAYMENT;
		else if (this.isVendorCreditMemo())
			this.type = Transaction.TYPE_VENDOR_CREDIT_MEMO;
		else if (this.isWriteCheck())
			this.type = Transaction.TYPE_WRITE_CHECK;
		else if (this.isCustomerPrePayment())
			this.type = Transaction.TYPE_CUSTOMER_PRE_PAYMENT;
		else if (this.isTAXAdjustment())
			this.type = Transaction.TYPE_ADJUST_SALES_TAX;
		else if (this.isJournalEntry())
			this.type = Transaction.TYPE_JOURNAL_ENTRY;

	}

	public boolean isTAXAdjustment() {
		return this != null && this instanceof TAXAdjustment;
	}

	public boolean isReceiveVAT() {

		return this != null && this instanceof ReceiveVAT;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.classic.Lifecycle#onDelete(org.hibernate.Session)
	 */
	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!isVoid() && !isDraftOrTemplate()) {
			doDeleteEffect(this);
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.classic.Lifecycle#onUpdate(org.hibernate.Session)
	 */

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		if (getStatementRecord() != null) {
			getStatementRecord().getTransactionsLists().add(this);
			session.saveOrUpdate(getStatementRecord());
		}

		// // this.accountTransactionEntriesList.clear();
		//
		// /**
		// * Checking whether this update call is for Voiding the Transaction or
		// * not. If yes then it will Roll back all the effect done by this
		// * Transaction.
		// */
		// if (isBecameVoid()) {
		// doReverseEffect(session, this);
		// }
		// /**
		// * This block is for Editing of the Transaction.
		// */
		// else if (this.isEdited) {
		// doReverseEffect(session, oldTransaction);
		//
		// /**
		// * delete the entries created by the old Transaction
		// */
		// deleteCreatedEntries(session, oldTransaction);
		// session.saveOrUpdate(this);
		// }

		return false;
	}

	// private void doReverseEffect(Session session, Transaction transaction) {
	//
	// FinanceLogger.log("Transaction with No: {0} Type: {1}  going to void,"
	// + "so All effects of this transaction going to rollback",
	// String.valueOf(transaction.getNumber()), Utility
	// .getTransactionName(type));
	//
	// // First undo the previous changes
	// double amount = (isDebitTransaction() ? -1d : 1d) * transaction.total;
	// Account effectingAccount = getEffectingAccount();
	// if (effectingAccount != null) {
	// effectingAccount.updateCurrentBalance(transaction, amount);
	// session.update(effectingAccount);
	// effectingAccount.onUpdate(session);
	// }
	// Payee payee = getPayee();
	// if (payee != null) {
	// payee.updateBalance(session, transaction, amount);
	// }
	//
	// if (transaction.creditsAndPayments != null) {
	// FinanceLogger
	// .log("If transactions has any credits and being used by other transactions(RonUpdateeceivePayment,Paybill) as AppliedCredits,then Rollback credits efftect & delete");
	//
	// for (TransactionCreditsAndPayments tcp : transaction.creditsAndPayments
	// .getTransactionCreditsAndPayments()) {
	// if (tcp.getAmountToUse() > 0) {
	// if (tcp.getTransactionReceivePayment() != null) {
	// tcp.getTransactionReceivePayment()
	// .updateAppliedCredits(tcp.getAmountToUse());
	// } else if (tcp.getTransactionPayBill() != null) {
	// tcp.getTransactionPayBill().updateAppliedCredits(
	// tcp.getAmountToUse());
	// }
	// tcp.setAmountToUse(0.0);
	// }
	//
	// tcp.setCreditsAndPayments(null);
	// session.saveOrUpdate(tcp);
	// }
	// transaction.creditsAndPayments.setCreditAmount(0.0);
	// transaction.creditsAndPayments.setBalance(0.0);
	// transaction.creditsAndPayments.setTransaction(null);
	// }
	//
	// if (transaction.transactionItems != null) {
	// for (TransactionItem ti : transaction.transactionItems) {
	// ti.isVoid = true;
	// }
	// }
	// }

	/**
	 * It can be called while voiding the {@link Transaction}. It's effect
	 * includes clearing all the list entries which are created as a back up at
	 * the time of creation of the Transaction.
	 * 
	 * @param session
	 */

	protected void deleteCreatedEntries(Transaction transaction) {
		if (transaction.accountTransactionEntriesList != null) {
			transaction.accountTransactionEntriesList.clear();
		}

	}

	protected boolean isBecameVoid() {
		return isVoid() && !this.isVoidBefore;
	}

	public boolean isReceivePayment() {

		return this != null && this instanceof ReceivePayment;
	}

	public boolean isPayTax() {

		return this != null && this instanceof PayTAX;
	}

	public abstract boolean isPositiveTransaction();

	public abstract boolean isDebitTransaction();

	public abstract Account getEffectingAccount();

	public abstract Payee getPayee();

	public abstract int getTransactionCategory();

	@Override
	public abstract String toString();

	public abstract Payee getInvolvedPayee();

	public void onEdit(Transaction clonedObject) {
		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if (this.isVoid() && !clonedObject.isVoid()) {
			// voidTransactionItems();
			doDeleteEffect(clonedObject);

		}
		/*
		 * else if (this.transactionItems != null &&
		 * !this.transactionItems.equals(clonedObject.transactionItems)) {
		 * updateTranasactionItems(clonedObject);
		 * deleteCreatedEntries(clonedObject);
		 * clonedObject.transactionItems.clear(); addUpdateHistory(); }
		 */
		else {
			addUpdateHistory();
		}
		if (saveStatus == STATUS_TEMPLATE) {
			updateReminders();
		}
	}

	private void updateReminders() {
		Session session = HibernateUtil.getCurrentSession();
		RecurringTransaction result = (RecurringTransaction) session
				.getNamedQuery("getRecurring.for.template")
				.setLong("templateId", this.getID()).uniqueResult();
		for (Reminder reminder : result.getReminders()) {
			reminder.setValid(this.isValidTransaction());
		}
		session.saveOrUpdate(result);
	}

	private void doDeleteEffect(Transaction clonedObject) {
		double amount = (isDebitTransaction() ? -1d : 1d) * this.total;

		this.updateEffectedAccount(amount);

		this.updatePayee(false);

		this.voidCreditsAndPayments(this);

		addVoidHistory();
		cleanTransactionitems(clonedObject);
		deleteCreatedEntries(clonedObject);
		updateTAxReturnEntries();
	}

	private void updateTAxReturnEntries() {
		Session session = HibernateUtil.getCurrentSession();
		session.getNamedQuery("update.TaxReturnEntry.make.Transaction.null")
				.setLong("transactionId", getID()).executeUpdate();
	}

	protected void voidTransactionItems() {
		if (this.transactionItems != null) {
			for (TransactionItem ti : this.transactionItems) {
				ti.doReverseEffect(HibernateUtil.getCurrentSession());
			}
		}
	}

	/**
	 * Creates an entry in the VATRateCalculation entry for a transactionItem in
	 * the UK company. This makes us to track all the VAT related amount to pay
	 * while making the PayVAT.
	 * 
	 * @param transactionItem
	 * @param session
	 * @return boolean
	 * 
	 */
	public boolean setTAXRateCalculation(TransactionItem transactionItem) {

		if (isBecameVoid()) {
			this.taxRateCalculationEntriesList.clear();
			return false;
		}
		if (transactionItem.getTaxCode() == null) {
			return false;
		}

		TAXCode code = transactionItem.getTaxCode();
		TAXItemGroup taxItemGroup = null;

		if (getTransactionCategory() == Transaction.CATEGORY_CUSTOMER) {
			taxItemGroup = code.getTAXItemGrpForSales();
		} else if (transactionItem.transaction.getTransactionCategory() == Transaction.CATEGORY_VENDOR) {
			taxItemGroup = code.getTAXItemGrpForPurchases();
		}
		if (taxItemGroup == null) {
			return false;
		}

		double lineTotal = transactionItem.isAmountIncludeTAX() ? transactionItem
				.getLineTotal() - transactionItem.getVATfraction()
				: transactionItem.getLineTotal();

		if (taxItemGroup instanceof TAXItem) {
			TAXItem vatItem = ((TAXItem) taxItemGroup);
			addTAXRateCalculation(vatItem, lineTotal, false);
		} else {
			TAXGroup vatGroup = (TAXGroup) taxItemGroup;
			for (TAXItem taxItem : vatGroup.getTAXItems()) {
				addTAXRateCalculation(taxItem, lineTotal, true);
			}
		}

		return false;
	}

	/**
	 * Set the VATItem entry in the VATRateCalculation. This is used to differ
	 * this entry from the VATGroup type entry.
	 * 
	 * @param vatItem
	 * @param transactionItem
	 * @param session
	 */
	public void addTAXRateCalculation(TAXItem vatItem, double lineTotal,
			boolean isGroup) {
		/**
		 * Line total will be positive for all receiving money and negative for
		 * paying money. But for TDS line total is always positive.
		 */
		if (!isPositiveTransaction()
				&& vatItem.getTaxAgency().getTaxType() != TAXAgency.TAX_TYPE_TDS) {
			lineTotal = -lineTotal;
		}

		TAXRateCalculation vc = new TAXRateCalculation(vatItem, this, lineTotal);

		vc.setVATGroupEntry(isGroup);

		getTaxRateCalculationEntriesList().add(vc);

	}

	/**
	 * when you edit transaction, previous transaction items and related objects
	 * has to delete, THis method is to clear lists objects that transaction
	 * item has those no longer need for editing transactions
	 * 
	 * @param coreObject
	 */

	public void cleanTransactionitems(Transaction coreObject) {
		getTaxRateCalculationEntriesList().clear();
	}

	private void updateTranasactionItems(Transaction transaction) {
		for (TransactionItem ti : transaction.transactionItems) {
			// for (int i = 0; i < this.transactionItems.size(); i++) {
			//
			// TransactionItem ti2 = this.transactionItems.get(i);
			//
			// if (ti.id == ti2.id && !ti.equals(ti2)) {
			// if (ti.referringTransactionItem != null)
			// ti.referringTransactionItem.usedamt =
			// ti.referringTransactionItem.lineTotal
			// - ti2.lineTotal;
			//
			// } else if (i == (this.transactionItems.size() - 1)) {
			// if (ti.referringTransactionItem != null)
			// ti.referringTransactionItem.usedamt -= ti.lineTotal;
			//
			// }
			// }
		}

	}

	protected void voidCreditsAndPayments(Transaction transaction) {
		if (this.creditsAndPayments != null) {

			for (TransactionCreditsAndPayments tcp : transaction.creditsAndPayments
					.getTransactionCreditsAndPayments()) {
				if (DecimalUtil.isGreaterThan(tcp.getAmountToUse(), 0)) {
					if (tcp.getTransactionReceivePayment() != null) {
						tcp.getTransactionReceivePayment()
								.updateAppliedCredits(tcp.getAmountToUse(),
										this);
					} else if (tcp.getTransactionPayBill() != null) {
						tcp.getTransactionPayBill().updateAppliedCredits(
								tcp.getAmountToUse(), this);
					}
					tcp.setAmountToUse(0.0);
				}

				tcp.setCreditsAndPayments(null);
				HibernateUtil.getCurrentSession().saveOrUpdate(tcp);
			}
			HibernateUtil.getCurrentSession().delete(this.creditsAndPayments);
			this.setCreditsAndPayments(null);

		}
	}

	protected abstract void updatePayee(boolean onCreate);

	private void updateEffectedAccount(double amount) {
		Account effectingAccount = getEffectingAccount();
		if (effectingAccount != null) {
			effectingAccount.updateCurrentBalance(this, amount, currencyFactor);
			HibernateUtil.getCurrentSession().update(effectingAccount);
			effectingAccount.onUpdate(HibernateUtil.getCurrentSession());
		}
	}

	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		// if (isVoid() || isDeleted()) {
		//
		// throw new AccounterException(
		// AccounterException.ERROR_NO_SUCH_OBJECT);
		// // "This Transaction  is already voided or Deleted, can't Modify");
		// }
		if (creditsAndPayments != null) {
			creditsAndPayments.canEdit(clientObject);
		}
		Transaction transaction = (Transaction) clientObject;
		if (transaction.getSaveStatus() == STATUS_DRAFT) {
			return true;
		}
		// checkForReconciliation(transaction);
		if (saveStatus != STATUS_DRAFT) {
			checkNullValues();
		}
		// if (isVoid() /* && !getReconciliationItems().isEmpty() */) {
		// throw new AccounterException(
		// AccounterException.ERROR_VOIDING_TRANSACTION_RECONCILIED);
		// }

		return true;
	}

	protected void checkForReconciliation(Transaction transaction)
			throws AccounterException {
		if (reconciliationItems == null || reconciliationItems.isEmpty()) {
			return;
		}
		Map<Account, Double> map = getEffectingAccountsWithAmounts();
		if (map.size() == 0) {
			throw new AccounterException(
					AccounterException.ERROR_THERE_IS_NO_TRANSACTION_ITEMS);
		}
		for (ReconciliationItem item : reconciliationItems) {
			Account reconciliedAccount = item.getReconciliation().getAccount();
			for (Account account : map.keySet()) {
				if (reconciliedAccount.getID() == account.getID()) {
					Double presentAmount = map.get(account);
					double amount = item.getAmount();
					if (DecimalUtil.isLessThan(amount, 0.00D)) {
						amount *= -1;
					}
					if (!DecimalUtil.isEquals(presentAmount, amount)) {
						throw new AccounterException(
								AccounterException.ERROR_EDITING_TRANSACTION_RECONCILIED);
					}
				}
			}
		}
	}

	public void setRecurringTransaction(
			RecurringTransaction recurringTransaction) {
		this.recurringTransaction = recurringTransaction;
	}

	public RecurringTransaction getRecurringTransaction() {
		return recurringTransaction;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public Transaction clone() throws CloneNotSupportedException {
		Transaction clone = (Transaction) super.clone();

		// cloned should be a template.
		clone.setId(0);

		List<TransactionItem> items = new ArrayList<TransactionItem>();
		if (transactionItems != null) {
			for (TransactionItem transactionItem : transactionItems) {
				items.add(transactionItem.clone());
			}
		}
		clone.setTransactionItems(items);

		clone.setRecurringTransaction(null);

		return clone;
	}

	public void resetID() {
		setId(0);
	}

	public AccounterClass getAccounterClass() {
		return accounterClass;
	}

	public void setAccounterClass(AccounterClass accounterClass) {
		this.accounterClass = accounterClass;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(int saveStatus) {
		this.saveStatus = saveStatus;
	}

	public boolean isDraftOrTemplate() {
		return this.saveStatus == STATUS_DRAFT
				|| this.saveStatus == STATUS_TEMPLATE;
	}

	/**
	 * @return the lastActivity
	 */
	public Activity getLastActivity() {
		return lastActivity;
	}

	/**
	 * @param lastActivity
	 *            the lastActivity to set
	 */
	public void setLastActivity(Activity lastActivity) {
		this.lastActivity = lastActivity;
	}

	public boolean addAccountTransaction(AccountTransaction accountTransaction) {
		if (isBecameVoid()) {
			getAccountTransactionEntriesList().clear();
			return false;
		}
		AccountTransaction similar = getSimilarAccountTransaction(accountTransaction);
		if (similar == null) {
			getAccountTransactionEntriesList().add(accountTransaction);
			return true;
		} else {
			getAccountTransactionEntriesList().remove(similar);
			Session session = HibernateUtil.getCurrentSession();
			session.delete(similar);
			return false;
		}
	}

	private AccountTransaction getSimilarAccountTransaction(
			AccountTransaction accountTransaction) {
		for (AccountTransaction record : getAccountTransactionEntriesList()) {
			if (record.getTransaction().getID() == accountTransaction
					.getTransaction().getID()
					&& record.getAccount().getID() == accountTransaction
							.getAccount().getID()
					&& record.getAmount() == -1
							* accountTransaction.getAmount()) {
				return record;
			}
		}
		return null;
	}

	/**
	 * @return the reconciliationItems
	 */
	public Set<ReconciliationItem> getReconciliationItems() {
		return reconciliationItems;
	}

	/**
	 * @param reconciliationItems
	 *            the reconciliationItems to set
	 */
	public void setReconciliationItems(
			Set<ReconciliationItem> reconciliationItems) {
		this.reconciliationItems = reconciliationItems;
	}

	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = new HashMap<Account, Double>();
		if (getEffectingAccount() != null) {
			map.put(getEffectingAccount(), total);
		}
		if (getPayee() != null) {
			map.put(getPayee().getAccount(),
					type == Transaction.TYPE_PAY_BILL ? this.subTotal
							: this.total);
		}
		for (TransactionItem item : transactionItems) {
			map.put(item.getEffectingAccount(), item.getEffectiveAmount());
		}
		// for (TransactionDepositItem item : getTransactionDepositItems()) {
		// map.put(item.getAccount(), item.getTotal());
		// }
		return map;
	}

	public List<TransactionLog> getHistory() {
		return this.history;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getCurrencyFactor() {
		return currencyFactor;
	}

	public void setCurrencyFactor(double currencyFactor) {
		this.currencyFactor = currencyFactor;
	}

	public List<WareHouseAllocation> getWareHouseAllocations() {
		return wareHouseAllocations;
	}

	public void setWareHouseAllocations(
			List<WareHouseAllocation> wareHouseAllocations) {
		this.wareHouseAllocations = wareHouseAllocations;
	}

	public Set<TAXRateCalculation> getTaxRateCalculationEntriesList() {
		return taxRateCalculationEntriesList;
	}

	public void setTaxRateCalculationEntriesList(
			Set<TAXRateCalculation> taxRateCalculationEntriesList) {
		this.taxRateCalculationEntriesList = taxRateCalculationEntriesList;
	}

	public void setHistory(List<TransactionLog> history) {
		this.history = history;
	}

	protected void checkingCustomerNull(Customer customer)
			throws AccounterException {
		if (customer == null) {
			throw new AccounterException(AccounterException.ERROR_CUSTOMER_NULL);
		}
	}

	protected void chekingTaxCodeNull(TAXCode taxCode)
			throws AccounterException {
		if (taxCode == null) {
			throw new AccounterException(AccounterException.ERROR_TAX_CODE_NULL);
		}
	}

	protected void checkAccountNull(Account account) throws AccounterException {
		if (account == null) {
			throw new AccounterException(AccounterException.ERROR_ACCOUNT_NULL);
		}
	}

	protected void checkPaymentMethodNull() throws AccounterException {
		if (paymentMethod == null) {
			throw new AccounterException(
					AccounterException.ERROR_PAYMENT_METHOD_NULL);
		}
	}

	protected void checkingTotal0() throws AccounterException {
		// if (total == 0) {
		// throw new AccounterException(AccounterException.ERROR_AMOUNT_ZERO);
		// }
	}

	protected void checkingVendorNull(Vendor vendor) throws AccounterException {
		if (vendor == null) {
			throw new AccounterException(AccounterException.ERROR_VENDOR_NULL);
		}
	}

	/**
	 * This method will check and tell you whether this transaction is valid or
	 * not.
	 * 
	 * Need to override this method in individual transaction to check.
	 * 
	 * @return valid transaction or not
	 */
	public boolean isValidTransaction() {
		boolean valid = true;
		if (isInPreventPostingBeforeDate(transactionDate)) {
			valid = false;
		} else if (DecimalUtil.isEquals(currencyFactor, 0)) {
			valid = false;
		} else if (!DecimalUtil.isGreaterThan(total, 0)) {
			valid = false;
		}
		return valid;
	}

	protected boolean isInPreventPostingBeforeDate(FinanceDate transactionDate) {
		FinanceDate postingBeforeDate = getCompany().getPreferences()
				.getPreventPostingBeforeDate();
		if (postingBeforeDate != null) {
			return transactionDate.before(postingBeforeDate);
		} else {
			return false;
		}
	}

	public boolean isAutomaticTransaction() {
		return isAutomaticTransaction;
	}

	public void setAutomaticTransaction(boolean isAutomaticTransaction) {
		this.isAutomaticTransaction = isAutomaticTransaction;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Reminder getTobeDeleteReminder() {
		return tobeDeleteReminder;
	}

	public void setTobeDeleteReminder(Reminder tobeDeleteReminder) {
		this.tobeDeleteReminder = tobeDeleteReminder;
	}

	public boolean isTemplate() {
		return this.saveStatus == STATUS_TEMPLATE;
	}

	public boolean isDraft() {
		return this.saveStatus == STATUS_DRAFT;
	}

	protected boolean isCurrencyFactorChanged() {
		return currencyFactor != previousCurrencyFactor;
	}

	public StatementRecord getStatementRecord() {
		return statementRecord;
	}

	public void setStatementRecord(StatementRecord statementRecord) {
		this.statementRecord = statementRecord;
	}

	public boolean isValidated() {
		return isValidated;
	}

	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}

	public List<Item> getInventoryUsed() {

		List<Item> inventory = new ArrayList<Item>();
		if (this.isDraftOrTemplate() || (isVoid() && this.isVoidBefore)) {
			return inventory;
		}
		for (TransactionItem tItem : getTransactionItems()) {
			if (tItem.getType() != TransactionItem.TYPE_ITEM
					|| (tItem.getItem().getType() != Item.TYPE_INVENTORY_PART && tItem
							.getItem().getType() != Item.TYPE_INVENTORY_ASSEMBLY)) {
				continue;
			}
			if (!inventory.contains(tItem.getItem())) {
				inventory.add(tItem.getItem());
			}
		}
		return inventory;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
}
