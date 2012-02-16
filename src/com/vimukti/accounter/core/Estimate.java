package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class Estimate extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int STATUS_OPEN = 0;

	public static final int STATUS_REJECTED = 1;

	public static final int STATUS_APPLIED = 5;

	public static final int STATUS_ACCECPTED = 2;

	public static final int STATUS_CLOSE = 4;

	public static final int QUOTES = 1;

	public static final int CREDITS = 2;

	public static final int CHARGES = 3;

	public static final int BILLABLEEXAPENSES = 4;

	public static final int DEPOSIT_EXPENSES = 5;

	/**
	 * This is the Customer to whom we are creating this Quote.
	 */
	@ReffereredObject
	Customer customer;
	private Job job;
	private int transactionType;
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
	 * The Shipping Address of the Selected Customer
	 * 
	 * @see Address
	 */
	Address shippingAdress;
	/**
	 * 
	 */
	private int estimateType;
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
	private double taxTotal;

	/**
	 * this is to verify whether this Quote being used in any Invoice or Sales
	 * Order
	 * 
	 * @see Invoice
	 * @see SalesOrder
	 */
	boolean isTurnedToInvoiceOrSalesOrder = false;

	private Invoice usedInvoice;

	private Invoice oldUsedInvoice;

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

	/**
	 * @return the shippingAdress
	 */
	public Address getShippingAdress() {
		return shippingAdress;
	}

	public void setShippingAdress(Address shippingAdress) {
		this.shippingAdress = shippingAdress;
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
		if (this.estimateType == CREDITS) {
			return false;
		}
		return true;
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

	@Override
	public void onEdit(Transaction clonedObject) {
		super.onEdit(clonedObject);
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {
		super.onLoad(session, arg1);
		this.oldUsedInvoice = usedInvoice;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		if (!UserUtils.canDoThis(Estimate.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		if (this.getID() != 0) {
			if (this.status == Transaction.STATUS_APPLIED) {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_IN_USE);
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
			// if (((Estimate) clientObject).status == STATUS_REJECTED) {
			// throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
			// // "This Quote is already  Rejected,can't  Modify");
			// }
		}

		return true;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public int getEstimateType() {
		return estimateType;
	}

	public void setEstimateType(int estimateType) {
		this.estimateType = estimateType;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		try {
			if (this.getEstimateType() == BILLABLEEXAPENSES
					|| this.getEstimateType() == DEPOSIT_EXPENSES) {
				updateTotals();
			}
		} catch (AccounterException e) {
		}
		super.onSave(session);
		return false;
	}

	@Override
	protected void checkNullValues() throws AccounterException {
		checkingCustomerNull(customer);
	}

	private void updateTotals() throws AccounterException {
		double lineTotal = 0.0;
		double totalTax = 0.0;
		for (TransactionItem record : this.getTransactionItems()) {
			Double lineTotalAmt = record.getLineTotal();
			Double vaTfraction = record.getVATfraction();
			if (record.isAmountIncludeTAX()) {
				lineTotalAmt = lineTotalAmt - vaTfraction;
			}
			if (record != null && record.isTaxable()) {
				chekingTaxCodeNull(record.taxCode);
				totalTax += record.getVATfraction();
			}
			lineTotal += lineTotalAmt;

		}
		setTaxTotal(totalTax);
		setNetAmount(lineTotal);
		setTotal(totalTax + lineTotal);
	}

	public void setTaxTotal(double taxTotal) {
		this.taxTotal = taxTotal;
	}

	/**
	 * @return the usedTransaction
	 */
	public Invoice getUsedInvoice() {
		return usedInvoice;
	}

	/**
	 * @param usedTransaction
	 *            the usedTransaction to set
	 */
	public void setUsedInvoice(Invoice usedTransaction, Session session) {
		if (this.usedInvoice == null && usedTransaction != null) {
			this.usedInvoice = usedTransaction;
			status = STATUS_APPLIED;
		} else if (usedTransaction == null) {
			this.usedInvoice = null;
			status = STATUS_ACCECPTED;
		}
	}

	/**
	 * @return the oldUsedInvoice
	 */
	public Invoice getOldUsedInvoice() {
		return oldUsedInvoice;
	}

	/**
	 * @param oldUsedInvoice
	 *            the oldUsedInvoice to set
	 */
	public void setOldUsedInvoice(Invoice oldUsedInvoice) {
		this.oldUsedInvoice = oldUsedInvoice;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {

		return super.onDelete(session);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.estimate()).gap();
		w.put(messages.no(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();
		if (this.customer != null) {
			w.put(messages.name(), this.customer.name);
			w.put(messages.openBalance(), this.customer.openingBalance).gap();
		}

		w.put(messages.currency(), this.currencyFactor).gap();
		w.put(messages.amount(), this.total);

		w.put(messages.paymentMethod(), this.paymentMethod);

		w.put(messages.memo(), this.memo).gap().gap();

	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (customer == null) {
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

	@Override
	protected void updatePayee(boolean onCreate) {

	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

}
