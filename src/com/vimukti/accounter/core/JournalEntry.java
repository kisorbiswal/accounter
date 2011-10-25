package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class JournalEntry extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7677695043049426006L;
	/**
	 * 
	 */
	public static final int TYPE_NORMAL_JOURNAL_ENTRY = 1;
	public static final int TYPE_CASH_BASIS_JOURNAL_ENTRY = 2;

	/**
	 * Transaction Journal Entry Debit Total
	 */
	double debitTotal = 0D;

	/**
	 * Transaction Journal Entry Credit Total
	 */
	double creditTotal = 0D;

	/**
	 * Type of {@link JournalEntry} , Either TYPE_NORMAL_JOURNAL_ENTRY or
	 * TYPE_CASH_BASIS_JOURNAL_ENTRY
	 */
	int journalEntryType = TYPE_NORMAL_JOURNAL_ENTRY;

	double balanceDue = 0d;

	/**
	 * A Journal Entry Has List of Entries List of Entries unlike Transaction
	 * Items,
	 * 
	 * @see TransactionItem
	 */

	Payee involvedPayee;

	// @ReffereredObject
	// Transaction transaction;

	public Set<TransactionReceivePayment> transactionReceivePayments = new HashSet<TransactionReceivePayment>();

	Set<TransactionPayBill> transactionPayBills = new HashSet<TransactionPayBill>();

	//

	public JournalEntry() {
		setType(Transaction.TYPE_JOURNAL_ENTRY);
	}

	// public JournalEntry(TAXAdjustment adjustment, String number,
	// int journalEntryType) {
	// setCompany(adjustment.getCompany());
	// this.type = Transaction.TYPE_JOURNAL_ENTRY;
	// this.journalEntryType = journalEntryType;
	// this.number = number;
	// this.transactionDate = adjustment.transactionDate;
	// this.balanceDue = adjustment.total;
	//
	// this.transaction = adjustment;
	// this.memo = "TAX Adjustment";
	//
	// Account liabilityAccount;
	// boolean isSalesType;
	//
	// liabilityAccount = adjustment.taxItem.isSalesType == true ?
	// adjustment.taxItem.taxAgency
	// .getSalesLiabilityAccount() : adjustment.taxItem.taxAgency
	// .getPurchaseLiabilityAccount();
	// isSalesType = adjustment.taxItem.isSalesType;
	//
	// Account adjustmentAccount = adjustment.getAdjustmentAccount();
	//
	// List<Entry> entries = new ArrayList<Entry>();
	//
	// Entry entry1 = new Entry();
	// entry1.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
	// entry1.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
	// entry1.setAccount(liabilityAccount);
	// entry1.setMemo(adjustmentAccount.getName());
	// entry1.setJournalEntry(this);
	// entry1.setTotal(adjustment.total);
	// entry1.setEntryDate(adjustment.getDate());
	//
	// Entry entry2 = new Entry();
	// entry2.setType(Entry.TYPE_FINANCIAL_ACCOUNT);
	// entry2.setJournalEntryType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
	// entry2.setAccount(adjustmentAccount);
	// entry2.setMemo(liabilityAccount.getName());
	// entry2.setJournalEntry(this);
	// entry2.setTotal(adjustment.total);
	// entry2.setEntryDate(adjustment.getDate());
	//
	// if (adjustment.increaseVATLine) {
	// if (isSalesType) {
	// entry1.setDebit(0d);
	// entry1.setCredit(adjustment.total);
	// entry2.setDebit(adjustment.total);
	// entry2.setCredit(0d);
	// } else {
	// entry1.setDebit(adjustment.total);
	// entry1.setCredit(0d);
	// entry2.setCredit(adjustment.total);
	// entry2.setDebit(0d);
	// }
	// } else {
	// if (isSalesType) {
	// entry1.setDebit(adjustment.total);
	// entry1.setCredit(0d);
	// entry2.setCredit(adjustment.total);
	// entry2.setDebit(0d);
	// } else {
	// entry1.setDebit(0d);
	// entry1.setCredit(adjustment.total);
	// entry2.setDebit(adjustment.total);
	// entry2.setCredit(0d);
	// }
	// }
	//
	// entries.add(entry1);
	// entries.add(entry2);
	// this.setEntries(entries);
	// this.setDebitTotal(entry1.getDebit() + entry2.getDebit());
	// this.setCreditTotal(entry1.getCredit() + entry2.getCredit());
	//
	// }

	// public JournalEntry(VATReturn return1) {
	// setCompany(return1.getCompany());
	// this.type = Transaction.TYPE_JOURNAL_ENTRY;
	// this.journalEntryType = TYPE_NORMAL_JOURNAL_ENTRY;
	// this.number = NumberUtils.getNextTransactionNumber(
	// Transaction.TYPE_JOURNAL_ENTRY, getCompany());
	// this.transactionDate = return1.transactionDate;
	// this.transaction = return1;
	//
	// double creditTotal = 0;
	// double debitTotal = 0;
	// List<Entry> entries = new ArrayList<Entry>();
	// for (Box b : return1.boxes) {
	// // if (b.getTaxRateCalculations() != null
	// // && b.getTaxRateCalculations().size() > 0) {
	//
	// if (b.getAmount() != 0
	// && (b.getBoxNumber() == 1 || b.getBoxNumber() == 2)) {
	//
	// Entry e = new Entry();
	//
	// if (DecimalUtil.isGreaterThan(b.getAmount(), 0)) {
	// e.setDebit(b.amount);
	// debitTotal += b.amount;
	// } else {
	// creditTotal += (-1 * b.amount);
	// e.setCredit(-1 * b.amount);
	// }
	// e.setAccount(return1.getTaxAgency().getSalesLiabilityAccount());
	//
	// e.setEntryDate(return1.getDate());
	// e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
	// e.setMemo("Filed VAT amount of " + b.amount + " for Box "
	// + b.boxNumber);
	// e.setTaxItem(b.getTaxRateCalculations() != null ? (b
	// .getTaxRateCalculations().get(0)).getTaxItem() : null);
	// e.setTotal(b.getAmount());
	// entries.add(e);
	//
	// } else if (b.getAmount() != 0 && b.getBoxNumber() == 4) {
	//
	// Entry e = new Entry();
	// e.setTotal(b.getAmount());
	// if (DecimalUtil.isGreaterThan(b.getAmount(), 0)) {
	// e.setCredit(b.amount);
	// creditTotal += b.amount;
	// } else {
	// debitTotal += (-1 * b.amount);
	// e.setDebit(-1 * b.amount);
	// }
	// e.setAccount(return1.getTaxAgency()
	// .getPurchaseLiabilityAccount());
	// e.setEntryDate(return1.getDate());
	// e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
	// e.setMemo("Filed VAT amount of " + b.amount + " for Box "
	// + b.boxNumber);
	// e.setTaxItem(b.getTaxRateCalculations() != null ? (b
	// .getTaxRateCalculations().get(0)).getTaxItem() : null);
	// entries.add(e);
	// } else if (b.getAmount() != 0
	// && (b.getBoxNumber() == 6 || b.boxNumber == 7
	// || b.boxNumber == 8 || b.boxNumber == 9)) {
	//
	// Entry e = new Entry();
	// if (DecimalUtil.isGreaterThan(b.getAmount(), 0)) {
	// e.setDebit(0d);
	// // debitTotal += b.amount;
	// } else {
	// // creditTotal += b.amount;
	// e.setCredit(0d);
	// }
	// e.setTotal(b.getAmount());
	// e.setTaxItem(b.getTaxRateCalculations() != null ? (b
	// .getTaxRateCalculations().get(0)).getTaxItem() : null);
	// e.setAccount(return1.getTaxAgency()
	// .getPurchaseLiabilityAccount());
	// e.setEntryDate(return1.getDate());
	// e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
	// e.setMemo("Filed net amount of " + b.amount + " for Box "
	// + b.boxNumber);
	// entries.add(e);
	//
	// } else if (b.getAmount() != 0 && b.boxNumber == 10) {
	//
	// Entry e = new Entry();
	// if (DecimalUtil.isGreaterThan(b.getAmount(), 0)) {
	// e.setDebit(b.amount);
	// debitTotal += b.amount;
	// } else {
	// creditTotal += (-1 * b.amount);
	// e.setCredit(-1 * b.amount);
	// }
	// e.setTotal(b.getAmount());
	// e.setTaxItem(b.getTaxRateCalculations() != null ? (b
	// .getTaxRateCalculations().get(0)).getTaxItem() : null);
	// e.setAccount(return1.getTaxAgency()
	// .getPurchaseLiabilityAccount());
	// e.setEntryDate(return1.getDate());
	// e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
	// e.setMemo("Filed net amount of " + b.amount + " for Box "
	// + b.boxNumber);
	// entries.add(e);
	// }
	// // }
	// }
	// double amount = return1.getBoxes().get(4).getAmount()
	// + return1.getBoxes().get(return1.getBoxes().size() - 1)
	// .getAmount();
	//
	// Entry e = new Entry();
	// e.setTotal(amount);
	// e.setEntryDate(return1.getDate());
	// e.setType(Entry.JOURNAL_ENTRY_TYPE_FINANCIAL_ACCOUNT);
	// e.setMemo("Filed VAT Amount");
	// if (DecimalUtil.isGreaterThan(amount, 0)) {
	// e.setCredit(amount);
	// creditTotal += amount;
	//
	// } else {
	// debitTotal += (-1 * amount);
	// e.setDebit(-1 * amount);
	// }
	//
	// e.setAccount(getCompany().getVATFiledLiabilityAccount());
	// entries.add(e);
	// this.setDebitTotal(debitTotal);
	// this.setCreditTotal(creditTotal);
	//
	// this.setEntries(entries);
	//
	// }

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getID() {
		return id;
	}

	/**
	 * @return the memo
	 */
	@Override
	public String getMemo() {
		return memo;
	}

	public double getDebitTotal() {
		return debitTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal = debitTotal;
	}

	public double getCreditTotal() {
		return creditTotal;
	}

	public void setCreditTotal(double creditTotal) {
		this.creditTotal = creditTotal;
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
	public Account getEffectingAccount() {
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
	public String toString() {
		return AccounterServerConstants.TYPE_JOURNAL_ENTRY;
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {
		this.isVoidBefore = isVoid;
		super.onLoad(session, arg1);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;

		this.total = this.debitTotal;
		// this.creditTotal = 0.0;
		// this.debitTotal = 0.0;

		// written by kumar.
		// for (TransactionItem entry : this.entries) {
		//
		// if (entry.getType() == Entry.TYPE_CUSTOMER) {
		//
		// if (!DecimalUtil.isEquals(entry.getCredit(), 0)) {
		//
		// this.creditsAndPayments = new CreditsAndPayments(this);
		// session.save(creditsAndPayments);
		// } else {
		// this.balanceDue = entry.getDebit();
		// }
		//
		// } else if (entry.getType() == Entry.TYPE_VENDOR) {
		//
		// if (!DecimalUtil.isEquals(entry.getDebit(), 0)) {
		//
		// this.creditsAndPayments = new CreditsAndPayments(this);
		// session.save(creditsAndPayments);
		// } else {
		// this.balanceDue = entry.getCredit();
		// }
		// }
		//
		// }

		// Creating Activity
		Activity activity = new Activity(getCompany(),
				AccounterThreadLocal.get(), ActivityType.ADD, this);
		session.save(activity);
		this.setLastActivity(activity);

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		super.onUpdate(session);
		// if (isBecameVoid()) {
		//
		// // If this Journal Entry has any Credits then make them as null.
		// this.creditsAndPayments = null;
		//
		// // If this Journal entry has been paid in any receive payments then
		// // roll back the payments and Update the credits balances.
		// if (this.transactionReceivePayments != null) {
		// for (TransactionReceivePayment trp : this.transactionReceivePayments)
		// {
		// trp.onVoidTransaction(session);
		// session.saveOrUpdate(trp);
		// }
		// }
		//
		// // If this Journal entry has been paid in any payments then roll
		// // back the payments and and Update the credits balances.
		// for (TransactionPayBill tx : this.transactionPayBills) {
		// tx.makeVoid(session);
		// }
		//
		// this.balanceDue = 0.0;
		//
		// if (this.transaction instanceof VATAdjustment
		// || this.transaction instanceof VATReturn) {
		// this.transaction.setVoid(this.isVoid);
		// this.transaction.onUpdate(session);
		// session.saveOrUpdate(this.transaction);
		// }
		// }
		// if (this.entry != null) {
		// for (Entry ti : this.entry) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }

		return false;
	}

	protected boolean isBecameVoid() {
		return isVoid && !this.isVoidBefore;
	}

	@Override
	public Payee getInvolvedPayee() {
		return involvedPayee;
	}

	public void updatePaymentsAndBalanceDue(double amount2) {

		this.balanceDue += amount2;
	}

	private void doVoidEffect(Session session, JournalEntry journalEntry) {

		/* If this Journal Entry has any Credits then make them as null. */
		if (this.creditsAndPayments != null)
			this.creditsAndPayments = null;

		/*
		 * If this Journal entry has been paid in any receive payments then roll
		 * back the payments and Update the credits balances.
		 */
		if (this.transactionReceivePayments != null) {
			for (TransactionReceivePayment trp : this.transactionReceivePayments) {
				trp.onVoidTransaction(session);
				session.saveOrUpdate(trp);
			}
		}

		/*
		 * If this Journal entry has been paid in any payments then roll back
		 * the payments and and Update the credits balances.
		 */
		for (TransactionPayBill tx : this.transactionPayBills) {
			tx.makeVoid(session);
		}

		this.balanceDue = 0.0;

		// if (this.transaction instanceof TAXAdjustment
		// || this.transaction instanceof VATReturn) {
		// this.transaction.setVoid(this.isVoid);
		// this.transaction.onUpdate(session);
		// session.saveOrUpdate(this.transaction);
		// }

		// if (this.entries != null) {
		// for (Entry ti : this.entries) {
		// if (ti instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) ti;
		// lifeCycle.onUpdate(session);
		// }
		// }
		// }

	}

	@Override
	public void onEdit(Transaction clonedObject) {

		// JournalEntry journalEntry = (JournalEntry) clonedObject;
		// Session session = HibernateUtil.getCurrentSession();

		// if ((this.isVoid && !journalEntry.isVoid)
		// || (this.isDeleted() && !journalEntry.isDeleted())) {
		//
		// journalEntry.isEdited = false;
		// this.isEdited = false;
		//
		// for (Entry voidEntry : this.entries) {
		//
		// voidEntry.updateAccountBalances(session, true);
		// }
		//
		// doVoidEffect(session, this);

		// } else {
		//
		// journalEntry.isEdited = true;
		// this.isEdited = true;

		/* To rollback the effect of clonedObject JournalEntry */

		// for (Entry preEntry : journalEntry.entries) {
		// for (Entry newEntry : this.entries) {
		// if (newEntry.id == preEntry.id) {
		// if (!DecimalUtil.isEquals(preEntry.credit, 0.0)
		// && !DecimalUtil.isEquals(newEntry.debit, 0.0)) {
		// newEntry.credit = 0.0;
		// }
		// if (!DecimalUtil.isEquals(preEntry.debit, 0.0)
		// && !DecimalUtil.isEquals(newEntry.credit, 0.0)) {
		// newEntry.debit = 0.0;
		// }
		// }
		// }
		//
		// preEntry.updateEntryAccountBalances(session, true);
		// }

		/* Creates the effect for New JournalEntry */

		// for (Entry newEntry : this.entries) {
		//
		// // newEntry.journalEntry.creditsAndPayments = null;
		// newEntry.updateEntryAccountBalances(session, false);
		// }
		// }

		super.onEdit(clonedObject);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		if (this.isVoidBefore) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}
		return true;
	}

	@Override
	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = new HashMap<Account, Double>();
		for (TransactionItem e : transactionItems) {
			map.put(e.getAccount(), e.getLineTotal());
		}
		return map;
	}

	public void setInvolvedPayee(Payee involvedPayee) {
		this.involvedPayee = involvedPayee;
	}

}
