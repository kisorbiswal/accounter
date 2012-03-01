package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * 
 * @author Suresh Garikapati
 * 
 *         Cash Purchase
 * 
 *         Effect on Transaction Item:
 * 
 *         ===============================> Item
 * 
 *         If Item's ExpenseAccount isIncrease true then Decrease the current
 *         and total balance by line total otherwise Increase.
 * 
 * 
 *         ===============================> Account
 * 
 *         If Account isIncrease true then Decrease the current and total
 *         balance by line total otherwise Increase.
 * 
 *         Effect on Pay From Account:
 * 
 *         If Specified PayFrom Account(Cash|Bank|Credit Card|Other Current
 *         Liability|LongTermLiability) isIncrease true then Increase the
 *         current and total balance by Cash Purchase total otherwise Decrease
 */
public class CashPurchase extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -304718612318704134L;

	public static final int EMPLOYEE_EXPENSE_STATUS_SAVE = 0;
	public static final int EMPLOYEE_EXPENSE_STATUS_DELETE = 1;
	public static final int EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL = 2;
	public static final int EMPLOYEE_EXPENSE_STATUS_APPROVED = 3;
	public static final int EMPLOYEE_EXPENSE_STATUS_DECLINED = 4;
	public static final int EMPLOYEE_EXPENSE_STATUS_NOT_TO_SHOW = 5;

	/**
	 * The payee from whom we are making the cash purchase.
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * The Account to what we are making the Cash Expense.
	 */
	Account cashExpenseAccount;

	User employee;
	/**
	 * The contact of the payee. (His alternate phone number, email, primary
	 * address etc are entered
	 */

	Contact contact;

	/**
	 * This is the address from where we are making the purchase. This is
	 * different from payee's primary address.
	 */
	Address vendorAddress;
	/**
	 * This is an optional field where the payee can give his primary phone
	 * number.
	 */
	String phone;

	/**
	 * The account from where the amount for cash purchase is paid from.
	 */
	@ReffereredObject
	Account payFrom;

	/**
	 * If we want to make the purchase through cash, we can give the check
	 * number here.
	 */
	String checkNumber;

	/**
	 * The date on/before which the delivery of the good should be done.
	 */
	FinanceDate deliveryDate;

	/*
	 * It specifies the employee expense status
	 */
	int expenseStatus;

	//

	/**
	 * @return the cashExpenseAccount
	 */
	public Account getCashExpenseAccount() {
		return cashExpenseAccount;
	}

	/**
	 * @param cashExpenseAccount
	 *            the cashExpenseAccount to set
	 */
	public void setCashExpenseAccount(Account cashExpenseAccount) {
		this.cashExpenseAccount = cashExpenseAccount;
	}

	public CashPurchase() {
		super();
		setType(Transaction.TYPE_CASH_PURCHASE);
	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the employee
	 */
	public User getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            the employee to set
	 */
	public void setEmployee(User employee) {
		this.employee = employee;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the vendorAddress
	 */
	public Address getVendorAddress() {
		return vendorAddress;
	}

	/**
	 * @param vendorAddress
	 *            the vendorAddress to set
	 */
	public void setVendorAddress(Address vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the payFrom
	 */
	public Account getPayFrom() {
		return payFrom;
	}

	/**
	 * @param payFrom
	 *            the payFrom to set
	 */
	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
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
	// public void setCheckNumber(String checkNumber) {
	// this.checkNumber = checkNumber;
	// }
	/**
	 * @return the deliveryDate
	 */
	public FinanceDate getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDa@Overridete to set
	 */
	// public void setDeliveryDate(Date deliveryDate) {
	// this.deliveryDate = deliveryDate;
	// }
	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		return this.payFrom;
	}

	@Override
	public Payee getPayee() {
		return null;
	}

	@Override
	public String toString() {

		return AccounterServerConstants.TYPE_CASH_PURCHASE;

	}

	// public boolean equals(CashPurchase cp) {
	// if (this.vendor.equals(cp.vendor) && this.contact.equals(cp.contact)
	// && this.vendorAddress.equals(cp.vendorAddress)
	// && this.phone.equals(cp.phone)
	// && this.paymentMethod.equals(cp.paymentMethod)
	// && this.payFrom.equals(cp.payFrom)
	// && this.checkNumber.equals(cp.checkNumber)
	// && this.deliveryDate.equals(cp.deliveryDate)
	// && this.memo.equals(cp.memo)
	// && this.reference.equals(cp.reference))
	// return true;
	//
	// return false;
	// }

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		super.onSave(session);
		isOnSaveProccessed = true;

		if (this.type == Transaction.TYPE_EMPLOYEE_EXPENSE
				&& this.expenseStatus != CashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			return false;

		/**
		 * update status if payment method is check, it will used to get list of
		 * cash purchase for issue payment transaction
		 */

		if (!(this.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
				&& !(this.paymentMethod
						.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}

		return false;
	}

	@Override
	protected void checkNullValues() throws AccounterException {
		// checkingVendorNull(vendor);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		super.onUpdate(session);
		//
		// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		//
		// /**
		// * update and void transactions items this transaction becomes void.
		// */
		// if (isBecameVoid()) {
		// if (this.transactionItems != null) {
		// for (TransactionItem ti : this.transactionItems) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }
		// }
		return false;

	}

	@Override
	public Payee getInvolvedPayee() {

		return this.getVendor();
	}

	@Override
	public void onEdit(Transaction clonedObject) {

		CashPurchase cashPurchase = (CashPurchase) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (this.type == Transaction.TYPE_EMPLOYEE_EXPENSE
				&& this.expenseStatus != CashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			return;

		if (isDraftOrTemplate()) {
			super.onEdit(cashPurchase);
			return;
		}

		/**
		 * 
		 * if present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if (this.isVoid() && !cashPurchase.isVoid()) {

		} else {

			this.cleanTransactionitems(this);

			if ((this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
			} else {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

			/**
			 * To get the payFrom Account of clonedObject cashPurchase
			 */
			if (this.type != Transaction.TYPE_EMPLOYEE_EXPENSE
					|| (this.type == Transaction.TYPE_EMPLOYEE_EXPENSE && this.expenseStatus == cashPurchase.expenseStatus)) {

				Account payFromAccount = (Account) session.get(Account.class,
						cashPurchase.payFrom.getID());

				/**
				 * Updating the balance values of present and previous accounts
				 * of vendors
				 */
				payFromAccount.updateCurrentBalance(this,
						isDebitTransaction() ? -cashPurchase.total
								: cashPurchase.total, cashPurchase
								.getCurrencyFactor());
				payFromAccount.onUpdate(session);
			}

			this.payFrom.updateCurrentBalance(this,
					isDebitTransaction() ? this.total : -this.total,
					this.currencyFactor);
			this.payFrom.onUpdate(session);

		}

		super.onEdit(cashPurchase);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		Transaction transaction = (Transaction) clientObject;
		if (transaction.getSaveStatus() == Transaction.STATUS_DRAFT) {
			User user = AccounterThreadLocal.get();
			if (user.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
				return true;
			}
		}

		if (!UserUtils.canDoThis(CashPurchase.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		return super.canEdit(clientObject);
	}

	/**
	 * @return the expenseStatus
	 */
	public int getExpenseStatus() {
		return expenseStatus;
	}

	/**
	 * @param expenseStatus
	 *            the expenseStatus to set
	 */
	public void setExpenseStatus(int expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.cashPurchase()).gap();

		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();

		if (getVendor() != null) {
			w.put(messages.Vendor(), this.getVendor().getName());
		}

		w.put(messages.currency(), this.currencyFactor).gap();

		w.put(messages.amount(), this.total).gap();

		if (this.vendorAddress != null) {
			w.put(messages.address(), this.vendorAddress.address1
					+ this.vendorAddress.stateOrProvinence);
		}
		w.put(messages.paymentMethod(), this.paymentMethod).gap();

		if (this.cashExpenseAccount != null)
			w.put(messages.cashExpense() + " " + messages.account(),
					this.cashExpenseAccount.getName()).gap();

		if (this.employee != null)
			w.put(messages.paymentMethod(), this.employee.getName());

		if (this.contact != null)
			w.put(messages.paymentMethod(), this.contact.getName()).gap();
		if (this.vendorAddress != null)
			w.put(messages.paymentMethod(), this.vendorAddress.toString());

		w.put(messages.paymentMethod(), this.phone).gap();
		if (this.payFrom != null)
			w.put(messages.paymentMethod(), this.payFrom.toString());

		w.put(messages.paymentMethod(), this.checkNumber).gap();

		if (this.deliveryDate != null)
			w.put(messages.paymentMethod(), this.deliveryDate.toString());

		w.put(messages.paymentMethod(), this.expenseStatus).gap();
	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (payFrom == null) {
			valid = false;
		} else if (paymentMethod == null || paymentMethod.isEmpty()) {
			valid = false;
		} else if (transactionItems != null && !transactionItems.isEmpty()) {
			for (TransactionItem item : transactionItems) {
				if (!item.isValid()) {
					valid = false;
					break;
				}
			}
		} else {
			valid = false;
		}
		return valid;
	}

	public void setDeliveryDate(FinanceDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@Override
	protected void updatePayee(boolean onCreate) {

	}
}
