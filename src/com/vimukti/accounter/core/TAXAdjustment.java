package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Session;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * A VATAdjustment is to be done when there are amounts in uncategorised amounts
 * box of File VAT or when we feel that the vat return boxes amounts are not
 * correct. It has a VATItem which we are going to update the amount, an
 * adjustment account which acts as a debit account for this transaction, a
 * reference to journal entry as a new journal entry will be created.
 * 
 * @author Chandan
 * 
 */
public class TAXAdjustment extends Transaction implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7521518095162566582L;

	boolean increaseVATLine;
	boolean isFiled;

	@ReffereredObject
	Account adjustmentAccount;

	@ReffereredObject
	TAXItem taxItem;

	@ReffereredObject
	TAXAgency taxAgency;

	@ReffereredObject
	JournalEntry journalEntry;

	/**
	 * @return the increaseVATLine
	 */
	public Boolean getIncreaseVATLine() {
		return increaseVATLine;
	}

	/**
	 * @param increaseVATLine
	 *            the increaseVATLine to set
	 */
	public void setIncreaseVATLine(Boolean increaseVATLine) {
		this.increaseVATLine = increaseVATLine;
	}

	/**
	 * @return the isFiled
	 */
	public Boolean isFiled() {
		return isFiled;
	}

	/**
	 * @param isFiled
	 *            the isFiled to set
	 */
	public void setIsFiled(Boolean isFiled) {
		this.isFiled = isFiled;
	}

	/**
	 * @return the adjustmentAccount
	 */
	public Account getAdjustmentAccount() {
		return adjustmentAccount;
	}

	/**
	 * @param adjustmentAccount
	 *            the adjustmentAccount to set
	 */
	public void setAdjustmentAccount(Account adjustmentAccount) {
		this.adjustmentAccount = adjustmentAccount;
	}

	/**
	 * @return the vatItem
	 */
	public TAXItem getTaxItem() {
		return taxItem;
	}

	/**
	 * @param vatItem
	 *            the vatItem to set
	 */
	public void setTaxItem(TAXItem taxItem) {
		this.taxItem = taxItem;
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

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// currently not using anywhere in the project.

	}

	public Account getEffectingAccount() {

		return null;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;

		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			this.setType(Transaction.TYPE_ADJUST_VAT_RETURN);

			// Query query = session.getNamedQuery("getNextTransactionNumber");
			// query.setLong("type", Transaction.TYPE_JOURNAL_ENTRY);
			// List list = query.list();
			// //
			// long nextVoucherNumber = 1;
			// if (list != null && list.size() > 0) {
			// nextVoucherNumber = ((Long) list.get(0)).longValue() + 1;
			// }

			JournalEntry vatAdjustmentJournalEntry = new JournalEntry(this,
					number, JournalEntry.TYPE_NORMAL_JOURNAL_ENTRY);

			this.journalEntry = vatAdjustmentJournalEntry;
		} finally {
			session.setFlushMode(flushMode);
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		super.onUpdate(s);
		return false;
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
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.getPayee();
	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return true;
	}

	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

}
