package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * @author Suresh Garikapati <br>
 *         <b>Cash Sale</b><br>
 * <br>
 *         <b><i>Effect on Transaction Item</i></b><br>
 * <br>
 *         ===============================>Item <br>
 * 
 *         If Item's IncomeAccount isIncrease true then Increasae the current
 *         and total balance by line total otherwise Decrease.<br>
 * <br>
 *         ===============================>Account <br>
 * 
 *         If Account isIncrease true then Increasae the current and total
 *         balance by line total otherwise Decrease.<br>
 * <br>
 *         ===============================>Sales Tax <br>
 * 
 *         If Specified TaxCode's all Tax Agency's Liability Account isIncrease
 *         true then Increase the current and total balance by line total
 *         otherwise decrease. Specified TaxCode's all Tax Agency's Balance will
 *         Increase by line total . <br>
 * <br>
 *         <b><i>Effect on Tax Group</i></b><br>
 * <br>
 *         Specified TaxGroup's all TaxCode's Tax Agency's Liability Account
 *         isIncrease true then Increase the current and total balance by sales
 *         tax collected for each taxcode otherwise decrease. Specified
 *         TaxGroup's all TaxCode's Tax Agency's Balance will Increase by sales
 *         tax collected for each taxcode. <br>
 * <br>
 *         <b><i> Effect on Deposited In Account</i></b><br>
 * 
 *         If Specified Deposit In Account(cash|Bank|Credit Card) isIncrease
 *         true then Decrease the current and total balance by CashSale total
 *         otherwise Increase
 * 
 * 
 */
