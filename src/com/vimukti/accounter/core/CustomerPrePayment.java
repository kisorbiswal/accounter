package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class CustomerPrePayment extends Transaction {

	private static final long serialVersionUID = -4118462182877133386L;

	/**
	 * customer for this CustomerPrePayment Transaction
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * {@link account}, associated With this CustomerPrePayment
	 */

	Account depositIn;

	double endingBalance = 0D;

	double customerBalance = 0D;

	double balanceDue = 0D;

	/**
	 * {@link Address} of the customer Selected, for CustomerPrePayment
	 * Transaction
	 */
	Address address;

	private String checkNumber;

	/**
	 * List of {@link TransactionCustomerPrePayment}'s
	 */
	List<TransactionCreditsAndPayments> transactionCreditsAndPayments;

	//

	public CustomerPrePayment() {

	}

	/**
	 * @return the customerBalance
	 */
	public double getCustomerBalance() {
		return customerBalance;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return depositIn;
	}

	/**
	 * @return the endingBalance
	 */
	public double getEndingBalance() {
		return endingBalance;
	}

	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public void setCustomerBalance(double customerBalance) {
		this.customerBalance = customerBalance;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;

		super.onSave(session);

		// Inserting this Customer prePayment entry in to Credits And Payments
		// table.
		if (DecimalUtil.isGreaterThan(this.getTotal(), 0.0)) {
			this.balanceDue = this.total;
			if (creditsAndPayments != null
					&& DecimalUtil.isEquals(creditsAndPayments.creditAmount,
							0.0d)) {
				creditsAndPayments.update(this);
				this.setCreditsAndPayments(creditsAndPayments);
			} else {
				creditsAndPayments = new CreditsAndPayments(this);
			}
			this.setCreditsAndPayments(creditsAndPayments);
			session.save(creditsAndPayments);
		}
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
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		return this.depositIn;
	}

	@Override
	public Payee getPayee() {
		return null;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_CUSTOMER_PRE_PAYMENT;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;

	}

	public void setAccount(Account account) {
		this.depositIn = account;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public Payee getInvolvedPayee() {
		return this.customer;
	}

	public boolean equals(CustomerPrePayment obj) {
		if (((this.depositIn != null && obj.depositIn != null) ? (this.depositIn.id == obj.depositIn.id)
				: true)
				&& ((this.customer != null && obj.customer != null) ? (this.customer
						.equals(obj.customer)) : true)
				&& ((this.paymentMethod != null && obj.paymentMethod != null) ? (this.paymentMethod
						.equals(obj.paymentMethod)) : true)
				&& ((!DecimalUtil.isEquals(this.total, 0.0) && !DecimalUtil
						.isEquals(obj.total, 0.0)) ? DecimalUtil.isEquals(
						this.total, obj.total) : true)) {
			return true;
		}
		return false;
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {
		super.onLoad(session, arg1);
	}

	@Override
	public void onEdit(Transaction clonedObject) {

		Session session = HibernateUtil.getCurrentSession();
		CustomerPrePayment customerPrePayment = (CustomerPrePayment) clonedObject;
		if ((this.isVoid && !clonedObject.isVoid)
				|| (this.isDeleted() && !clonedObject.isDeleted() && !this.isVoid)) {

		} else if (!this.equals(customerPrePayment)) {

			/**
			 * Checking that whether two customers are same are not. If they are
			 * not same then update clonedObject customer and New customer
			 * balances same customers.
			 */
			if (customerPrePayment.customer.id != this.customer.id) {

				voidCreditsAndPayments(customerPrePayment);

				if (creditsAndPayments != null
						&& DecimalUtil.isEquals(
								creditsAndPayments.creditAmount, 0.0d)) {
					creditsAndPayments.update(this);
				} else {
					creditsAndPayments = new CreditsAndPayments(this);
				}
				this.setCreditsAndPayments(creditsAndPayments);
				session.save(creditsAndPayments);

			} else if (!DecimalUtil.isEquals(customerPrePayment.total,
					this.total)) {

				this.customer.updateBalance(session, this,
						this.total - customerPrePayment.total);
				this.creditsAndPayments.updateCreditPayments(this.total);

			}
			if (!this.depositIn.equals(customerPrePayment.depositIn)) {

				Account depositInAccount = (Account) session.get(Account.class,
						customerPrePayment.depositIn.id);
				depositInAccount.updateCurrentBalance(this,
						customerPrePayment.total);
				depositInAccount.onUpdate(session);
				this.depositIn.updateCurrentBalance(this, -this.total);
				this.depositIn.onUpdate(session);

			} else {
				this.depositIn.updateCurrentBalance(this,
						customerPrePayment.total - this.total);
				this.depositIn.onUpdate(session);

			}
		}
		this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

		if ((customerPrePayment.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || customerPrePayment.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))
				&& (!this.paymentMethod
						.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) && !this.paymentMethod
						.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		}

		super.onEdit(customerPrePayment);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return super.canEdit(clientObject);
	}

}
