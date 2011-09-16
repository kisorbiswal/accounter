package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

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

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
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

		// super.onSave(session);
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

	// @Override
	public boolean equals(CashPurchase obj) {
		if (((this.vendor != null && obj.vendor != null) ? (this.vendor.id == obj.vendor.id)
				: true)
				&& ((this.cashExpenseAccount != null && obj.cashExpenseAccount != null) ? (this.cashExpenseAccount.id == obj.cashExpenseAccount.id)
						: true)
				&& ((this.employee != null && obj.employee != null) ? (this.employee
						.equals(obj.employee)) : true)

				&& ((this.payFrom != null && obj.payFrom != null) ? (this.payFrom
						.equals(obj.payFrom)) : true)
				&& ((this.paymentMethod != null && obj.paymentMethod != null) ? (this.paymentMethod
						.equals(obj.paymentMethod)) : true)
				&& ((!DecimalUtil.isEquals(this.total, 0.0) && !DecimalUtil
						.isEquals(obj.total, 0.0)) ? DecimalUtil.isEquals(
						this.total, obj.total) : true)

				&& (this.type == Transaction.TYPE_EMPLOYEE_EXPENSE && this.expenseStatus == obj.expenseStatus)

				&& this.transactionItems.size() == obj.transactionItems.size()) {

			for (int i = 0; i < this.transactionItems.size(); i++) {
				if (!this.transactionItems.get(i).equals(
						obj.transactionItems.get(i))) {
					return false;
				}
			}

			return true;
		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

		CashPurchase cashPurchase = (CashPurchase) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (this.type == Transaction.TYPE_EMPLOYEE_EXPENSE
				&& this.expenseStatus != CashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			return;

		/**
		 * 
		 * if present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if ((this.isVoid && !cashPurchase.isVoid)
				|| (this.isDeleted() && !cashPurchase.isDeleted() && !this.isVoid)) {

		} else if (!this.equals(cashPurchase)) {

			this.cleanTransactionitems(this);

			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

			if ((cashPurchase.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || cashPurchase.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))
					&& (!this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) && !this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

			/**
			 * To get the payFrom Account of clonedObject cashPurchase
			 */
			if (this.type != Transaction.TYPE_EMPLOYEE_EXPENSE
					|| (this.type == Transaction.TYPE_EMPLOYEE_EXPENSE && this.expenseStatus == cashPurchase.expenseStatus)) {

				Account payFromAccount = (Account) session.get(Account.class,
						cashPurchase.payFrom.id);

				/**
				 * Updating the balance values of present and previous accounts
				 * of vendors
				 */
				payFromAccount.updateCurrentBalance(this,
						isDebitTransaction() ? -cashPurchase.total
								: cashPurchase.total);
				payFromAccount.onUpdate(session);
			}

			this.payFrom.updateCurrentBalance(this,
					isDebitTransaction() ? this.total : -this.total);
			this.payFrom.onUpdate(session);

		}

		super.onEdit(cashPurchase);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

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

}
