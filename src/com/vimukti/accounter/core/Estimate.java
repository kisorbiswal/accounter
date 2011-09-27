package com.vimukti.accounter.core;

import org.hibernate.Session;

import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class Estimate extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int STATUS_OPEN = 0;
	public static final int STATUS_REJECTED = 1;
	public static final int STATUS_ACCECPTED = 2;

	/**
	 * This is the Customer to whom we are creating this Quote.
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
	Address address;

	/**
	 * This defaults to the chosen Customer's primary contact Business Phone
	 * number.
	 * 
	 * @see Customer
	 * @see Contact
	 */
	String phone;

	SalesPerson salesPerson;

	/**
	 * The payment term which we have selected for this Quote
	 */
	PaymentTerms paymentTerm;

	/**
	 * the date on which it will expire, this will become due date in Invoice in
	 * which this Quote will used.
	 * 
	 * @see Invoice.
	 */
	FinanceDate expirationDate;

	/**
	 * the date on which the products should deliver, this will become delivery
	 * date of the Invoice in which this Quote will used..
	 */
	FinanceDate deliveryDate;

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
	double taxTotal;

	/**
	 * this is to verify whether this Quote being used in any Invoice or Sales
	 * Order
	 * 
	 * @see Invoice
	 * @see SalesOrder
	 */
	boolean isTurnedToInvoiceOrSalesOrder = false;

	public Estimate() {
		setType(Transaction.TYPE_ESTIMATE);
	}

	public Estimate(Session session, ClientEstimate estimate) {
		this.type = Transaction.TYPE_ESTIMATE;

	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public void setExpirationDate(FinanceDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setDeliveryDate(FinanceDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * onEdit
	 * 
	 * @return the salesPerson
	 */
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
	}

	/**
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	/**
	 * @return the expirationFinanceDate
	 */
	public FinanceDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @return the deliveryDate
	 */
	public FinanceDate getDeliveryDate() {
		return deliveryDate;
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
	public double getTaxTotal() {
		return taxTotal;
	}

	public boolean isTurnedToInvoiceOrSalesOrder() {
		return isTurnedToInvoiceOrSalesOrder;
	}

	public void setTurnedToInvoiceOrSalesOrder(
			boolean isTurnedToInvoiceOrSalesOrder) {
		this.isTurnedToInvoiceOrSalesOrder = isTurnedToInvoiceOrSalesOrder;
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

		return null;
	}

	@Override
	public Payee getPayee() {

		return null;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_ESTIMATE;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.customer;
	}

	public boolean equals(Estimate est) {
		if (DecimalUtil.isEquals(this.getTotal(), est.getTotal())
				&& this.id == est.id
				&& this.transactionItems.size() == est.transactionItems.size()
				&& ((this.transactionDate != null && est.transactionDate != null) ? (this.transactionDate
						.equals(est.transactionDate)) : true)
				&& ((this.customer != null && est.customer != null) ? (this.customer
						.equals(est.customer)) : true)) {
			for (int i = 0; i < this.transactionItems.size(); i++) {
				if (!this.transactionItems.get(i).equals(
						est.transactionItems.get(i)))
					return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		if (this.status == Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED) {
			throw new AccounterException(AccounterException.ERROR_NAME_CONFLICT);
			// "This Quote is Already used in SalesOrder or Invoice");
		}
		// else if (this.status == STATUS_REJECTED) {
		// throw new InvalidOperationException(
		// "You can't edit Quote, it is Rejected");
		//
		// }

		/**
		 * If Quote is already voided or deleted, we can't edit it
		 */
		if (((Estimate) clientObject).status == STATUS_REJECTED) {
			throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
			// "This Quote is already  Rejected,can't  Modify");
		}

		return true;
	}
}
