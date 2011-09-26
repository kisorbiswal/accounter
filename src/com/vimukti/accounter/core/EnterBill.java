package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * @author vimukti16 <br>
 *         <b>Enter Bill</b><br>
 * <br>
 *         <b><i>Effect on Transaction Item:</i></b><br>
 * 
 *         ===============================>Item<br>
 * 
 *         If Item's ExpenseAccount isIncrease true then Decrease the current
 *         and total balance by line total otherwise Increase.<br>
 * 
 * <br>
 *         ===============================>Account<br>
 * 
 *         If Account isIncrease true then Decrease the current and total
 *         balance by line total otherwise Increase.<br>
 * 
 * <br>
 *         <b><i>Other Effect:</i></b><br>
 * 
 *         Accounts Payable account current and total balance will Increase by
 *         the EnterBill total. Vendor balance should Increase by the EnterBill
 *         total.
 */
public class EnterBill extends Transaction implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is the Vendor to whom we are creating this Bill
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
	 * The payment term which we have selected for this bill
	 */
	PaymentTerms paymentTerm;

	/**
	 * This is the date after which the Bill is owed.
	 */
	FinanceDate dueDate;

	/**
	 * This is the date on which the delivery of the products is done.
	 */
	FinanceDate deliveryDate;

	FinanceDate discountDate;

	boolean isPaid = false;

	/**
	 * This is to specify how much of this Bill is paid.
	 */
	double payments = 0D;

	/**
	 * This will specify the balance due
	 */
	double balanceDue = 0D;

	/**
	 * This consists a Set of {@link TransactionPayBill} This will specify in
	 * which PayBill this Bill is being paid.
	 */
	@ReffereredObject
	Set<TransactionPayBill> transactionPayBills = new HashSet<TransactionPayBill>();

	/**
	 * This will specify which purchase order has been used in this Bill.
	 * 
	 * @see PurchaseOrder
	 */
	@ReffereredObject
	PurchaseOrder purchaseOrder;

	/**
	 * This will specify which Item Receipt has been used in this Bill
	 * 
	 * @see ItemReceipt
	 */
	@ReffereredObject
	ItemReceipt itemReceipt;

	//

	public EnterBill() {
		setType(Transaction.TYPE_ENTER_BILL);
	}

	public EnterBill(Session session, ClientEnterBill enterbill) {
		this.type = Transaction.TYPE_ENTER_BILL;

	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public ItemReceipt getItemReceipt() {
		return itemReceipt;
	}

	public void setItemReceipt(ItemReceipt itemReceipt) {
		this.itemReceipt = itemReceipt;
	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	public void setPhone(String phoneNo) {
		this.phone = phoneNo;
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
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the dueDate
	 */
	public FinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @return the deliveryDate
	 */
	public FinanceDate getDeliveryDate() {
		return deliveryDate;
	}

	public FinanceDate getDiscountDate() {
		return discountDate;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);
		this.balanceDue = this.total;

		/**
		 * To check if any Purchase Order is involved in this Purchase Invoice.
		 * If this Purchase Invoice uses any Purchase Order then we should
		 * update the status accordingly. The Status of the Purchase Order may
		 * be Not Received or Partially Received or Received. First of all we
		 * need to check whether this Purchase Invoice uses any Purchase Order
		 * or not.
		 */
		// if (this.purchaseOrder != null) {
		// boolean isPartiallyReceived = false;
		// boolean flag = true;
		// if (this.transactionItems != null
		// && this.transactionItems.size() > 0) {
		// for (TransactionItem transactionItem : this.transactionItems) {
		//
		// /**
		// * This is to know whether this transaction item is of new
		// * one or it's came from any Purchase Order.
		// */
		// TransactionItem referringTransactionItem =
		// transactionItem.referringTransactionItem;
		// double amount = 0d;
		//
		// if (referringTransactionItem != null) {
		// referringTransactionItem.usedamt += transactionItem.quantity
		// * referringTransactionItem.unitPrice;
		// amount = referringTransactionItem.usedamt;
		// /**
		// * This is to save changes to the invoiced amount of the
		// * referring transaction item to this transaction item.
		// */
		// session.update(referringTransactionItem);
		//
		// if (flag
		// && ((transactionItem.type == TransactionItem.TYPE_ACCOUNT
		// || transactionItem.type == TransactionItem.TYPE_SALESTAX ||
		// transactionItem.type == transactionItem.TYPE_ITEM) && amount <
		// referringTransactionItem.lineTotal)) {
		// isPartiallyReceived = true;
		// flag = false;
		// }
		// }
		//
		// }
		// }
		// /**
		// * Updating the Status of the Purchase Order involved in this
		// * Purchase Invoice depending on the above Analysis.
		// */
		// if (isPartiallyReceived) {
		// this.purchaseOrder.status =
		// Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		// } else {
		// this.purchaseOrder.status =
		// Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		//
		// }
		//
		// }
		modifyPurchaseOrder(this, true);
		/*
		 * Make void the corresponding Item Receipt.
		 */
		if (this.itemReceipt != null) {
			this.itemReceipt.isVoid = true;
			this.itemReceipt.isBilled = true;
			this.itemReceipt.balanceDue = 0;
			this.itemReceipt.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;

			Account pendingItemReceipt = getCompany()
					.getPendingItemReceiptsAccount();
			pendingItemReceipt.updateCurrentBalance(this.itemReceipt,
					-this.itemReceipt.total);
			session.update(pendingItemReceipt);
			pendingItemReceipt.onUpdate(session);

			session.saveOrUpdate(pendingItemReceipt);

			this.itemReceipt.balanceDue = 0.0;

			this.itemReceipt.voidTransactionItems();
			deleteCreatedEntries(session, this.itemReceipt);
		}

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		// if (this.isBecameVoid()) {
		//
		// for (TransactionPayBill tx : this.transactionPayBills) {
		// tx.makeVoid(session);
		// }
		//
		// this.payments = this.total;
		// this.balanceDue = 0.0;
		//
		// if (this.purchaseOrder != null) {
		// boolean isPartiallyReceived = false;
		// boolean flag = true;
		// if (this.transactionItems != null
		// && this.transactionItems.size() > 0) {
		// for (TransactionItem transactionItem : this.transactionItems) {
		//
		// /**
		// * This is to know whether this transaction item is of
		// * new one or it's came from any Purchase Order.
		// */
		// TransactionItem referringTransactionItem =
		// transactionItem.referringTransactionItem;
		// double amount = 0d;
		//
		// if (referringTransactionItem != null) {
		// referringTransactionItem.usedamt -= transactionItem.lineTotal;
		// amount = referringTransactionItem.usedamt;
		// /**
		// * This is to save changes to the invoiced amount of
		// * the referring transaction item to this
		// * transaction item.
		// */
		// session.update(referringTransactionItem);
		// if (flag
		// && ((transactionItem.type == TransactionItem.TYPE_ACCOUNT ||
		// transactionItem.type == TransactionItem.TYPE_SALESTAX) && amount >
		// 0)) {
		// isPartiallyReceived = true;
		// flag = false;
		// }
		// }
		//
		// }
		// }
		// /**
		// * Updating the Status of the Purchase Order involved in this
		// * Purchase Invoice depending on the above Analysis.
		// */
		// if (isPartiallyReceived) {
		// this.purchaseOrder.status =
		// Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		// } else {
		// this.purchaseOrder.status =
		// Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		//
		// }
		//
		// }
		// // /*
		// // * Make void the corresponding Item Receipt.
		// // */
		// if (this.itemReceipt != null) {
		// // this.itemReceipt.isVoid = false;
		// // this.itemReceipt.isBilled = false;
		// // this.itemReceipt.status =
		// // Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		// }
		//
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

	/**
	 * @return the isPaid
	 */
	public boolean getIsPaid() {
		return isPaid;
	}

	/**
	 * @return the payments
	 */
	public double getPayments() {
		return payments;
	}

	/**
	 * @return the balanceDue
	 */
	public double getBalanceDue() {
		return balanceDue;
	}

	/**
	 * @param payments
	 *            the payments to set
	 */
	public void setPayments(double payments) {
		this.payments = payments;
	}

	/**
	 * @param balanceDue
	 *            the balanceDue to set
	 */
	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
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
		return this.vendor;
	}

	public void updatePaymentsAndBalanceDue(double amount) {
		this.payments -= amount;
		this.balanceDue += amount;
		if (DecimalUtil.isGreaterThan(this.balanceDue, 0)
				&& DecimalUtil.isLessThan(this.balanceDue, this.total)) {

			this.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		} else if (DecimalUtil.isEquals(this.balanceDue, 0.0)) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (DecimalUtil.isEquals(this.balanceDue, this.total)) {

			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		}

	}

	/*
	 * Vendor vendor;
	 * 
	 * Contact contact;
	 * 
	 * Address vendorAddress;
	 * 
	 * String phone;
	 * 
	 * PaymentTerms paymentTerm;
	 * 
	 * Date dueDate;
	 * 
	 * Date deliveryDate;
	 * 
	 * String memo;
	 * 
	 * String reference;
	 * 
	 * boolean isPaid = false;
	 * 
	 * double payments = 0D;
	 * 
	 * double balanceDue = 0D;
	 * 
	 * Account accountsPayable;
	 * 
	 * Set<TransactionPayBill> transactionPayBills = new
	 * HashSet<TransactionPayBill>();
	 */

	public void setDueDate(FinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;

	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public String toString() {
		return "Supplier Enter Bill";
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.vendor;
	}

	public boolean equals(EnterBill obj) {

		if (this.vendor.id == obj.vendor.id

				&& ((this.purchaseOrder != null && obj.purchaseOrder != null) ? (this.purchaseOrder
						.equals(obj.purchaseOrder)) : true)
				&& ((this.itemReceipt != null && obj.itemReceipt != null) ? (this.itemReceipt
						.equals(obj.itemReceipt)) : true)
				&& ((!DecimalUtil.isEquals(this.total, 0.0) && !DecimalUtil
						.isEquals(obj.total, 0.0)) ? DecimalUtil.isEquals(
						this.total, obj.total) : true)
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

		EnterBill enterBill = (EnterBill) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		this.balanceDue = (!DecimalUtil.isGreaterThan((this.total - payments),
				0.0)) ? 0.0 : (this.total - payments);
		// this.balanceDue = (this.balanceDue = this.total - payments) == 0.0 ?
		// 0.0 : this.balanceDue;
		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if ((this.isVoid && !enterBill.isVoid)
				|| (this.isDeleted() && !enterBill.isDeleted() && !this.isVoid)) {

			doVoidEffect(session, this);

		} else if (!this.equals(enterBill)) {

			this.cleanTransactionitems(this);

			/**
			 * If cloned and client Object vendors are not same then update
			 * cloned and client Object vendor balances and PurchaseOrder
			 */

			if (enterBill.vendor.id != this.vendor.id) {

				// doVoidEffect(session, enterBill);

				Vendor vendor = (Vendor) session.get(Vendor.class,
						enterBill.vendor.id);
				vendor.updateBalance(session, this, -enterBill.total);

				// this.onSave(session);

				this.vendor.updateBalance(session, this, this.total);

				modifyPurchaseOrder(enterBill, false);
				modifyPurchaseOrder(this, true);

				// modifyItemReceipt(this, true);
				// modifyItemReceipt(enterBill, false);

				return;

			}
			if (this.purchaseOrder != null || enterBill.purchaseOrder != null)
				if (this.purchaseOrder == null
						&& enterBill.purchaseOrder != null) {
					modifyPurchaseOrder(enterBill, false);
				} else if (this.purchaseOrder != null
						&& enterBill.purchaseOrder == null) {
					modifyPurchaseOrder(this, true);
				} else if (!this.purchaseOrder.equals(enterBill.purchaseOrder)) {
					modifyPurchaseOrder(enterBill, false);
					modifyPurchaseOrder(this, true);

				} else {
					for (TransactionItem transactionItem : enterBill.transactionItems) {
						if (transactionItem.referringTransactionItem != null
								&& DecimalUtil
										.isGreaterThan(
												transactionItem.referringTransactionItem.usedamt,
												0)) {
							transactionItem.referringTransactionItem.usedamt -= transactionItem.lineTotal;
						}
					}
					modifyPurchaseOrder(this, true);

				}
			else if (enterBill.vendor.id == this.vendor.id) {

				/*
				 * If cloned and client Object vendors are same then update
				 * balances and PurchaseOrder
				 */

				if (DecimalUtil.isGreaterThan(enterBill.total, this.total)) {
					modifyPurchaseOrder(this, false);
					// modifyItemReceipt(this, false);

				} else if (DecimalUtil.isLessThan(enterBill.total, this.total)) {
					modifyPurchaseOrder(this, true);
					// modifyItemReceipt(this, true);
				}

				this.vendor.updateBalance(session, this, -1
						* (enterBill.total - this.total));
			}

			/*
			 * Updating PayBills if any created by using this EnterBill
			 */
			this.updateTransactionPayBills();
		}

		super.onEdit(enterBill);
	}

	private void updateTransactionPayBills() {

		if (this.transactionPayBills != null) {
			Double amtdue = this.total;
			for (TransactionPayBill tpb : this.transactionPayBills) {
				amtdue = tpb.updatePayments(amtdue);
				tpb.setAmountDue(this.total);
				tpb.setOriginalAmount(this.total);
				HibernateUtil.getCurrentSession().saveOrUpdate(tpb);
			}
		}

	}

	private void doVoidEffect(Session session, EnterBill enterBill) {

		if (enterBill.transactionPayBills != null) {
			for (TransactionPayBill tpb : enterBill.transactionPayBills) {
				tpb.onVoidEffect(session);
				session.saveOrUpdate(tpb);
			}
		}

		if (enterBill.status != Transaction.STATUS_DELETED)
			enterBill.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

		enterBill.payments = enterBill.total;
		enterBill.balanceDue = 0.0;

		/*
		 * If clonedObject EnterBill is created by using ItemReceipt which is
		 * created by using PyrchaseOrder then decrease the PurchaseOrder used
		 * amount
		 */

		if (enterBill.itemReceipt != null
				&& enterBill.itemReceipt.purchaseOrder != null) {
			for (TransactionItem transactionItem : enterBill.itemReceipt.transactionItems) {
				TransactionItem referringTransactionItem = transactionItem.referringTransactionItem;
				if (referringTransactionItem != null
						&& !referringTransactionItem.isVoid()) {
					referringTransactionItem.usedamt -= transactionItem.lineTotal;
				}
				session.saveOrUpdate(transactionItem);

			}
			enterBill.itemReceipt.purchaseOrder.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;

			enterBill.itemReceipt.purchaseOrder.onUpdate(session);
			session.saveOrUpdate(enterBill.itemReceipt.purchaseOrder);
		}

		modifyPurchaseOrder(enterBill, false);

		// modifyItemReceipt(enterBill, false);
		// if (enterBill.purchaseOrder != null) {
		// enterBill.purchaseOrder.status = Transaction.STATUS_CANCELLED;
		// }

	}

	private void modifyPurchaseOrder(EnterBill enterBill, boolean isAddition) {

		if (enterBill.purchaseOrder == null)
			return;

		Session session = HibernateUtil.getCurrentSession();
		PurchaseOrder purchaseOrder = (PurchaseOrder) session.get(
				PurchaseOrder.class, enterBill.purchaseOrder.id);

		if (purchaseOrder != null) {
			boolean isPartialEnterBill = false;
			boolean flag = true;

			if (enterBill.transactionItems != null
					&& enterBill.transactionItems.size() > 0) {

				// for (TransactionItem transactionItem :
				// enterBill.transactionItems) {
				//
				// if (transactionItem.referringTransactionItem != null) {
				//
				// TransactionItem referringTransactionItem = (TransactionItem)
				// session
				// .get(
				// TransactionItem.class,
				// transactionItem.referringTransactionItem
				// .getID());
				// //
				// double amount = referringTransactionItem.usedamt;
				//
				// if (transactionItem.type == TransactionItem.TYPE_ITEM)
				// transactionItem.lineTotal = transactionItem.quantity
				// * referringTransactionItem.unitPrice;
				//
				// else
				// transactionItem.lineTotal = transactionItem.lineTotal;
				//
				// if (id == 0l)
				// referringTransactionItem.usedamt +=
				// transactionItem.lineTotal;
				//
				// else if (id != 0l && !enterBill.isVoid())
				// referringTransactionItem.usedamt += transactionItem.quantity
				// * referringTransactionItem.unitPrice;
				//
				// // else if (id != 0l && !enterBill.isVoid())
				// // referringTransactionItem.usedamt +=
				// // transactionItem.lineTotal
				// // - referringTransactionItem.usedamt;
				//
				// if (enterBill.isVoid())
				// referringTransactionItem.usedamt -=
				// transactionItem.lineTotal;
				//
				// session.update(referringTransactionItem);
				//
				// if (flag
				// && ((transactionItem.type == TransactionItem.TYPE_ACCOUNT
				// || transactionItem.type == TransactionItem.TYPE_SALESTAX ||
				// (transactionItem.type == TransactionItem.TYPE_ITEM &&
				// transactionItem.quantity <
				// referringTransactionItem.quantity)))) {
				// if (isAddition ? referringTransactionItem.usedamt <
				// referringTransactionItem.lineTotal
				// : referringTransactionItem.usedamt > 0) {
				// isPartialEnterBill = true;
				// flag = false;
				// }
				// }
				//
				// // if (id != 0l && !enterBill.isVoid())
				// // referringTransactionItem.usedamt += amount;
				//
				// // session.update(referringTransactionItem);
				// }
				//
				// }
			}
			for (TransactionItem transactionItem : enterBill.transactionItems) {
				/**
				 * This is to know whether this transaction item is of new one
				 * or it's came from any Sales Order.
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
							&& ((transactionItem.type == TransactionItem.TYPE_ACCOUNT
									|| transactionItem.type == TransactionItem.TYPE_SALESTAX || ((transactionItem.type == TransactionItem.TYPE_ITEM) && transactionItem
									.getQuantity().compareTo(
											referringTransactionItem
													.getQuantity()) < 0)))) {

						if (isAddition ? DecimalUtil.isLessThan(amount,
								referringTransactionItem.lineTotal)
								: DecimalUtil.isGreaterThan(amount, 0)) {
							isPartialEnterBill = true;
							flag = false;
						}
					}
				}

			}

			if (!isPartialEnterBill) {
				double usdAmount = 0;
				for (TransactionItem orderTransactionItem : purchaseOrder.transactionItems)
					// if (orderTransactionItem.getType() != 6)
					usdAmount += orderTransactionItem.usedamt;
				// else
				// usdAmount += orderTransactionItem.lineTotal;
				if (DecimalUtil.isLessThan(usdAmount, purchaseOrder.netAmount))
					isPartialEnterBill = true;
			}
			if (isPartialEnterBill) {
				purchaseOrder.status = Transaction.STATUS_OPEN;
			} else {
				purchaseOrder.status = isAddition ? Transaction.STATUS_COMPLETED
						: Transaction.STATUS_OPEN;
			}

			purchaseOrder.onUpdate(session);
			session.saveOrUpdate(purchaseOrder);
		}
	}

	private void modifyItemReceipt(EnterBill enterBill, boolean isAddition) {
		Session session = HibernateUtil.getCurrentSession();

		if (enterBill.itemReceipt != null) {

			if (enterBill.transactionItems != null
					&& enterBill.transactionItems.size() > 0) {

				for (TransactionItem transactionItem : this.transactionItems) {
					TransactionItem referringTransactionItem = transactionItem.referringTransactionItem;

					if (referringTransactionItem != null) {
						if (!isAddition)
							if (transactionItem.type == transactionItem.TYPE_ITEM) {
								referringTransactionItem.usedamt -= transactionItem
										.getQuantity()
										.calculatePrice(
												referringTransactionItem.unitPrice);
							} else
								referringTransactionItem.usedamt -= referringTransactionItem.usedamt
										- transactionItem.lineTotal;

						else {
							if (transactionItem.type == TransactionItem.TYPE_ITEM) {
								referringTransactionItem.usedamt = transactionItem
										.getQuantity()
										.calculatePrice(
												referringTransactionItem.unitPrice);
							} else
								referringTransactionItem.usedamt += transactionItem.lineTotal
										- referringTransactionItem.usedamt;
						}
						session.update(referringTransactionItem);
					}
				}
			}
			enterBill.itemReceipt.onUpdate(session);
			session.saveOrUpdate(enterBill.itemReceipt);
		}
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		if (this.transactionPayBills != null) {
			for (TransactionPayBill transactionPayBill : this.transactionPayBills) {
				if (DecimalUtil.isGreaterThan(
						transactionPayBill.appliedCredits, 0d)
						|| DecimalUtil.isGreaterThan(
								transactionPayBill.cashDiscount, 0d)) {
					throw new AccounterException(
							AccounterException.ERROR_CANT_EDIT);
					// "EnterBill can't be edited because  cashDiscount hasbeen applied on this payment EnterBill No:"
					// + this.number);
				}
			}

		}

		if (this.status == Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED
				|| this.status == Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED) {
			throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
			// "You have already paid some amount for this Bill, You can't Edit and Void it.");
		}

		return super.canEdit(clientObject);
	}
}
