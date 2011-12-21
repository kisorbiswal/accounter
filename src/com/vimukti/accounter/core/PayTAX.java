package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * It corresponds to paying the VAT to VATAgency. It includes payFrom account,
 * VATAgency to which we are paying, and the total amount we have to pay. It
 * also includes list of TransactionPayVAT which shows all the vat amounts in
 * vat code order.
 * 
 * @author Chandan
 * 
 */
public class PayTAX extends Transaction implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2303963552649468306L;

	/**
	 * PayFrom {@link Account},
	 */
	@ReffereredObject
	Account payFrom;

	/**
	 * Bills Due On or Before.
	 */
	FinanceDate returnsDueOnOrBefore;

	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	boolean isEdited = false;

	private String checkNumber;

	List<TransactionPayTAX> transactionPayTAX;

	//

	/**
	 * @return the payFrom
	 */
	public Account getPayFrom() {
		return payFrom;
	}

	/**
	 * @param payFrom
	 *            the payFrom to set
	 */
	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
	}

	/**
	 * @return the returnsDueOnOrBefore
	 */
	public FinanceDate getReturnsDueOnOrBefore() {
		return returnsDueOnOrBefore;
	}

	/**
	 * @param returnsDueOnOrBefore
	 *            the returnsDueOnOrBefore to set
	 */
	public void setReturnsDueOnOrBefore(FinanceDate returnsDueOnOrBefore) {
		this.returnsDueOnOrBefore = returnsDueOnOrBefore;
	}

	/**
	 * @return the vatAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the isEdited
	 */
	public boolean isEdited() {
		return isEdited;
	}

	/**
	 * @param isEdited
	 *            the isEdited to set
	 */
	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	/**
	 * @return the transactionPayVAT
	 */
	public List<TransactionPayTAX> getTransactionPayVAT() {
		return transactionPayTAX;
	}

	/**
	 * @param transactionPayVAT
	 *            the transactionPayVAT to set
	 */
	public void setTransactionPayVAT(List<TransactionPayTAX> transactionPayVAT) {
		this.transactionPayTAX = transactionPayVAT;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// NOTHING TO DO.
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		for (TransactionPayTAX t : transactionPayTAX) {
			t.setPayTAX(this);
		}
		if (this.getID() == 0) {
			super.onSave(session);

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
		if (isBecameVoid()) {
			doVoidEffect(session);
		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {
		PayTAX oldPayTAX = (PayTAX) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (isDraftOrTemplate()) {
			return;
		}

		if (this.isVoid() && !oldPayTAX.isVoid()) {
			doVoidEffect(session);
		} else {
			if ((payFrom.getID() != oldPayTAX.payFrom.getID())
					|| !DecimalUtil.isEquals(this.total, oldPayTAX.total)) {
				Account account = (Account) session.get(Account.class,
						payFrom.getID());
				if (account != null) {
					account.updateCurrentBalance(this, -oldPayTAX.getTotal(),
							oldPayTAX.getCurrencyFactor());
					session.update(account);
				}

				payFrom.updateCurrentBalance(this, total, this.currencyFactor);
				session.saveOrUpdate(payFrom);
			}
			oldPayTAX.transactionPayTAX.clear();
			for (TransactionPayTAX tpt : this.transactionPayTAX) {
				tpt.setPayTAX(this);
			}
		}
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid()) {
			doVoidEffect(session);
		}
		return super.onDelete(session);
	}

	private void doVoidEffect(Session session) {
		this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		payFrom.updateCurrentBalance(this, -total, this.currencyFactor);
		session.saveOrUpdate(payFrom);
		if (this.transactionPayTAX != null) {
			transactionPayTAX.clear();
		}
	}

	@Override
	public Account getEffectingAccount() {
		return this.payFrom;
	}

	@Override
	public Payee getInvolvedPayee() {

		return null;
	}

	@Override
	public Payee getPayee() {

		return null;
	}

	@Override
	public int getTransactionCategory() {

		return 0;
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
	public String toString() {

		return AccounterServerConstants.TYPE_PAY_VAT;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		if (this.isVoid()) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}
		return true;
	}

	@Override
	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = super.getEffectingAccountsWithAmounts();
		for (TransactionPayTAX vat : transactionPayTAX) {
			map.put(vat.getTaxAgency().getAccount(), vat.getAmountToPay());
			map.put(vat.getVatReturn().getTaxAgency()
					.getFiledLiabilityAccount(), vat.getAmountToPay());
		}
		return map;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.payTax()).gap();
		w.put(messages.no(), this.number);
		w.put(messages.date(), this.transactionDate.toString()).gap();
		w.put(messages.currency(), this.currencyFactor);
		w.put(messages.amount(), this.total).gap();
		w.put(messages.paymentMethod(), this.paymentMethod).gap();
		w.put(messages.memo(), this.memo);

	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

}
