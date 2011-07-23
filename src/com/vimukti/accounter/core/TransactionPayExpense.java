package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionPayExpense implements Lifecycle {

	long id;
	public long id;

	/**
	 * The Expense object which is the cause of PayExpense to be made.
	 */
	Expense expense;
	/**
	 * The amount which the TransactionPayExpense make.
	 */
	double payment = 0D;

	/**
	 * The PayExpense object which holds the TransactionPayExpense reference,.
	 */
	PayExpense payExpense;

	transient boolean isImported;
	transient private boolean isOnSaveProccessed;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the expense
	 */
	public Expense getExpense() {
		return expense;
	}

	/**
	 * @return the payment
	 */
	public double getPayment() {
		return payment;
	}

	/**
	 * @return the payExpense
	 */
	public PayExpense getPayExpense() {
		return payExpense;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id){
		this.id = id;
	}

	/**
	 * @param expense
	 *            the expense to set
	 */
	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	/**
	 * @param payment
	 *            the payment to set
	 */
	public void setPayment(double payment) {
		this.payment = payment;
	}

	/**
	 * @param payExpense
	 *            the payExpense to set
	 */
	public void setPayExpense(PayExpense payExpense) {
		this.payExpense = payExpense;
	}

	public boolean isImported() {
		return isImported;
	}

	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLoad(Session session, Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		this.expense.setPaidAmount(this.expense.getPaidAmount() + this.payment);
		this.expense.setAmountDue(this.expense.getAmountDue() - this.payment);

		if (DecimalUtil.isEquals(this.expense.getAmountDue(),0.0)) {

			this.expense.setStatus(Expense.STATUS_PAID);

		} else {

			this.expense.setStatus(Expense.STATUS_PARTIALLY_PAID);

		}

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}
}