public class CashSales extends Transaction implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The payee to whom the sales is being done.
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * The contact of the payee. (His alternate phone number, email, primary
	 * address etc are entered
	 */
	Contact contact;

	/**
	 * This is the address to where we have to send the bill for the sale. This
	 * can different from payee's primary address.
	 */
	Address billingAddress;

	/**
	 * This is the address to where we have to send the goods which he ordered
	 * in the sale. This can different from payee's primary address and billing
	 * address.
	 */
	Address shippingAdress;

	String phone;

	/**
	 * This can be used whenever the payee is assigned with a dedicated Sales
	 * person for all the sales. We can change this person as well.
	 */
	SalesPerson salesPerson;

	/**
	 * This account is used to deposit the cash-sale total.
	 */
	@ReffereredObject
	Account depositIn;

	/**
	 * We can specify any shipping term on the request of customer
	 */
	ShippingTerms shippingTerm;

	/**
	 * The method how the shipping of the goods is done to the customer.
	 */
	ShippingMethod shippingMethod;

	/**
	 * The date on/before which the goods has to be reached to the customer.
	 */
	FinanceDate deliverydate;

	/**
	 * For loyal dedicated customers we can mention a price level. By this price
	 * level the overall price of the cash sale can be reduced to a percentage
	 * mentioned in the price level.
	 */
	PriceLevel priceLevel;

	double taxTotal = 0D;

	double discountTotal = 0D;

	/**
	 * Constructor of CashSales
	 */
	public CashSales() {

	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
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
	 * @return the billingAddress
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddress
	 *            the billingAddress to set
	 */
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the shippingAdress
	 */
	public Address getShippingAdress() {
		return shippingAdress;
	}

	/**
	 * @param shippingAdress
	 *            the shippingAdress to set
	 */
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
	 * @param phone
	 *            the phone to set
	 */
	// public void setPhone(String phone) {
	// this.phone = phone;
	// }
	/**
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
	 * @return the depositIn
	 */
	public Account getDepositIn() {
		return depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(Account depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the shippingTerm
	 */
	public ShippingTerms getShippingTerm() {
		return shippingTerm;
	}

	/**
	 * @param shippingTerm
	 *            the shippingTerm to set
	 */
	public void setShippingTerm(ShippingTerms shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @param shippingMethod
	 *            the shippingMethod to set
	 */
	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the delivery FinanceDate
	 */
	public FinanceDate getDeliverydate() {
		return deliverydate;
	}

	/**
	 * @param deliverydate
	 *            the delivery date to set
	 */
	// public void setDeliverydate(Date delivery date) {
	// this.deliverydate = delivery date;
	// }
	/**
	 * @return the priceLevel
	 */
	public PriceLevel getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @param priceLevel
	 *            the priceLevel to set
	 */
	public void setPriceLevel(PriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	/**
	 * @return the salesTax
	 */
	public double getTaxTotal() {
		return taxTotal;
	}

	/**
	 * @param salesTax
	 *            the salesTax to set
	 */
	// public void setSalesTax(double salesTax) {
	// this.salesTax = salesTax;
	// }
	/**
	 * @return the allLineTotal
	 */
	public double getAllLineTotal() {
		return subTotal;
	}

	/**
	 * @param allLineTotal
	 *            the allLineTotal to set
	 */
	// public void setAllLineTotal(double allLineTotal) {
	// this.allLineTotal = allLineTotal;
	// }
	/**
	 * @return the allTaxableLineTotal
	 */
	public double getAllTaxableLineTotal() {
		return totalTaxableAmount;
	}

	/**
	 * @param allTaxableLineTotal
	 *            the allTaxableLineTotal to set
	 */
	// public void setAllTaxableLineTotal(double allTaxableLineTotal) {
	// this.allTaxableLineTotal = allTaxableLineTotal;
	// }
	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal() {
		return discountTotal;
	}

	/**
	 * @param discountTotal
	 *            the discountTotal to set
	 */
	// public void setDiscountTotal(double discountTotal) {
	// this.discountTotal = discountTotal;
	// }
	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	// public void setIsVoid(boolean isVoid) {
	// this.isVoid = isVoid;
	// }
	/**
	 * @return the isDeposited
	 */
	@Override
	public boolean getIsDeposited() {
		return isDeposited;
	}

	/**
	 * @param isDeposited
	 *            the isDeposited to set
	 */
	// public void setIsDeposited(boolean isDeposited) {
	// this.isDeposited = isDeposited;
	// }
	// @Override
	// public void setTransactionItems(List<TransactionItem> transactionItems) {
	// super.setTransactionItems(transactionItems);
	//
	// this.allLineTotal = getLineTotalSum();
	// this.allTaxableLineTotal = getTaxableLineTotalSum();
	// this.discountTotal = getDiscountTotalSum();
	//
	// }
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
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		//
		// if (isBecameVoid()) {
		//
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
	public boolean isPositiveTransaction() {
		return true;
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

		return AccounterServerConstants.TYPE_CASH_SALES;
	}

	/**
	 * Comparison of two cash sales
	 * 
	 * @param CashSale
	 * 
	 * @return True if both objects are equal else false
	 */
	// public boolean equals(CashSales cs) {
	//
	// if (this.customer.equals(cs.customer)
	// && this.contact.equals(cs.contact)
	// && this.billingAddress.equals(billingAddress)
	// && this.shippingAdress.equals(shippingAdress)
	// && this.phone.equals(cs.phone)
	// && this.salesPerson.equals(cs.salesPerson)
	// && this.paymentMethod.equals(cs.paymentMethod)
	// && this.depositIn.equals(cs.depositIn)
	// && this.shippingTerm.equals(cs.shippingTerm)
	// && this.shippingMethod.equals(cs.shippingMethod)
	// && this.deliverydate.equals(cs.deliverydate)
	// && this.memo.equals(cs.memo)
	// && this.reference.equals(cs.reference)
	// && this.priceLevel.equals(cs.priceLevel)
	// && this.salesTax == cs.salesTax
	// // && this.allLineTotal == cs.allLineTotal
	// // && this.allTaxableLineTotal == cs.allTaxableLineTotal
	// && this.discountTotal == cs.discountTotal)
	// return true;
	// return false;
	// }

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public void setTaxTotal(double salesTax) {
		this.taxTotal = salesTax;
	}

	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
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
	public Payee getInvolvedPayee() {
		return this.customer;
	}

	// @Override
	public boolean equals(CashSales cs) {
		if (this.transactionItems.size() == cs.transactionItems.size()
				&& this.id == cs.id
				&& DecimalUtil.isEquals(this.getTotal(), cs.getTotal())
				// && (this.customer != null && cs.customer != null) ?
				// (this.customer.equals(cs.customer)): true
				&& ((this.getDepositIn() != null && cs.getDepositIn() != null) ? (this
						.getDepositIn().equals(cs.getDepositIn())) : true)
				&& ((this.paymentMethod != null && cs.paymentMethod != null) ? (this.paymentMethod
						.equals(cs.paymentMethod)) : true)) {
			for (int i = 0; i < this.transactionItems.size(); i++) {
				if (!this.transactionItems.get(i).equals(
						cs.transactionItems.get(i)))
					return false;
			}
			return true;
		}
		return false;

	}

	/**
	 * This method is called when we save in the edit mode
	 */

	@Override
	public void onEdit(Transaction clonedObject) {
		CashSales cashSales = (CashSales) clonedObject;
		Session session = HibernateUtil.getCurrentSession();
		/**
		 * 
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then only it will entered into the loop
		 */
		if ((this.isVoid && !cashSales.isVoid)
				|| (this.isDeleted() && !cashSales.isDeleted() && !this.isVoid)) {

		} else if (!cashSales.equals(this)) {

			this.cleanTransactionitems(this);

			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;

			if ((cashSales.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) || cashSales.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))
					&& (!this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) && !this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

			/**
			 * if we compare depositIn accounts the condition never satisfies so
			 * if we compare total and the account is same then first previous
			 * total will be decreased and present total will be increased
			 */

			// if (cashSales.depositIn.id != this.depositIn.id) {
			Account preDepositAccnt = (Account) session.get(Account.class,
					cashSales.depositIn.id);
			preDepositAccnt.updateCurrentBalance(this, cashSales.total);
			preDepositAccnt.onUpdate(session);

			this.depositIn.updateCurrentBalance(this, -this.total);
			this.depositIn.onUpdate(session);

			// }
			// if(cashSales.total!=this.total){
			// this.depositIn.updateCurrentBalance(this,
			// this.total-cashSales.total);
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
