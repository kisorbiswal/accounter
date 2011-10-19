package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * A VATReturn object is created when we want to File the VAT that the company
 * is owed to the HMRC.
 * 
 * @author Ravi Chandan
 * 
 */
public class VATReturn extends Transaction {

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

	@Override
	public boolean onSave(Session session) throws CallbackException {
		super.onSave(session);

		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);

		try {

			if (this.isOnSaveProccessed)
				return true;
			this.isOnSaveProccessed = true;
			this.total = this.boxes.get(4).getAmount();
			this.balance = this.total;
			this.type = Transaction.TYPE_VAT_RETURN;

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

			Query query = session.getNamedQuery("getTaxAdjustment.by.dates")
					.setParameter("fromDate", this.VATperiodStartDate)
					.setParameter("toDate", this.VATperiodEndDate)
					.setEntity("company", getCompany());

			List<TAXAdjustment> vadj = query.list();
			if (vadj != null) {
				for (TAXAdjustment va : vadj) {
					va.setIsFiled(true);
					session.update(va);
				}
			}

			query = session
					.getNamedQuery("getTaxrateCalc.by.taxitem.and.details")
					.setParameter("toDate", this.VATperiodEndDate)
					.setParameter("vatAgency", taxAgency.getID())
					.setEntity("company", getCompany());

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
		super.onUpdate(session);
		if (isBecameVoid()) {

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
					.getNamedQuery("getTaxrateCalc.by.vatitem.and.details")
					.setParameter("toDate", this.VATperiodEndDate)
					.setParameter("vatAgency", taxAgency.getID())
					.setEntity("company", getCompany());

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
			throws AccounterException {
		VATReturn vatReturn = (VATReturn) clientObject;
		List<Box> boxes2 = vatReturn.getBoxes();
		for (Box box : boxes2) {
			box.setCompany(vatReturn.getCompany());
		}
		return true;
	}

	@Override
	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = super.getEffectingAccountsWithAmounts();
		map.put(taxAgency.getAccount(), total);
		return map;
	}

}
