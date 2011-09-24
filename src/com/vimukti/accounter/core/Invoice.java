package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * @author Suresh Garikapati
 * 
 * <br>
 *         <b>Invoice </b><br>
 * <br>
 *         <b><i>Effect on Transaction Item </i></b><br>
 *         ===============================>Item <br>
 *         If Item's IncomeAccount isIncrease true then Increasae the current
 *         and total balance by line total otherwise Decrease.<br>
 * 
 * <br>
 *         ===============================>Account<br>
 * 
 *         If Account isIncrease true then Increasae the current and total
 *         balance by line total otherwise Decrease.<br>
 * 
 * <br>
 *         ===============================>Sales Tax<br>
 * 
 *         If Specified TaxCode's all Tax Agency's Liability Account isIncrease
 *         true then Increase the current and total balance by line total
 *         otherwise decrease. Specified TaxCode's all Tax Agency's Balance will
 *         Increase by line total .<br>
 * <br>
 *         <b><i> Effect on Tax Group</b></i><br>
 * 
 *         Specified TaxGroup's all TaxCode's Tax Agency's Liability Account
 *         isIncrease true then Increase the current and total balance by sales
 *         tax collected for each taxcode otherwise decrease. Specified
 *         TaxGroup's TaxCode's all Tax Agency's Balance will Increase by sales
 *         tax collected for each taxcode.<br>
 * <br>
 *         <b><i>Other Effect:</b></i><br>
 * 
 *         Accounts Receivable account current and total balance will increase
 *         by the Invoice total. Customer balance should Increase by the Invoice
 *         total.<br>
 * 
 * <br>
 *         <b><i> Status Updation:</b></i><br>
 * 
 *         If any Quote is used then, that Quote isTurnedToInvoice become true
 *         and the status of that Quote become Accepted.
 * 
 */

