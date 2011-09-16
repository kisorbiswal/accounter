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
 *         Credit Card Charge (Effect is similar to Cash Purchase)
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
public class CreditCardCharge extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is the {@link Vendor} to whom we are creating This CreditCardCharge
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * This is the one of the chosen {@link Contact} of the {@link Vendor}
	 */
	Contact contact;

	/**
	 * This is the chosen {@link Vendor} Address among all the address of Vendor
	 * 
	 * @see Address
	 * 
	 */
	Address vendorAddress;

	/**
	 * This defaults to the chosen Vendor's primary contact Business Phone
	 * number.
	 */
	String phone;

	/**
	 * This is the Account through which we are creating this CreditCardCharge
	 */
	@ReffereredObject
	Account payFrom;

	/**
	 * This will hold user given value.
	 */
	String checkNumber;

	/**
	 * The FinanceDate on which these Products should be delivered.
	 */
	FinanceDate deliveryDate;

	//

	public CreditCardCharge() {

	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @return the vendorAddress
	 */
	public Address getVendorAddress() {
		return vendorAddress;
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
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * @return the deliveryDate
	 */
	public FinanceDate getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @return the isDeposited
	 */
	@Override
	public boolean getIsDeposited() {
		return isDeposited;
	}

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

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_CREDIT_CARD_CHARGE;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		isOnSaveProccessed = true;
		super.onSave(session);

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
		// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		// if (this.transactionItems != null) {
		// for (TransactionItem ti : this.transactionItems) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }
		return false;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.vendor;
	}

	public boolean equals(CreditCardCharge obj) {
		if (((this.vendor != null && obj.vendor != null) ? (this.vendor.id == obj.vendor.id)
				: true)
				&& ((this.payFrom != null && obj.payFrom != null) ? (this.payFrom
						.equals(obj.payFrom)) : true)
				&& ((this.paymentMethod != null && obj.paymentMethod != null) ? (this.paymentMethod
						.equals(obj.paymentMethod)) : true)
				&& ((!DecimalUtil.isEquals(this.total, 0.0) && !DecimalUtil
						.isEquals(obj.total, 0.0)) ? (DecimalUtil.isEquals(
						this.total, obj.total)) : true)
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

		Session session = HibernateUtil.getCurrentSession();

		CreditCardCharge creditCardCharge = (CreditCardCharge) clonedObject;

		if ((this.isVoid && !creditCardCharge.isVoid)
				|| (this.isDeleted() && !creditCardCharge.isDeleted())) {

			// if (this.status != Transaction.STATUS_DELETED)
			// this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;

		} else if (!this.equals(creditCardCharge)) {

			if (!this.payFrom.equals(creditCardCharge.payFrom)) {
				Account prePayFrom = (Account) session.get(Account.class,
						creditCardCharge.payFrom.id);
				prePayFrom.updateCurrentBalance(this,
						-creditCardCharge.total);
				prePayFrom.onUpdate(session);
				this.payFrom.updateCurrentBalance(this, this.total);
				payFrom.onUpdate(session);

			} else if (this.payFrom.equals(creditCardCharge.payFrom)) {
				this.payFrom.updateCurrentBalance(this, this.total
						- creditCardCharge.total);
				// session.update(this.payFrom);
				this.payFrom.onUpdate(session);

			}
			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

			if ((creditCardCharge.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || creditCardCharge.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))
					&& (!this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) && !this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}
		}

		super.onEdit(clonedObject);

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return super.canEdit(clientObject);

	}

}
