package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class PayeeUpdate extends CreatableObject {

	private Payee payee;

	private Transaction transaction;

	private double amount;

	public PayeeUpdate() {
		// TODO Auto-generated constructor stub
	}

	public PayeeUpdate(Payee payee, Transaction transaction, double amount) {
		this.payee = payee;
		this.transaction = transaction;
		this.amount = amount;
	}

	/**
	 * @return the payee
	 */
	public Payee getPayee() {
		return payee;
	}

	/**
	 * @param payee
	 *            the payee to set
	 */
	public void setPayee(Payee payee) {
		this.payee = payee;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		setCompany(transaction.getCompany());
		payee.effectBalance(amount);
		session.saveOrUpdate(payee);
		return super.onSave(session);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		getPayee().effectBalance(-getAmount());
		session.saveOrUpdate(getPayee());
		return super.onDelete(session);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		long tID = getTransaction().getID();
		hash = 31 * hash + (int) (tID ^ (tID >>> 32));
		long pID = getPayee().getID();
		long bits = Double.doubleToLongBits(getAmount());
		hash = (int) (pID ^ (pID >>> 32)) + (int) (bits ^ (bits >>> 32));
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PayeeUpdate)) {
			return false;
		}
		PayeeUpdate other = (PayeeUpdate) obj;
		if (this.getTransaction().getID() == other.getTransaction().getID()
				&& this.getPayee().getID() == other.getPayee().getID()
				&& DecimalUtil.isEquals(this.getAmount(), other.getAmount())) {
			return true;
		}
		return false;
	}

	public void add(PayeeUpdate payeeUpdate) {
		amount += payeeUpdate.getAmount();
	}
}