public class Invoice extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// static Logger log = Logger.getLogger(Invoice.class);

	Invoice oldInvoice;

	@Override
	public void onLoad(Session session, Serializable arg1) {
		// log.info("Invoice Loaded: " + this.id + " " + this);
		super.onLoad(session, arg1);
		try {
			oldInvoice = (Invoice) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * The Customer involved in this Invoice Transaction
	 * 
	 * @see Customer
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * The Desired Contact as Per the Selected Customer
	 * 
	 * @see Contact
	 */
	Contact contact;

	/**
	 * The Billing Address of the Selected Customer
	 * 
	 * @see Address
	 */
	Address billingAddress;

	/**
	 * The Shipping Address of the Selected Customer
	 * 
	 * @see Address
	 */
	Address shippingAdress;

	/**
	 * The Phone Detail as Entered for the Customer
	 */
	String phone;

	/**
	 * The Default Sales Person for this Invoice Transaction
	 * 
	 * @see SalesPerson
	 */

	SalesPerson salesPerson;

	/**
	 * The Payment Term set for this Invoice
	 * 
	 * @see PaymentTerms
	 */
	PaymentTerms paymentTerm;

	/**
	 * The Shipping Term, set for this Invoice
	 * 
	 * @see ShippingTerms
	 */
	ShippingTerms shippingTerm;

	/**
	 * The Default Shipping Method for the Invoice
	 * 
	 * @see ShippingTerms
	 */
	ShippingMethod shippingMethod;

	/**
	 * Invoice Due Date
	 */
	FinanceDate dueDate;

	/**
	 * Invoice DeliveryDate
	 */
	FinanceDate deliverydate;

	String orderNum;

	double discountTotal;

	/**
	 * The {@link PriceLevel} set for this Invoice
	 */
	PriceLevel priceLevel;

	double taxTotal;

	double payments = 0D;

	double balanceDue = 0D;

	boolean isPaid = false;

	boolean isEdited = false;

	/**
	 * Estimate from which this Invoice was Recreated
	 * 
	 * @see Estimate
	 */
	@ReffereredObject
	Estimate estimate;

	/**
	 * {@link SalesOrder} against which the Invoice was made.
	 */
	@ReffereredObject
	SalesOrder salesOrder;

	/**
	 * Invoice Discount FinanceDate
	 */
	FinanceDate discountDate;

	/**
	 * Set of Transaction Receive Payments
	 * 
	 * @see TransactionReceivePayment
	 */
	@ReffereredObject
	Set<TransactionReceivePayment> transactionReceivePayments = new HashSet<TransactionReceivePayment>();

	//

	public Set<TransactionReceivePayment> getTransactionReceivePayments() {
		return transactionReceivePayments;
	}

	public void setTransactionReceivePayments(
			Set<TransactionReceivePayment> transactionReceivePayments) {
		this.transactionReceivePayments = transactionReceivePayments;
	}

	public Invoice() {
		super();
		setType(Transaction.TYPE_INVOICE);
	}

	public Invoice(Estimate estimate) {
		super();
		setType(Transaction.TYPE_INVOICE);

	}

	public Invoice(Session session, ClientInvoice invoice) {
		this.type = Transaction.TYPE_INVOICE;

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

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the billingAddress
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
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
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the shippingTerm
	 */
	public ShippingTerms getShippingTerm() {
		return shippingTerm;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @return the dueFinanceDate
	 */
	public FinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @return the deliverydate
	 */
	public FinanceDate getDeliverydate() {
		return deliverydate;
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

	/**
	 * @return the priceLevel
	 */
	public PriceLevel getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @return the payments
	 */
	public double getPayments() {
		return payments;
	}

	/**
	 * @param payments
	 *            the payments to set
	 */
	public void setPayments(double payments) {
		this.payments = payments;
	}

	/**
	 * @return the balanceDue
	 */
	public double getBalanceDue() {
		return balanceDue;
	}

	/**
	 * @param balanceDue
	 *            the balanceDue to set
	 */
	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the isPaid
	 */
	public boolean getIsPaid() {
		return isPaid;
	}

	/**
	 * @param isPaid
	 *            the isPaid to set
	 */
	public void setIsPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public boolean getIsEdited() {
		return isEdited;
	}

	public Estimate getEstimate() {
		return estimate;
	}

	public FinanceDate getDiscountDate() {
		return discountDate;
	}

	public SalesOrder getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(SalesOrder salesOrder) {
		this.salesOrder = salesOrder;
	}

	@Override
	public void setTransactionItems(List<TransactionItem> transactionItems) {
		super.setTransactionItems(transactionItems);

		// this.allLineTotal = getLineTotalSum();
		// this.allTaxableLineTotal = getTaxableLineTotalSum();
		this.discountTotal = getDiscountTotalSum();

	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		// NOTHING TO DO.
		return false;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;
		doCreateEffect(session);
		return false;

	}

	private void doCreateEffect(Session session) {
		// super.onSave(session);
		this.balanceDue = this.total;
		/**
		 * To update the Status of the Estimate (if any) Involved in this
		 * Invoice.
		 * 
		 */
		modifyEstimate(this, true);

		modifySalesOrder(this, true);

	}

	private void modifyEstimate(Invoice invoice, boolean isCreated) {
		if (invoice.estimate == null)
			return;
		Session session = HibernateUtil.getCurrentSession();
		Estimate estimate = (Estimate) session.get(Estimate.class,
				invoice.estimate.id);
		if (estimate != null) {
			boolean isPartiallyInvoiced = false;
			boolean flag = true;
			if (invoice.transactionItems != null
					&& invoice.transactionItems.size() > 0) {

				FinanceLogger
						.log("update the Status of the Estimate with Number {0}  (if any) Involved in this Invoice ",
								String.valueOf(this.estimate.number));

				for (TransactionItem transactionItem : invoice.transactionItems) {
					/**
					 * This is to know whether this transaction item is of new
					 * one or it's came from any Sales Order.
					 */

					if (transactionItem.referringTransactionItem != null) {
						TransactionItem referringTransactionItem = (TransactionItem) session
								.get(TransactionItem.class,
										transactionItem.referringTransactionItem
												.getID());
						double amount = 0d;

						if (!isCreated)
							if (transactionItem.type == TransactionItem.TYPE_ITEM) {
								if (DecimalUtil
										.isLessThan(
												transactionItem.lineTotal,
												transactionItem
														.getQuantity()
														.calculatePrice(
																referringTransactionItem.unitPrice)))
									referringTransactionItem.usedamt -= transactionItem.lineTotal;
								else
									referringTransactionItem.usedamt -= transactionItem
											.getQuantity()
											.calculatePrice(
													referringTransactionItem.unitPrice);
							} else
								referringTransactionItem.usedamt -= transactionItem.lineTotal;

						else {
							if (transactionItem.type == TransactionItem.TYPE_ITEM) {
								if (DecimalUtil
										.isLessThan(
												transactionItem.lineTotal,
												transactionItem
														.getQuantity()
														.calculatePrice(
																referringTransactionItem.unitPrice)))
									referringTransactionItem.usedamt += transactionItem.lineTotal;
								else
									referringTransactionItem.usedamt += transactionItem
											.getQuantity()
											.calculatePrice(
													referringTransactionItem.unitPrice);
							} else
								referringTransactionItem.usedamt += transactionItem.lineTotal;
						}
						amount = referringTransactionItem.usedamt;
						/**
						 * This is to save changes to the invoiced amount of the
						 * referring transaction item to this transaction item.
						 */
						session.update(referringTransactionItem);

						if (flag
								&& ((transactionItem.type == TransactionItem.TYPE_ACCOUNT || ((transactionItem.type == TransactionItem.TYPE_ITEM) && transactionItem
										.getQuantity().compareTo(
												referringTransactionItem
														.getQuantity()) < 0)))) {
							if (isCreated ? DecimalUtil.isLessThan(amount,
									referringTransactionItem.lineTotal)
									: DecimalUtil.isGreaterThan(amount, 0)) {
								isPartiallyInvoiced = true;
								flag = false;
							}
						}
						// if (id != 0l && !invoice.isVoid())
						// referringTransactionItem.usedamt +=
						// transactionItem.lineTotal;

					}

				}
			}
			/**
			 * Updating the Status of the Sales Order involved in this Invoice
			 * depending on the above Analysis.
			 */
			if (!isPartiallyInvoiced) {
				double usdAmount = 0;
				for (TransactionItem orderTransactionItem : estimate.transactionItems)
					// if (orderTransactionItem.getType() != 6)
					usdAmount += orderTransactionItem.usedamt;
				// else
				// usdAmount += orderTransactionItem.lineTotal;
				if (DecimalUtil.isLessThan(usdAmount, estimate.netAmount))
					isPartiallyInvoiced = true;
			}
			if (isPartiallyInvoiced) {
				estimate.status = Estimate.STATUS_OPEN;
			} else {
				estimate.status = isCreated ? Estimate.STATUS_ACCECPTED
						: Estimate.STATUS_OPEN;

			}

			session.saveOrUpdate(estimate);

		}

		// if (invoice.estimate != null) {
		// FinanceLogger
		// .log(
		// "update the Status of the Estimate with Number {0}  (if any) Involved in this Invoice ",
		// String.valueOf(this.estimate.number));
		//
		// // To Mark the Estimate as Invoiced.
		// invoice.estimate.setTurnedToInvoiceOrSalesOrder(isCreated);
		//
		// // To update the status of the Estimate as Accepted.
		// invoice.estimate.status = isCreated ? Estimate.STATUS_ACCECPTED
		// : Estimate.STATUS_OPEN;
		// }
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);

		// if (isBecameVoid()) {
		//
		// doVoidEffect(session, this);
		// }
		// /**
		// * This block is to deal with editing part
		// */
		// else if (!this.equals(oldInvoice)) {
		// this.isEdited = true;
		// /*
		// * return if no modifications has been done in this editing.
		// */
		// // if (this.compareTo(oldInvoice) == 0) {
		// // this.isEdited = false;
		// // return false;
		// // }
		// /**
		// * First do the reverse effect of the old Invoice
		// */
		//
		// doReverseEffect(session, oldInvoice);
		//
		// /**
		// * do the new Effect with the new values.
		// */
		// doCreateEffect(session);
		//
		// /**
		// * Mark this transaction for further edit
		// */
		// this.isEdited = false;
		//
		// /**
		// * Save explictly all the effect
		// */
		// session.saveOrUpdate(this);
		// }

		return false;
	}

	private void doVoidEffect(Session session, Invoice invoice) {

		if (invoice.transactionReceivePayments != null) {
			for (TransactionReceivePayment trp : invoice.transactionReceivePayments) {
				trp.onVoidTransaction(session);
				session.saveOrUpdate(trp);
			}
		}
		if (invoice.status != Transaction.STATUS_DELETED)
			invoice.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		invoice.payments = invoice.total;
		invoice.balanceDue = 0.0;

		modifyEstimate(invoice, false);

		modifySalesOrder(invoice, false);
		// if (invoice.salesOrder != null) {
		// invoice.salesOrder.status = Transaction.STATUS_CANCELLED;
		// }

	}

	private void modifySalesOrder(Invoice invoice, boolean isAddition) {
		if (invoice.salesOrder == null)
			return;
		Session session = HibernateUtil.getCurrentSession();
		SalesOrder salesOrder = (SalesOrder) session.get(SalesOrder.class,
				invoice.salesOrder.id);
		if (salesOrder != null) {
			boolean isPartiallyInvoiced = false;
			boolean flag = true;
			if (invoice.transactionItems != null
					&& invoice.transactionItems.size() > 0) {

				FinanceLogger.log(
						"update the Status of the Sales Order with Number {0} "
								+ "  (if any) Invloved in this Invoice",
						String.valueOf(invoice.salesOrder.number));

				for (TransactionItem transactionItem : invoice.transactionItems) {
					/**
					 * This is to know whether this transaction item is of new
					 * one or it's came from any Sales Order.
					 */

					if (transactionItem.referringTransactionItem != null) {
						TransactionItem referringTransactionItem = (TransactionItem) session
								.get(TransactionItem.class,
										transactionItem.referringTransactionItem
												.getID());
						double amount = 0d;

						if (!isAddition)
							if (transactionItem.type == TransactionItem.TYPE_ITEM) {
								if (DecimalUtil
										.isLessThan(
												transactionItem.lineTotal,
												transactionItem
														.getQuantity()
														.calculatePrice(
																referringTransactionItem.unitPrice)))
									referringTransactionItem.usedamt -= transactionItem.lineTotal;
								else
									referringTransactionItem.usedamt -= transactionItem
											.getQuantity()
											.calculatePrice(
													referringTransactionItem.unitPrice);
							} else
								referringTransactionItem.usedamt -= transactionItem.lineTotal;

						else {
							if (transactionItem.type == TransactionItem.TYPE_ITEM) {
								if (DecimalUtil
										.isLessThan(
												transactionItem.lineTotal,
												transactionItem
														.getQuantity()
														.calculatePrice(
																referringTransactionItem.unitPrice)))
									referringTransactionItem.usedamt += transactionItem.lineTotal;
								else
									referringTransactionItem.usedamt += transactionItem
											.getQuantity()
											.calculatePrice(
													referringTransactionItem.unitPrice);
							} else
								referringTransactionItem.usedamt += transactionItem.lineTotal;
						}
						amount = referringTransactionItem.usedamt;
						/**
						 * This is to save changes to the invoiced amount of the
						 * referring transaction item to this transaction item.
						 */
						session.update(referringTransactionItem);

						if (flag
								&& ((transactionItem.type == TransactionItem.TYPE_ACCOUNT || ((transactionItem.type == TransactionItem.TYPE_ITEM) && transactionItem
										.getQuantity().compareTo(
												referringTransactionItem
														.getQuantity()) < 0)))) {
							if (isAddition ? DecimalUtil.isLessThan(amount,
									referringTransactionItem.lineTotal)
									: DecimalUtil.isGreaterThan(amount, 0)) {
								isPartiallyInvoiced = true;
								flag = false;
							}
						}
						// if (id != 0l && !invoice.isVoid())
						// referringTransactionItem.usedamt +=
						// transactionItem.lineTotal;

					}

				}
			}
			/**
			 * Updating the Status of the Sales Order involved in this Invoice
			 * depending on the above Analysis.
			 */
			if (!isPartiallyInvoiced) {
				double usdAmount = 0;
				for (TransactionItem orderTransactionItem : salesOrder.transactionItems)
					// if (orderTransactionItem.getType() != 6)
					usdAmount += orderTransactionItem.usedamt;
				// else
				// usdAmount += orderTransactionItem.lineTotal;
				if (DecimalUtil.isLessThan(usdAmount, salesOrder.netAmount))
					isPartiallyInvoiced = true;
			}
			if (isPartiallyInvoiced) {
				salesOrder.status = Transaction.STATUS_OPEN;
			} else {
				salesOrder.status = isAddition ? Transaction.STATUS_COMPLETED
						: Transaction.STATUS_OPEN;

			}

			salesOrder.onUpdate(session);
			session.saveOrUpdate(salesOrder);

		}

	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return true;
	}

	@Override
	public Account getEffectingAccount() {
		return null;
	}

	@Override
	public Payee getPayee() {
		return customer;
	}

	public void updatePaymentsAndBalanceDue(double amount) {
		this.payments -= amount;
		this.balanceDue += amount;

		if (DecimalUtil.isGreaterThan(this.balanceDue, 0.0)
				&& DecimalUtil.isLessThan(this.balanceDue, this.total)) {
			this.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		} else if (DecimalUtil.isEquals(this.balanceDue, 0.0)) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (DecimalUtil.isEquals(this.balanceDue, this.total)) {
			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		}
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public double getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(double salesTaxAmount) {
		this.taxTotal = salesTaxAmount;

	}

	public void setDiscountTotal(double discountTotal) {
		this.discountTotal = discountTotal;
	}

	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
	}

	public void setDueDate(FinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_INVOICE;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.customer;
	}

	public boolean equals(Invoice in) {
		if (this.getTotal() == in.getTotal()
				&& this.transactionItems.size() == in.transactionItems.size()

				&& ((this.transactionDate != null && in.transactionDate != null) ? (this.transactionDate
						.equals(in.transactionDate)) : true)
				&& ((this.customer != null && in.customer != null) ? (this.customer
						.equals(in.customer)) : true)
				&& ((this.estimate != null && in.estimate != null) ? (this.estimate
						.equals(estimate)) : true)
				&& ((this.salesOrder != null && in.salesOrder != null) ? (this.salesOrder.id == in.salesOrder.id)
						: true)) {

			for (int i = 0; i < this.transactionItems.size(); i++) {
				if (!this.transactionItems.get(i).equals(
						in.transactionItems.get(i))) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

		Invoice invoice = (Invoice) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		this.balanceDue = this.total - payments;
		/**
		 * if present transaction is deleted or voided & previous transaction is
		 * not voided then only it will enter the loop
		 */

		if ((this.isVoid && !invoice.isVoid)
				|| (this.isDeleted() && !invoice.isDeleted() && !this.isVoid)) {
			doVoidEffect(session, this);
		} else if (!invoice.equals(this)) {

			this.cleanTransactionitems(this);
			if (!this.customer.equals(invoice.customer)) {
				doVoidEffect(session, invoice);

				Customer customer = (Customer) session.get(Customer.class,
						invoice.customer.id);
				customer.updateBalance(session, this, invoice.total);
				this.onSave(session);
				return;
			}
			if (!DecimalUtil.isEquals(this.total, invoice.total)) {
				if (DecimalUtil.isGreaterThan(this.total, this.payments)) {
					Customer customer = (Customer) session.get(Customer.class,
							invoice.customer.id);
					customer.updateBalance(session, this, invoice.total);
					this.customer.updateBalance(session, this, -this.total);
				}
			}

			if (this.salesOrder != null || invoice.salesOrder != null)
				if (this.salesOrder == null && invoice.salesOrder != null) {
					modifySalesOrder(invoice, false);
				} else if (this.salesOrder != null
						&& invoice.salesOrder == null) {
					modifySalesOrder(this, true);
				} else if (!this.salesOrder.equals(invoice.salesOrder)) {
					modifySalesOrder(invoice, false);
					modifySalesOrder(this, true);

				} else {
					for (TransactionItem transactionItem : invoice.transactionItems) {
						if (transactionItem.referringTransactionItem != null
								&& DecimalUtil
										.isGreaterThan(
												transactionItem.referringTransactionItem.usedamt,
												0)) {
							transactionItem.referringTransactionItem.usedamt -= transactionItem.lineTotal;
						}
					}
					modifySalesOrder(this, true);

				}
			if (this.estimate != null || invoice.estimate != null)
				if (this.estimate == null && invoice.estimate != null) {
					modifyEstimate(invoice, false);
				} else if (this.estimate != null && invoice.estimate == null) {
					modifyEstimate(this, true);
				} else if (!this.estimate.equals(invoice.estimate)) {
					modifyEstimate(invoice, false);
					modifyEstimate(this, true);
				}
			this.updateTransactionReceivepayments();
		}

		super.onEdit(invoice);

	}

	private void updateTransactionReceivepayments() {
		/**
		 * First do the reverse effect of the old Invoice
		 */
		if (this.transactionReceivePayments != null) {
			Double amtdue = this.total;
			for (TransactionReceivePayment trp : this.transactionReceivePayments) {
				trp.setInvoiceAmount(this.total);
				amtdue = trp.updatePayments(amtdue);
				HibernateUtil.getCurrentSession().saveOrUpdate(trp);
			}
		}
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		if (this.transactionReceivePayments != null) {
			for (TransactionReceivePayment trp : this.transactionReceivePayments) {
				if (DecimalUtil.isGreaterThan(trp.writeOff, 0D)
						|| DecimalUtil.isGreaterThan(trp.cashDiscount, 0d)) {
					throw new AccounterException(
							AccounterException.ERROR_RECEIVE_PAYMENT_DISCOUNT_USED);
					// "In the ReceivePayment writeoff or discount is used for this Invoice");
				}

			}
		}

		if (this.status == Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED
				|| this.status == Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED) {
			throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
			// "You have already paid some amount for this Invoice, You can't Edit  it and Void it.");
		}

		return super.canEdit(clientObject);
	}

}
