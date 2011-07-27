package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.InvalidOperationException;

/**
 * 
 * A VATReturn object is created when we want to File the VAT that the company
 * is owed to the HMRC.
 * 
 * @author Ravi Chandan
 * 
 */
public class VATReturn extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -155771928048866705L;
	/**
	 * The Start date of this VAT Return
	 */
	FinanceDate VATperiodStartDate;
	/**
	 * The End date of this VAT Return
	 */
	FinanceDate VATperiodEndDate;
	/**
	 * The VAT Agency to which we are going to create this VAT Return
	 * 
	 */
	TAXAgency taxAgency;
	//

	List<Box> boxes = new ArrayList<Box>();

	JournalEntry journalEntry;

	double balance;

	public static final int VAT_RETURN_UK_VAT = 1;
	public static final int VAT_RETURN_IRELAND = 2;
	public static final int VAT_RETURN_NONE = 3;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the journalEntry
	 */
	public JournalEntry getJournalEntry() {
		return journalEntry;
	}

	/**
	 * @param journalEntry
	 *            the journalEntry to set
	 */
	public void setJournalEntry(JournalEntry journalEntry) {
		this.journalEntry = journalEntry;
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

	/**
	 * @return the boxes
	 */
	public List<Box> getBoxes() {
		return boxes;
	}

	/**
	 * @param boxes
	 *            the boxes to set
	 */
	public void setBoxes(List<Box> boxes) {
		this.boxes = boxes;
	}

	public FinanceDate getVATperiodStartDate() {
		return VATperiodStartDate;
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

	public void setVATperiodStartDate(FinanceDate tperiodStartDate) {
		VATperiodStartDate = tperiodStartDate;
	}

	public FinanceDate getVATperiodEndDate() {
		return VATperiodEndDate;
	}

	public void setVATperiodEndDate(FinanceDate tperiodEndDate) {
		VATperiodEndDate = tperiodEndDate;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		// not using
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// not using

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onSave(Session session) throws CallbackException {

		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);

		try {

			if (this.isOnSaveProccessed)
				return true;
			this.isOnSaveProccessed = true;
			this.total = this.boxes.get(4).getAmount();
			this.balance = this.total;
			this.type = Transaction.TYPE_VAT_RETURN;

			FinanceLogger.log("VatReturn with number: {0} going to save",
					String.valueOf(this.getNumber()));

			this.taxAgency.updateBalance(session, this, this.total);

			// this.taxAgency.salesLiabilityAccount.updateCurrentBalance(this,
			// -this.boxes.get(2).getAmount());
			// session.update(this.taxAgency.salesLiabilityAccount);
			// this.taxAgency.salesLiabilityAccount.onUpdate(session);
			//
			// this.taxAgency.purchaseLiabilityAccount.updateCurrentBalance(this,
			// this.boxes.get(3).getAmount());
			// session.update(this.taxAgency.purchaseLiabilityAccount);
			// this.taxAgency.purchaseLiabilityAccount.onUpdate(session);

			Query query = session
					.createQuery(
							"from com.vimukti.accounter.core.TAXAdjustment v where v.transactionDate between :fromDate and :toDate and v.isFiled = false")
					.setParameter("fromDate", this.VATperiodStartDate)
					.setParameter("toDate", this.VATperiodEndDate);

			List<TAXAdjustment> vadj = query.list();
			if (vadj != null) {
				for (TAXAdjustment va : vadj) {
					va.setIsFiled(true);
					session.update(va);
				}
			}

			query = session
					.createQuery(
							"from com.vimukti.accounter.core.TAXRateCalculation vr where vr.taxItem is not null and vr.taxAgency.id=:vatAgency and vr.transactionDate <= :toDate and vr.vatReturn is null")
					.setParameter("toDate", this.VATperiodEndDate)
					.setParameter("vatAgency", taxAgency.getID());

			this.setJournalEntry(new JournalEntry(this));

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

		} finally {
			session.setFlushMode(flushMode);
		}

	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		if (isBecameVoid()) {

			FinanceLogger
					.log("VatReturn with number: {0} going to RollBack All effects ",
							String.valueOf(this.getNumber()));

			this.taxAgency.updateBalance(session, this, -1 * this.total);

			this.taxAgency.salesLiabilityAccount.updateCurrentBalance(this,
					this.boxes.get(2).getAmount());
			this.taxAgency.purchaseLiabilityAccount.updateCurrentBalance(this,
					this.boxes.get(3).getAmount());
			// Company.getCompany().getAccountsPayableAccount()
			// .updateCurrentBalance(
			// this,
			// -(total + this.boxes.get(boxes.size() - 1)
			// .getAmount()));
			//
			Query query = session
					.createQuery(
							"from com.vimukti.accounter.core.TAXRateCalculation vr where vr.vatItem is not null and vr.vatItem.vatAgency.id=:vatAgency and vr.transactionDate > :toDate and vr.vatReturn is null")
					.setParameter("toDate", this.VATperiodEndDate)
					.setParameter("vatAgency", taxAgency.getID());

			List<TAXRateCalculation> vrc = query.list();
			for (TAXRateCalculation v : vrc) {
				v.setVatReturn(null);
				session.saveOrUpdate(v);
			}

			this.balance = 0;
		}

		ChangeTracker.put(this);
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		// currently not using
		return null;
	}

	@Override
	public Payee getPayee() {
		// currently not using
		return null;
	}

	@Override
	public int getTransactionCategory() {
		// currently not using
		return 0;
	}

	@Override
	public boolean isDebitTransaction() {
		// currently not using
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		// currently not using
		return false;
	}

	@Override
	public String toString() {
		// currently not using
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.taxAgency;
	}

	public void updateBalance(double amountToPay) {

		this.balance += amountToPay;

	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {

		return true;
	}

}
