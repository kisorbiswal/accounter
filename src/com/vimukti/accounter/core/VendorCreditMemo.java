package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * <br>
 * <b>Vendor Credit Memo</b><br>
 * <br>
 * <b><i>Effect on Transaction Item:</i></b><br>
 * <br>
 * ===============================>Item<br>
 * 
 * If Item's ExpenseAccount isIncrease true then Increase the current and total
 * balance by line total otherwise Decrease.<br>
 * 
 * <br>
 * ===============================>Account<br>
 * 
 * If Account isIncrease true then Increase the current and total balance by
 * line total otherwise Decrease.<br>
 * 
 * 
 * <br>
 * Accounts Payable account current and total balance will Decrease by the VCM
 * total. Vendor balance should Decrease by the VCM total.<br>
 * 
 * <br>
 * <b><i>Effect on CreditsAndPayments:</i></b><br>
 * 
 * New Credit and Payment is created for this vendor with Credit Amount and
 * Balance as VCM total and Memo as transaction number followed by
 * VendorCreditMemo.
 * 
 * @author Suresh Garikapati
 */

public class VendorCreditMemo extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1012905638093494410L;

	/**
	 * The vendor to whom we are creating this Vendor Credit Memo
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * The Contact information to contact the Specified Vendor
	 */
	Contact contact;

	/**
	 * The phone number to contact the Specified Vendor
	 */
	String phone;

	double balanceDue = 0D;

	//

	/**
	 * @return the version
	 */
	public VendorCreditMemo() {
		setType(Transaction.TYPE_VENDOR_CREDIT_MEMO);
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);
		if (this.id == 0) {

			/**
			 * If the total is greater than 0.0, then we need to add to
			 * CreditsAndPayments.
			 */
			if (DecimalUtil.isGreaterThan(this.total, 0.0)) {
				this.balanceDue = this.total;
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
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		// if (isBecameVoid()) {
		// // this.creditsAndPayments.setTransaction(null);
		// // session.delete(this.creditsAndPayments);
		// this.creditsAndPayments = null;
		// this.balanceDue = 0.0;
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
		// return this.vendor;
		return null;

	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public void setNumber(String number) {
		this.number = number;
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
		return AccounterServerConstants.TYPE_VENDOR_CREDIT_MEMO;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.vendor;
	}

	public boolean equals(VendorCreditMemo obj) {
		if (this.vendor.id == obj.vendor.id
				&& (!DecimalUtil.isEquals(this.total, 0.0)
						&& !DecimalUtil.isEquals(obj.total, 0.0) ? DecimalUtil
						.isEquals(this.total, obj.total) : true)
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

		VendorCreditMemo vendorCreditMemo = (VendorCreditMemo) clonedObject;
		Session session = HibernateUtil.getCurrentSession();
		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		// if ((this.isVoid && !vendorCreditMemo.isVoid)
		// || (this.isDeleted() && !vendorCreditMemo.isDeleted() &&
		// !this.isVoid)) {
		//
		// this.vendor.updateBalance(session, this, this.total);
		//
		// } else
		if ((this.isVoid && !vendorCreditMemo.isVoid)
				|| (this.isDeleted() && !vendorCreditMemo.isDeleted() && !this.isVoid)) {

			this.balanceDue = 0d;

		} else if (!this.equals(vendorCreditMemo)) {

			this.cleanTransactionitems(this);

			/**
			 * Checking that whether two vendors are same are not. If they are
			 * not same then update clonedObject Vendor and New Vendor balances
			 * same vendors.
			 */

			if (vendorCreditMemo.vendor.id != this.vendor.id) {

				voidCreditsAndPayments(vendorCreditMemo);

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
			if ((this.vendor.equals(vendorCreditMemo.vendor))
					&& (!DecimalUtil.isEquals(this.total,
							vendorCreditMemo.total))) {

				if (DecimalUtil.isLessThan(this.total, vendorCreditMemo.total)) {
					this.vendor.updateBalance(session, this,
							vendorCreditMemo.total - this.total);
					this.creditsAndPayments.updateCreditPayments(this.total);
				} else {

					this.vendor.updateBalance(session, this,
							vendorCreditMemo.total - this.total);
					this.creditsAndPayments.updateCreditPayments(this.total);
				}

				// session.saveOrUpdate(this.creditsAndPayments);

			}

			/**
			 * Updating TransactionCreditsAndPayments to New Vendor
			 */
			// this.creditsAndPayments.updateCreditPayments(this.total);
		}

		super.onEdit(vendorCreditMemo);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return super.canEdit(clientObject);
	}

}
