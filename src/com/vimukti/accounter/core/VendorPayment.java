package com.vimukti.accounter.core;

import java.util.Map;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class VendorPayment extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vendor vendor;

	/** Will be used in VendorPrePayment. */
	private boolean isAmountIncludeTDS;
	double unusedAmount = 0D;

	private double tdsTotal;

	/**
	 * {@link Address} of the vendor Selected, for PayBill Transaction
	 */
	Address address;

	/**
	 * Pay From {@link Account}, for this PayBill Transaction
	 */
	@ReffereredObject
	Account payFrom;

	double endingBalance;

	String checkNumber;

	private TAXItem tdsTaxItem;

	double vendorBalance = 0D;

	boolean isToBePrinted;

	public VendorPayment() {
		setType(Transaction.TYPE_VENDOR_PAYMENT);
	}

	@Override
	protected void checkNullValues() throws AccounterException {
		checkingVendorNull(vendor, Global.get().messages().payTo());
		checkAccountNull(payFrom, Global.get().messages().payFrom());
		checkPaymentMethodNull();

	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.payBill()).gap();
		w.put(messages.no(), this.number);
		w.put(messages.date(), this.transactionDate.toString()).gap();
		w.put(messages.currency(), this.currencyFactor).gap().gap();
		w.put(messages.amount(), this.total).gap();
		w.put(messages.paymentMethod(), this.paymentMethod).gap().gap();
		w.put(messages.memo(), this.memo).gap();

	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
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

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_VENDOR;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_VENDOR_PAYMENT;
	}

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	@Override
	public Payee getInvolvedPayee() {
		return this.vendor;
	}

	@Override
	protected void updatePayee(boolean onCreate) {
		double amount = onCreate ? -total : total;
		vendor.updateBalance(HibernateUtil.getCurrentSession(), this, amount);
	}

	public boolean isAmountIncludeTDS() {
		return isAmountIncludeTDS;
	}

	public void setAmountIncludeTDS(boolean isAmountIncludeTDS) {
		this.isAmountIncludeTDS = isAmountIncludeTDS;
	}

	/**
	 * @return the unusedAmount
	 */
	public double getUnusedAmount() {
		return unusedAmount;
	}

	/**
	 * @param unusedAmount
	 *            the unusedAmount to set
	 */
	public void setUnusedAmount(double unusedAmount) {
		this.unusedAmount = unusedAmount;
	}

	/**
	 * @return the tdsTotal
	 */
	public double getTdsTotal() {
		return tdsTotal;
	}

	/**
	 * @param tdsTotal
	 *            the tdsTotal to set
	 */
	public void setTdsTotal(double tdsTotal) {
		this.tdsTotal = tdsTotal;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the payFrom
	 */
	public Account getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
	}

	/**
	 * @return the endingBalance
	 */
	public double getEndingBalance() {
		return endingBalance;
	}

	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
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
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public TAXItem getTdsTaxItem() {
		return tdsTaxItem;
	}

	public void setTdsTaxItem(TAXItem tdsTaxItem) {
		this.tdsTaxItem = tdsTaxItem;
	}

	/**
	 * @return the vendorBalance
	 */
	public double getVendorBalance() {
		return vendorBalance;
	}

	public boolean isToBePrinted() {
		return isToBePrinted;
	}

	public void setToBePrinted(boolean isToBePrinted) {
		this.isToBePrinted = isToBePrinted;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (isDraft()) {
			super.onSave(session);
			return false;
		}
		if (this.getID() == 0l) {
			if ((!this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK) && !this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}
			this.subTotal = this.total - this.unusedAmount;

			super.onSave(session);

			if (isDraftOrTemplate()) {
				return false;
			}

			if (DecimalUtil.isGreaterThan(this.getUnusedAmount(), 0.0)) {
				// insert this vendorPayment into CreditsAndPayments
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

			double amountEffectedToAccount = total - tdsTotal;
			if (!DecimalUtil.isEquals(amountEffectedToAccount, 0.00D)) {
				payFrom.updateCurrentBalance(this, amountEffectedToAccount,
						currencyFactor);
				session.update(payFrom);
				payFrom.onUpdate(HibernateUtil.getCurrentSession());
			}

			// Update TDS Account if TDSEnabled
			if (getCompany().getPreferences().isTDSEnabled()
					&& this.getVendor().isTdsApplicable()) {
				if (DecimalUtil.isGreaterThan(tdsTotal, 0.00D)) {
					TAXItem taxItem = this.getTdsTaxItem();
					if (taxItem != null) {
						addTAXRateCalculation(taxItem, amountEffectedToAccount,
								false);
					}
				}
			}

		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {
		Session session = HibernateUtil.getCurrentSession();
		VendorPayment vendorPayment = (VendorPayment) clonedObject;

		if (isDraftOrTemplate()) {
			super.onEdit(vendorPayment);
			return;
		}

		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if (this.isVoid() && !vendorPayment.isVoid()) {
			doVoidEffect(session, this);
		} else {
			editVendorPayment(vendorPayment);
		}

		super.onEdit(vendorPayment);
	}

	private void doVoidEffect(Session session, VendorPayment vendorPayment) {

		vendorPayment.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;

		if (DecimalUtil.isGreaterThan(vendorPayment.getUnusedAmount(), 0.0)) {
			voidCreditsAndPayments(vendorPayment);
		}

		if (vendorPayment.creditsAndPayments != null) {

			vendorPayment.creditsAndPayments = null;
		}

		double amountEffectedToAccount = total - tdsTotal;
		if (DecimalUtil.isGreaterThan(amountEffectedToAccount, 0.00D)) {
			payFrom.updateCurrentBalance(this, -1 * amountEffectedToAccount,
					currencyFactor);
			session.update(payFrom);
			payFrom.onUpdate(HibernateUtil.getCurrentSession());
		}
	}

	private void editVendorPayment(VendorPayment vendorPayment) {
		Session session = HibernateUtil.getCurrentSession();
		if (this.vendor.getID() != vendorPayment.vendor.getID()) {

			doVoidEffect(session, vendorPayment);
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

		double effectToAccount = vendorPayment.total - vendorPayment.tdsTotal;
		double preEffectedToAccount = this.total - this.tdsTotal;
		if (this.payFrom.getID() != vendorPayment.payFrom.getID()
				|| isCurrencyFactorChanged()) {
			Account payFromAccount = (Account) session.get(Account.class,
					vendorPayment.payFrom.getID());
			payFromAccount.updateCurrentBalance(this, -effectToAccount,
					vendorPayment.currencyFactor);
			payFromAccount.onUpdate(session);
			this.payFrom.updateCurrentBalance(this, preEffectedToAccount,
					currencyFactor);
			this.payFrom.onUpdate(session);
		} else if (!DecimalUtil.isEquals(effectToAccount, preEffectedToAccount)) {
			this.payFrom.updateCurrentBalance(this, preEffectedToAccount
					- effectToAccount, currencyFactor);
			this.payFrom.onUpdate(session);
		}

		doEffectTDS(session, vendorPayment);

		if (this.vendor.getID() == vendorPayment.vendor.getID()
				&& (!(DecimalUtil.isEquals(this.total, vendorPayment.total)) || isCurrencyFactorChanged())) {
			this.vendor.updateBalance(session, this, vendorPayment.total,
					vendorPayment.previousCurrencyFactor);
			this.vendor.updateBalance(session, this, -total, currencyFactor);
		}

		if (!this.paymentMethod
				.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK)
				&& !this.paymentMethod
						.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK)) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else {
			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		}

		if (this.vendor.getID() == vendorPayment.vendor.getID()) {
			this.creditsAndPayments.updateCreditPayments(this.total);
		}

	}

	private void doEffectTDS(Session session, VendorPayment vendorPayment) {
		cleanTransactionitems(vendorPayment);
		double amountEffectedToAccount = total - tdsTotal;
		if (getCompany().getPreferences().isTDSEnabled()
				&& this.getVendor().isTdsApplicable()) {
			if (DecimalUtil.isGreaterThan(tdsTotal, 0.00D)) {
				TAXItem taxItem = this.getTdsTaxItem();
				if (taxItem != null) {
					addTAXRateCalculation(taxItem, amountEffectedToAccount,
							false);

				}
			}
		}
	}

	@Override
	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = super.getEffectingAccountsWithAmounts();
		if (creditsAndPayments != null) {
			map.put(creditsAndPayments.getPayee().getAccount(),
					creditsAndPayments.getEffectingAmount());
		}

		if (payFrom != null) {
			map.put(payFrom, total - tdsTotal);
		}
		return map;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		return false;
	}
}
