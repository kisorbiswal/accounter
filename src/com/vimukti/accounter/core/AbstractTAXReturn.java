package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;

public class AbstractTAXReturn extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The Start date of this TAX Return */
	FinanceDate periodStartDate;

	/** The End date of this TAX Return */
	FinanceDate periodEndDate;

	/** TAX Returning Date */
	FinanceDate returningDate;

	/**
	 * The TAX Agency to which we are going to create this TAX Return
	 */
	TAXAgency taxAgency;

	/** TAX Return Balance */
	double balance;

	double totalTAXAmount;

	double salesTaxTotal;

	double purchaseTaxTotal;

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

	public FinanceDate getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(FinanceDate tperiodStartDate) {
		periodStartDate = tperiodStartDate;
	}

	public FinanceDate getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(FinanceDate tperiodEndDate) {
		periodEndDate = tperiodEndDate;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void updateBalance(double amountToPay) {
		this.balance += amountToPay;
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Payee getInvolvedPayee() {
		return taxAgency;
	}

	/**
	 * @return the returningDate
	 */
	public FinanceDate getReturningDate() {
		return returningDate;
	}

	/**
	 * @param returningDate
	 *            the returningDate to set
	 */
	public void setReturningDate(FinanceDate returningDate) {
		this.returningDate = returningDate;
	}

	/**
	 * @return the totalTAXAmount
	 */
	public double getTotalTAXAmount() {
		return totalTAXAmount;
	}

	/**
	 * @param totalTAXAmount
	 *            the totalTAXAmount to set
	 */
	public void setTotalTAXAmount(double totalTAXAmount) {
		this.totalTAXAmount = totalTAXAmount;
	}

	@Override
	public String toString() {
		return taxAgency.getName() + ":" + periodStartDate + "-"
				+ periodEndDate + "Balance:" + balance;
	}

	protected void updateTaxLiabilityAccounts() {
		Session session = HibernateUtil.getCurrentSession();
		Account salesLiabilityAccount = taxAgency.getSalesLiabilityAccount();
		salesLiabilityAccount.updateCurrentBalance(this, -1 * salesTaxTotal);
		session.update(salesLiabilityAccount);
		salesLiabilityAccount.onUpdate(session);

		Account purchaseLiabilityAccount = taxAgency
				.getPurchaseLiabilityAccount();
		purchaseLiabilityAccount.updateCurrentBalance(this, purchaseTaxTotal);
		session.update(purchaseLiabilityAccount);
		purchaseLiabilityAccount.onUpdate(session);

		// double amount = vatReturn.getBoxes().get(4).getAmount()
		// + vatReturn.getBoxes().get(vatReturn.getBoxes().size() - 1)
		// .getAmount();
		Account vatFiledLiabilityAccount = taxAgency.getFiledLiabilityAccount();
		vatFiledLiabilityAccount
				.updateCurrentBalance(this, this.totalTAXAmount);
		session.update(vatFiledLiabilityAccount);
		vatFiledLiabilityAccount.onUpdate(session);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		super.onSave(session);
		super.onSave(session);

		// FlushMode flushMode = session.getFlushMode();
		// session.setFlushMode(FlushMode.COMMIT);

		// try {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.total = this.totalTAXAmount;
		this.balance = this.total;
		this.type = Transaction.TYPE_TAX_RETURN;

		taxAgency.updateBalance(session, this, this.total);

		// this.taxAgency.salesLiabilityAccount.updateCurrentBalance(this,
		// -this.boxes.get(2).getAmount());
		// session.update(this.taxAgency.salesLiabilityAccount);
		// this.taxAgency.salesLiabilityAccount.onUpdate(session);
		//
		// this.taxAgency.purchaseLiabilityAccount.updateCurrentBalance(this,
		// this.boxes.get(3).getAmount());
		// session.update(this.taxAgency.purchaseLiabilityAccount);
		// this.taxAgency.purchaseLiabilityAccount.onUpdate(session);

		Query query = session.getNamedQuery("getTaxAdjustment.by.dates")
				.setParameter("fromDate", this.periodStartDate)
				.setParameter("toDate", this.periodEndDate)
				.setEntity("company", getCompany());

		List<TAXAdjustment> vadj = query.list();
		if (vadj != null) {
			for (TAXAdjustment va : vadj) {
				va.setIsFiled(true);
				session.update(va);
			}
		}

		query = session.getNamedQuery("getTaxrateCalc.by.taxitem.and.details")
				.setParameter("toDate", this.periodEndDate)
				.setParameter("vatAgency", taxAgency.getID())
				.setEntity("company", getCompany());

		// this.setJournalEntry(new JournalEntry(this));
		updateTaxLiabilityAccounts();

		List<TAXRateCalculation> vrc = query.list();
		// org.hibernate.Transaction t = session.beginTransaction();
		for (TAXRateCalculation v : vrc) {
			v.vatReturn = this;
			session.update(v);
		}
		// t.commit();

		// Company.getCompany().getAccountsPayableAccount().updateCurrentBalance(
		// this, (total + this.boxes.get(boxes.size() - 1).getAmount()));

		ChangeTracker.put(this);
		return false;

		// } finally {
		// session.setFlushMode(flushMode);
		// }
	}

	/**
	 * @return the salesTaxTotal
	 */
	public double getSalesTaxTotal() {
		return salesTaxTotal;
	}

	/**
	 * @param salesTaxTotal
	 *            the salesTaxTotal to set
	 */
	public void setSalesTaxTotal(double salesTaxTotal) {
		this.salesTaxTotal = salesTaxTotal;
	}

	/**
	 * @return the purchaseTaxTotal
	 */
	public double getPurchaseTaxTotal() {
		return purchaseTaxTotal;
	}

	/**
	 * @param purchaseTaxTotal
	 *            the purchaseTaxTotal to set
	 */
	public void setPurchaseTaxTotal(double purchaseTaxTotal) {
		this.purchaseTaxTotal = purchaseTaxTotal;
	}
}
