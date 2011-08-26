package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * @author Suresh Garikapati <br>
 *         <b>Customer Credit Memo </b><br>
 * <br>
 *         <b><i>Effect on Transaction Item </i></b><br>
 * <br>
 *         ===============================>Item <br>
 * 
 *         If Item's IncomeAccount isIncrease true then Decrease the current and
 *         total balance by line total otherwise Increase.<br>
 * 
 * <br>
 *         ===============================>Account <br>
 * 
 *         If Account isIncrease true then Decrease the current and total
 *         balance by line total otherwise Increase.<br>
 * 
 * <br>
 *         ===============================>Sales Tax <br>
 * 
 *         If Specified TaxCode's all Tax Agency's Liability Account isIncrease
 *         true then Decrease the current and total balance by line total
 *         otherwise Increase. Specified TaxCode's all Tax Agency's Balance will
 *         Decrease by line total .<br>
 * <br>
 *         <b><i>Effect on Tax Group</i></b><br>
 * 
 *         Specified TaxGroup's all TaxCode's Tax Agency's Liability Account
 *         isIncrease true then Decrease the current and total balance by sales
 *         tax collected for each taxcode otherwise Increase. Specified
 *         TaxGroup's TaxCode's all Tax Agency's Balance will Decrease by by
 *         sales tax collected for each taxcode.<br>
 * <br>
 *         <b><i>Other Effect:</i></b><br>
 * 
 *         Accounts Receivable account current and total balance will Decrease
 *         by the CCM total. Customer balance should Decrease by the CCM total.<br>
 * 
 * <br>
 *         <b><i>Effect on CreditsAndPayments:</i></b><br>
 * 
 *         New Credit and Payment is created for this customer with Credit
 *         Amount and Balance as CCM total and Memo as transaction number
 *         followed byCustomerCreditMemo
 * 
 * 
 */

/**
 * @author vimukti16
 * 
 */
