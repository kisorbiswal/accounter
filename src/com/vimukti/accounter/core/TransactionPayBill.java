package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * For every {@link PayBill} entry there will be one or more TransactionPayBill
 * entries. These are populated whenever any {@link Enterbill} or a journal
 * entry for a Vendor, or a {@link MakeDeposit} for Vendor type is created. We
 * can apply write off and credits (if any) for this.
 * 
 * @author Chandan
 * 
 */
public class TransactionPayBill implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4955126209995058112L;
	long id;
	int version;

	/**
	 * This due FinanceDate is given while creating the Enterbill or
	 * TransactionMakeDeposit or JournalEntry. This is just displayed in the
	 * TransactionPaybill but not editable.
	 */
	FinanceDate dueDate;

	/**
	 * The reference of EnterBill from where the TransactionPayBill is created.
	 */
	@ReffereredObject
	EnterBill enterBill;

	/**
	 * The amount by which the EnterBill or TransactionMakeDeposit is made.
	 */
	double originalAmount = 0D;

	/**
	 * The amount which still have to pay to clear the TransactionPayBill.
	 */
	double amountDue = 0D;

	/**
	 * This field holds the FinanceDate of the discount (if any) applied at the
	 * time of creating the EnterBill or TransactionMakeDeposit or JournalEntry.
	 */
	FinanceDate discountDate;

	/**
	 * The account from where the discount amount is deducted.
	 */
	@ReffereredObject
	Account discountAccount;

	/**
	 * The amount of discount which is given at the time of creation of this
	 * TransactionPayBill
	 */
	double cashDiscount = 0D;

	/**
	 * The amounts of credits of the payee(if any) which are used in this
	 * TransactionPayBill
	 */
	double appliedCredits = 0D;

	/**
	 * The amount which we are paying now to the TransactionPayBill
	 */
	double payment = 0D;

	/**
	 * The reference of PayBill which holds this TransactionPayBill object holds
	 * this TransactionPayExpense reference.
	 */

	double tdsAmount = 0D;

	@ReffereredObject
	PayBill payBill;

	/**
	 * The TransactionPayBill consists of the list of
	 * {@link TransactionCreditsAndPayments} from which we can apply the
	 * available credits of the payee.
	 */
	@ReffereredObject
	List<TransactionCreditsAndPayments> transactionCreditsAndPayments;

	/**
	 * This is to indicate whether the transaction is voided or not
	 */
	boolean isVoid = false;

	/**
	 * This reference is used to refer the TransactionMakeDeposit that was the
	 * cause of this TransactionPaybill(if any).
	 */
	@ReffereredObject
	TransactionMakeDeposit transactionMakeDeposit;

	/**
	 * The reference to the payee from which the TransactionPayBill is created.
	 */
	@ReffereredObject
	Vendor vendor;

	/**
	 * The bill number is the EnterBill or TransactionMakeDeposit or
	 * JournalEntry 's transaction number.
	 */
	String billNumber;

	/**
	 * The reference of JournalEntry from where the TransactionPayBill is
	 * created.
	 */
	@ReffereredObject
	JournalEntry journalEntry;

	transient private boolean isOnSaveProccessed;

	public TransactionPayBill() {
	}

	public JournalEntry getJournalEntry() {
		return journalEntry;
	}

	public void setJournalEntry(JournalEntry journalEntry) {
		this.journalEntry = journalEntry;
	}

	/**
	 * @return the dueDate
	 */
	public FinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @return the enterBill
	 */
	public EnterBill getEnterBill() {
		return enterBill;
	}

	public void setPayBill(PayBill payBill2) {
		this.payBill = payBill2;

	}

	/**
	 * @return the originalAmount
	 */
	public double getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @return the amountDue
	 */
	public double getAmountDue() {
		return amountDue;
	}

	/**
	 * @return the discountDate
	 */
	public FinanceDate getDiscountDate() {
		return discountDate;
	}

	/**
	 * @return the discountAccount
	 */
	public Account getDiscountAccount() {
		return discountAccount;
	}

	/**
	 * @return the cashDiscount
	 */
	public double getCashDiscount() {
		return cashDiscount;
	}

	/**
	 * @return the appliedCredits
	 */
	public double getAppliedCredits() {
		return appliedCredits;
	}

	/**
	 * @return the payment
	 */
	public double getPayment() {
		return payment;
	}

	public int getVersion() {
		return version;
	}

	/**
	 * @param enterBill
	 *            the enterBill to set
	 */
	public void setEnterBill(EnterBill enterBill) {
		this.enterBill = enterBill;
	}

	/**
	 * @param appliedCredits
	 *            the appliedCredits to set
	 */
	public void setAppliedCredits(double appliedCredits) {
		this.appliedCredits = appliedCredits;
	}

	/**
	 * @param payment
	 *            the payment to set
	 */
	public void setPayment(double payment) {
		this.payment = payment;
	}

	/**
	 * @return the paybill
	 */
	public PayBill getPayBill() {
		return payBill;
	}

	public List<TransactionCreditsAndPayments> getTransactionCreditsAndPayments() {
		return transactionCreditsAndPayments;
	}

	public void setTransactionCreditsAndPayments(
			List<TransactionCreditsAndPayments> transactionCreditsAndPayments) {
		this.transactionCreditsAndPayments = transactionCreditsAndPayments;

		if (transactionCreditsAndPayments == null)
			return;

		for (TransactionCreditsAndPayments transactionCreditsAndPayment : transactionCreditsAndPayments) {

			transactionCreditsAndPayment.setTransactionPayBill(this);
		}
	}

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
	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the transactionMakeDeposit
	 */
	public TransactionMakeDeposit getTransactionMakeDeposit() {
		return transactionMakeDeposit;
	}

	/**
	 * @param transactionMakeDeposit
	 *            the transactionMakeDeposit to set
	 */
	public void setTransactionMakeDeposit(
			TransactionMakeDeposit transactionMakeDeposit) {
		this.transactionMakeDeposit = transactionMakeDeposit;
	}

	/**
	 * @return the billNumber
	 */
	public String getBillNumber() {
		return billNumber;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (this.id == 0l) {

			// this.enterBill.getVendor().updateBalance(session, this.payBill,
			// -this.payment);

			if (this.getDiscountAccount() != null
					&& DecimalUtil.isGreaterThan(this.getCashDiscount(), 0.0)) {

				this.payBill.getVendor().updateBalance(session, this.payBill,
						-this.cashDiscount);

				this.discountAccount.updateCurrentBalance(this.payBill,
						this.cashDiscount);
				this.discountAccount.onUpdate(session);

			}
			// To make the EnterBill as Un Void or Un Editable
			if (this.discountAccount != null
					&& DecimalUtil.isGreaterThan(this.cashDiscount, 0.0)) {
				if (this.enterBill != null) {
					this.enterBill.setCanVoidOrEdit(Boolean.FALSE);
				}

			}

			// We need to update the enterBill payments and balance due with the
			// sum of cash discount and applied credits and payments.
			double amount = (this.cashDiscount) + (this.appliedCredits)
					+ (this.payment);

			if (this.enterBill != null) {
				// Update the Payments and the balance due of the corresponding
				// enterBill
				this.enterBill.setPayments(this.enterBill.getPayments()
						+ amount);
				this.enterBill.setBalanceDue(this.enterBill.getBalanceDue()
						- amount);

				if (DecimalUtil.isGreaterThan(this.enterBill.getBalanceDue(),
						0D)
						&& DecimalUtil.isLessThan(
								this.enterBill.getBalanceDue(),
								this.enterBill.getTotal())) {
					this.enterBill.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
				} else if (DecimalUtil.isEquals(this.enterBill.getBalanceDue(),
						0D)) {
					this.enterBill.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
				}
				// To Decide whether this Invoice is Not paid, Partially paid or

			} else if (this.transactionMakeDeposit != null) {
				// Update the Payments and the balance due of the corresponding
				// customer refund
				this.transactionMakeDeposit
						.setPayments(this.transactionMakeDeposit.getPayments()
								+ amount);
				this.transactionMakeDeposit
						.setBalanceDue(this.transactionMakeDeposit
								.getBalanceDue() - amount);

			} else if (this.journalEntry != null) {
				this.journalEntry.setBalanceDue(this.journalEntry
						.getBalanceDue() - amount);
			}

		}

		// Update TDS Account if Company is INDIA
		if (Company.getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_INDIA) {
			TAXItem taxItem = this.payBill.getVendor().getTAXItem();
			TAXAgency taxAgency = taxItem.getTaxAgency();
			Account account = taxAgency.getSalesLiabilityAccount();
			account.updateCurrentBalance(this.payBill, -tdsAmount);
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		if (this.isVoid) {

			this.payBill.getVendor().updateBalance(session, this.payBill,
					this.payment);
			if (this.getDiscountAccount() != null
					&& DecimalUtil.isGreaterThan(this.getCashDiscount(), 0.0)) {

				this.payBill.getVendor().updateBalance(session, this.payBill,
						this.cashDiscount);
				this.discountAccount.updateCurrentBalance(this.payBill,
						-this.cashDiscount);
				this.discountAccount.onUpdate(session);
				session.saveOrUpdate(this.discountAccount);
			}

			double amount = (this.cashDiscount) + (this.appliedCredits)
					+ (this.payment);

			if (DecimalUtil.isGreaterThan(this.appliedCredits, 0.0)) {

				for (TransactionCreditsAndPayments transactionCreditsAndPayments : this.transactionCreditsAndPayments) {

					transactionCreditsAndPayments
							.onEditTransaction(-transactionCreditsAndPayments.amountToUse);

				}
				// this.appliedCredits = 0.0;
			}

			// this.cashDiscount = 0.0;
			// this.payment = 0.0;
			if (this.enterBill != null) {

				this.enterBill.updatePaymentsAndBalanceDue(amount);
				session.saveOrUpdate(this.enterBill);
				this.enterBill = null;

			} else if (this.transactionMakeDeposit != null) {

				this.transactionMakeDeposit.updatePaymentsAndBalanceDue(amount);
				session.saveOrUpdate(this.transactionMakeDeposit);
				this.transactionMakeDeposit = null;

			} else if (this.journalEntry != null) {
				this.journalEntry.balanceDue += amount;
				session.saveOrUpdate(this.journalEntry);
				this.journalEntry = null;
			}
			session.saveOrUpdate(this);

		}
		// ChangeTracker.put(this);
		return false;
	}

	/**
	 * By this method we can rollback the amount of the paybill and void the
	 * transactions of transactionCreditsAndPayments.
	 * 
	 * @param session
	 */
	public void makeVoid(Session session) {
		this.payBill.updateUnUsedAmount(session, this.payment);

		for (TransactionCreditsAndPayments tcp : this
				.getTransactionCreditsAndPayments()) {
			tcp.onEditTransaction(-tcp.amountToUse);

		}
		this.setPayment(0.0);
		this.setAppliedCredits(0.0);
	}

	/**
	 * After using the credits to the TransactionPayBill that amount must not be
	 * displayed the next time. So we need to update that amount ie roll back.
	 * This method is also called when this object is rolled back. At that time
	 * the balance of the credits will be increased.
	 * 
	 * @param amount
	 */
	public void updateAppliedCredits(double amount) {

		this.appliedCredits -= amount;

		if (this.enterBill != null) {
			this.enterBill.updatePaymentsAndBalanceDue(amount);
		} else if (this.transactionMakeDeposit != null) {
			this.transactionMakeDeposit.updatePaymentsAndBalanceDue(amount);
		} else if (this.journalEntry != null) {
			this.journalEntry.updatePaymentsAndBalanceDue(amount);
		}
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {

	}

	public void setAmountDue(Double amountDue) {
		this.amountDue = amountDue;
	}

	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public void setCashDiscount(double cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

	public void setDiscountDate(FinanceDate discountDate) {
		this.discountDate = discountDate;
	}

	public void setDiscountAccount(Account discountAccount) {
		this.discountAccount = discountAccount;
	}

	public void setDueDate(FinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public long getID() {

		return this.id;
	}

	public boolean equals(TransactionPayBill obj) {
		if (this.vendor.id == obj.vendor.id
				&& (this.enterBill != null && obj.enterBill != null) ? (this.enterBill
				.equals(obj.enterBill))
				: true && (this.payBill != null && obj.payBill != null) ? (this.payBill
						.equals(obj.payBill))
						: true
								&& DecimalUtil.isEquals(this.originalAmount,
										obj.originalAmount)
								&& DecimalUtil.isEquals(this.cashDiscount,
										obj.cashDiscount)
								&& (this.discountAccount != null && obj.discountAccount != null) ? (this.discountAccount
								.equals(obj.discountAccount))
								: true
										&& DecimalUtil.isEquals(
												this.appliedCredits,
												obj.appliedCredits)
										&& DecimalUtil.isEquals(this.payment,
												obj.payment)
										&& (this.journalEntry != null && obj.journalEntry != null) ? (this.journalEntry
										.equals(obj.journalEntry))
										: true && this.transactionCreditsAndPayments
												.size() == obj.transactionCreditsAndPayments
												.size()) {
			for (int i = 0; i < this.transactionCreditsAndPayments.size(); i++) {
				if (!this.transactionCreditsAndPayments.get(i).equals(
						obj.transactionCreditsAndPayments.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public double updatePayments(Double amtdue) {
		double previousUnusedAmount = 0.0;

		if (DecimalUtil.isGreaterThan(amtdue, 0)) {
			if (DecimalUtil.isGreaterThan(this.getPayment(), amtdue)) {

				if (DecimalUtil.isLessThan(this.getAmountDue(),
						this.getPayment()))
					previousUnusedAmount = this.getPayment()
							- this.getAmountDue();

				updateCredits((this.getPayment() - amtdue)
						- previousUnusedAmount, 0.0);

				this.setPayment(amtdue);
				amtdue = 0.0;

			} else {
				amtdue -= this.getPayment();

				if (DecimalUtil.isGreaterThan(this.appliedCredits, amtdue)) {

					updateCredits(this.appliedCredits - amtdue, amtdue);
					this.appliedCredits = amtdue;

				} else {

					amtdue -= this.appliedCredits;
					if (DecimalUtil.isGreaterThan(this.appliedCredits, amtdue))
						updateCredits(this.appliedCredits - amtdue, 0.0);

				}
			}
		}

		else {

			if (DecimalUtil.isGreaterThan(this.payment, amtdue)) {

				if (DecimalUtil.isLessThan(this.getAmountDue(),
						this.getPayment()))
					previousUnusedAmount = this.getPayment()
							- this.getAmountDue();

				updateCredits((this.getPayment() - amtdue)
						- previousUnusedAmount, 0.0);

				this.setPayment(0.0);
			}
		}
		return amtdue;
	}

	private void updateCredits(double creditAmount, double appliedCredits) {

		if (this.transactionCreditsAndPayments != null) {
			for (TransactionCreditsAndPayments transactionCreditsAndPayment : this.transactionCreditsAndPayments) {
				double amtToUse = transactionCreditsAndPayment.amountToUse;
				transactionCreditsAndPayment.onEditTransaction(-amtToUse);
				transactionCreditsAndPayment.amountToUse = appliedCredits;
			}
			this.appliedCredits = appliedCredits;
		}

		if (this.payBill.getCreditsAndPayments() != null) {
			this.payBill.getCreditsAndPayments().updateBalance(this.payBill,
					-creditAmount);

		} else {
			this.createCreditsAndPayments(creditAmount);
		}
	}

	private void createCreditsAndPayments(double creditAmount) {
		CreditsAndPayments creditsAndPayments = new CreditsAndPayments();
		creditsAndPayments.setCreditAmount(this.payBill.getTotal());
		creditsAndPayments.setBalance(creditAmount);
		creditsAndPayments.setMemo(this.payBill.getNumber() + "Pay Bill");
		creditsAndPayments.setTransaction(this.payBill);
		creditsAndPayments.payee = this.payBill.getVendor();
		this.payBill.setCreditsAndPayments(creditsAndPayments);

		HibernateUtil.getCurrentSession().save(creditsAndPayments);
	}

	public void onVoidEffect(Session session) {
		this.payBill.updateUnUsedAmount(session, this.payment);
		session.saveOrUpdate(this.payBill);

		for (TransactionCreditsAndPayments tcp : this
				.getTransactionCreditsAndPayments()) {
			tcp.onEditTransaction(-tcp.amountToUse);
			tcp.amountToUse = 0.0;
			session.saveOrUpdate(tcp);
		}
		this.setPayment(0.0);
		this.setAppliedCredits(0.0);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return true;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	public double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

}
