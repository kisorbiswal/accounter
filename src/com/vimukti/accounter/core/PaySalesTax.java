package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * It corresponds to paying the tax to TAXAgency. It includes payFrom account,
 * TAXAgency to which we are paying, and the total amount we have to pay. It
 * also includes list of TransactionPaySalesTax which shows all the vat amounts
 * in tax code order.
 * 
 * @author Chandan
 * 
 */
public class PaySalesTax extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6619070845012890393L;

	/**
	 * PayFrom {@link Account},
	 */
	@ReffereredObject
	Account payFrom;

	/**
	 * Bills Due On or Before.
	 */
	FinanceDate billsDueOnOrBefore;

	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	double endingBalance;

	// boolean isVoid = false;

	boolean isEdited = false;

	List<TransactionPaySalesTax> transactionPaySalesTax;

	//

	// List<TransactionPaySalesTax> transactionPaySalesTax;

	public PaySalesTax() {
		setType(Transaction.TYPE_PAY_SALES_TAX);
	}

	public boolean getIsVoid() {
		return isVoid;
	}

	public boolean getIsEdited() {
		return isEdited;
	}

	public Account getPayFrom() {
		return payFrom;
	}

	public FinanceDate getBillsDueOnOrBefore() {
		return billsDueOnOrBefore;
	}

	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	public double getEndingBalance() {
		return endingBalance;
	}

	public List<TransactionPaySalesTax> getTransactionPaySalesTax() {
		return transactionPaySalesTax;
	}

	public void setTransactionPaySalesTax(
			List<TransactionPaySalesTax> transactionPaySalesTax) {
		this.transactionPaySalesTax = transactionPaySalesTax;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (this.id == 0) {
			super.onSave(session);
			// if (Company.getCompany().accountingType ==
			// Company.ACCOUNTING_TYPE_UK) {
			// this.taxAgency.updateBalance(session, this, -1 * this.total);
			// }

			if (!(this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
					&& !(this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		super.onUpdate(session);
		if (this.isBecameVoid()) {

			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			if (this.transactionPaySalesTax != null) {
				for (TransactionPaySalesTax ti : this.transactionPaySalesTax) {

					if (ti instanceof Lifecycle) {
						Lifecycle lifeCycle = (Lifecycle) ti;
						lifeCycle.onUpdate(session);
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
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

	public void setBillsDueOnOrBefore(FinanceDate billsDueOnOrBefore) {
		this.billsDueOnOrBefore = billsDueOnOrBefore;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public int getTransactionCategory() {
		return 0;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_PAY_SALES_TAX;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.taxAgency;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		if (this.isVoid) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}
		return super.canEdit(clientObject);
	}
}