public class CustomerCreditMemo extends Transaction implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2197852881832579241L;

	/**
	 * The Customer to whom we are crediting this amount.
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * This is the one of the chosen {@link Contact} of the {@link Customer}
	 */
	Contact contact;

	/**
	 * This is the chosen {@link Customer} Address among all the address of
	 * Customer
	 * 
	 * @see Address
	 * 
	 */
	Address billingAddress;

	/**
	 * This defaults to the chosen Customer's primary contact Business Phone
	 * number.
	 * 
	 * @see Customer
	 * @see Contact
	 */
	String phone;

	SalesPerson salesPerson;
	@ReffereredObject
	Account account;

	/**
	 * This will Decrease or Increase the Price of the Item selected in this
	 * Transaction. The default value of this Price level is default's to the
	 * chosen {@link Customer} User can choose any Price Level later
	 * 
	 * @see Item
	 */
	PriceLevel priceLevel;

	/**
	 * The total Sales Tax collected on the whole Transaction.
	 */
	double salesTax = 0D;

	/**
	 * The total of the discounts applied on Each {@link TransactionItem}
	 */
	double discountTotal = 0D;

	double balanceDue = 0D;

	//

	public CustomerCreditMemo() {
	}

	public CustomerCreditMemo(Session session,
			ClientCustomerCreditMemo customercreditmemo) {
		this.type = Transaction.TYPE_CUSTOMER_CREDIT_MEMO;

	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @return the billingAddress
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the salesPerson
	 */
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the priceLevel
	 */
	public PriceLevel getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @return the salesTax
	 */
	public double getSalesTax() {
		return salesTax;
	}

	/**
	 * @return the allLineTotal
	 */
	public double getAllLineTotal() {
		return subTotal;
	}

	/**
	 * @return the allTaxableLineTotal
	 */
	public double getAllTaxableLineTotal() {
		return totalTaxableAmount;
	}

	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal() {
		return discountTotal;
	}

	@Override
	public void setTransactionItems(List<TransactionItem> transactionItems) {
		super.setTransactionItems(transactionItems);

		// this.allLineTotal = getLineTotalSum();
		// this.allTaxableLineTotal = getTaxableLineTotalSum();
		this.discountTotal = getDiscountTotalSum();

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);

		// Inserting this Customer Credit Memo entry in to Credits And Payments
		// table.
		if (DecimalUtil.isGreaterThan(this.getTotal(), 0.0)) {
			this.balanceDue = this.total;
			if (creditsAndPayments != null
					&& DecimalUtil.isEquals(creditsAndPayments.creditAmount,
							0.0d)) {
				creditsAndPayments.update(this);
			} else {
				creditsAndPayments = new CreditsAndPayments(this);
			}
			this.setCreditsAndPayments(creditsAndPayments);
			session.save(creditsAndPayments);
		}

		return false;

	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		//
		// if (isBecameVoid()) {
		// // this.creditsAndPayments.setTransaction(null);
		// // session.delete(this.creditsAndPayments);
		// this.creditsAndPayments = null;
		// this.balanceDue = 0.0;
		// }
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
	public void onLoad(Session session, Serializable arg1) {
		// NOTHING TO DO
		super.onLoad(session, arg1);
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
		return null;
	}

	@Override
	public Payee getPayee() {
		// return this.customer;
		return null;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
	}

	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setSalesTax(double salesTax) {
		this.salesTax = salesTax;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setDiscountTotal(double discountTotal) {
		this.discountTotal = discountTotal;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;

	}

	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_CUSTOMER_CREDIT_MEMO;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.customer;
	}

	// @Override
	public boolean equals(CustomerCreditMemo ccm) {
		if (DecimalUtil.isEquals(this.getTotal(), ccm.getTotal())
				&& this.id == ccm.id
				&& DecimalUtil.isEquals(this.total, ccm.total)
				// && (this.transactionDate != null && ccm.transactionDate !=
				// null) ? (this.transactionDate.equals(transactionDate)): true
				&& ((this.customer != null && ccm.customer != null) ? (this.customer
						.equals(ccm.customer)) : true)) {
			for (int i = 0; i < this.transactionItems.size(); i++) {
				if (!this.transactionItems.get(i).equals(
						ccm.transactionItems.get(i)))
					return false;
			}
			return true;
		}
		return false;
	}

	// @Override
	// public boolean canEdit(IAccounterServerCore clientObject)
	// throws InvalidOperationException {
	// //
	// return false;
	// }

	@Override
	public void onEdit(Transaction clonedObject) {

		CustomerCreditMemo customerCreditMemo = (CustomerCreditMemo) clonedObject;
		Session session = HibernateUtil.getCurrentSession();
		//
		// Customer cust = (Customer) session.get(Customer.class,
		// customerCreditMemo.customer.id);
		// cust.updateBalance(session, clonedObject, -customerCreditMemo.total);
		// session.saveOrUpdate(cust);

		if (!this.equals(customerCreditMemo)) {

			this.cleanTransactionitems(this);

			/**
			 * Checking that whether two customers are same are not. If they are
			 * not same then update clonedObject customer and New customer
			 * balances same customers.
			 */

			if (!this.customer.equals(customerCreditMemo.customer)) {

				voidCreditsAndPayments(customerCreditMemo);

				if (creditsAndPayments != null
						&& DecimalUtil.isEquals(
								creditsAndPayments.creditAmount, 0.0d)) {
					creditsAndPayments.update(this);

				} else {
					creditsAndPayments = new CreditsAndPayments(this);
				}
				this.setCreditsAndPayments(creditsAndPayments);
				session.save(creditsAndPayments);

			}
			if ((this.customer.equals(customerCreditMemo.customer))
					&& (!DecimalUtil.isEquals(this.total,
							customerCreditMemo.total))) {
				// updateCreditPayments
				// if (this.total > customerCreditMemo.total) {
				// this.total += this.total - customerCreditMemo.total;
				// } else if (this.total < customerCreditMemo.total) {
				// this.total -= customerCreditMemo.total - this.total;
				// }

				// Double dueAmount = this.total;
				// for (TransactionCreditsAndPayments tcp :
				// this.creditsAndPayments.transactionCreditsAndPayments) {
				// if (tcp.getTransactionReceivePayment() != null) {
				// this.creditsAndPayments.updateCreditPayments(dueAmount);
				// }
				// HibernateUtil.getCurrentSession().saveOrUpdate(tcp);
				// }

				if (DecimalUtil
						.isLessThan(this.total, customerCreditMemo.total)) {
					this.customer.updateBalance(session, this, this.total
							- customerCreditMemo.total);
					this.creditsAndPayments.updateCreditPayments(this.total);
				} else {
					// this.total = this.total - customerCreditMemo.total;
					this.customer.updateBalance(session, this, this.total
							- customerCreditMemo.total);
					this.creditsAndPayments.updateCreditPayments(this.total);

					// CreditsAndPayments creditsAndPayments = new
					// CreditsAndPayments(
					// this);
					// session.saveOrUpdate(creditsAndPayments);
					// this.creditsAndPayments
					// .updateCreditPayments(customerCreditMemo.total
					// - this.total);

				}
				// session.saveOrUpdate(this.creditsAndPayments);

			}
			// this.customer.updateBalance(session, this, this.total);
			// if (customerCreditMemo.customer.id == this.customer.id) {
			//
			// if (customerCreditMemo.total > this.total) {
			// customerCreditMemo.total -= customerCreditMemo.total
			// - this.total;
			//
			// } else if (customerCreditMemo.total < this.total) {
			// customerCreditMemo.total -= customerCreditMemo.total
			// - this.total;
			// }
			//
			// this.customer.updateBalance(session, customerCreditMemo,
			// customerCreditMemo.total);
			// }

		}
		super.onEdit(clonedObject);

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return super.canEdit(clientObject);
	}
}
