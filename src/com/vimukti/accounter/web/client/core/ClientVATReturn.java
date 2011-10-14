/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vimukti5
 * 
 */
public class ClientVATReturn extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The Start date of this VAT Return
	 */
	long VATperiodStartDate;
	/**
	 * The End date of this VAT Return
	 */
	long VATperiodEndDate;

	/**
	 * The VAT Agency to which we are going to create this VAT Return
	 * 
	 */
	long taxAgency;

	List<ClientBox> boxes = new ArrayList<ClientBox>();

	long journalEntry;

	double balance;

	/**
	 * @return the vATperiodStartDate
	 */
	public long getVATperiodStartDate() {
		return VATperiodStartDate;
	}

	/**
	 * @param tperiodStartDate
	 *            the vATperiodStartDate to set
	 */
	public void setVATperiodStartDate(long tperiodStartDate) {
		VATperiodStartDate = tperiodStartDate;
	}

	/**
	 * @return the vatAgency
	 */
	public long getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param taxAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
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
	 * @return the journalEntry
	 */
	public long getJournalEntry() {
		return journalEntry;
	}

	/**
	 * @param journalEntry
	 *            the journalEntry to set
	 */
	public void setJournalEntry(long journalEntry) {
		this.journalEntry = journalEntry;
	}

	/**
	 * @return the boxes
	 */
	public List<ClientBox> getBoxes() {
		return boxes;
	}

	public long getTAXAgency() {
		return taxAgency;
	}

	public void setTAXAgency(long tAXAgency) {
		taxAgency = tAXAgency;
	}

	public long getVATperiodEndDate() {
		return VATperiodEndDate;
	}

	public void setVATperiodEndDate(long vATperiodEndDate) {
		VATperiodEndDate = vATperiodEndDate;
	}

	/**
	 * @param boxes
	 *            the boxes to set
	 */
	public void setBoxes(List<ClientBox> boxes) {
		this.boxes = boxes;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getClientClassSimpleName
	 * ()
	 */
	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return null;
	}

	/*
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getName()
	 */
	@Override
	public String getName() {
		return null;
	}

	/*
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.VATRETURN;
	}

	/*
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getID()
	 */
	@Override
	public long getID() {
		return this.id;
	}

	/*
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#setID(java
	 * .lang.String)
	 */
	@Override
	public void setID(long id) {
		this.id = id;

	}

	public ClientVATReturn clone() {
		ClientVATReturn vatReturn = (ClientVATReturn) this.clone();

		List<ClientBox> boxs = new ArrayList<ClientBox>();
		for (ClientBox clientBox : this.boxes) {
			boxs.add(clientBox.clone());
		}
		vatReturn.boxes = boxs;

		List<ClientEntry> entries = new ArrayList<ClientEntry>();
		for (ClientEntry clientEntry : this.entry) {
			entries.add(clientEntry.clone());
		}
		vatReturn.entry = entries;

		// transactionItems list
		List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : this.transactionItems) {
			transactionItems.add(clientTransactionItem.clone());
		}
		vatReturn.transactionItems = transactionItems;

		// transactionMakeDeposit list
		List<ClientTransactionMakeDeposit> transactionMakeDeposit = new ArrayList<ClientTransactionMakeDeposit>();
		for (ClientTransactionMakeDeposit clientTransactionMakeDeposit : this.transactionMakeDeposit) {
			transactionMakeDeposit.add(clientTransactionMakeDeposit.clone());
		}
		vatReturn.transactionMakeDeposit = transactionMakeDeposit;

		// transactionPayBill list
		List<ClientTransactionPayBill> transactionPayBillList = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill clientTransactionPayBill : this.transactionPayBill) {
			transactionPayBillList.add(clientTransactionPayBill.clone());
		}
		vatReturn.transactionPayBill = transactionPayBillList;

		// transactionReceivePayment list
		List<ClientTransactionReceivePayment> transactionReceivePaymentList = new ArrayList<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment clientTransactionReceivePayment : this.transactionReceivePayment) {
			transactionReceivePaymentList.add(clientTransactionReceivePayment
					.clone());
		}
		vatReturn.transactionReceivePayment = transactionReceivePaymentList;

		// transactionIssuePayment list
		List<ClientTransactionIssuePayment> transactionIssuePayment = new ArrayList<ClientTransactionIssuePayment>();
		for (ClientTransactionIssuePayment clientTransactionIssuePayment : this.transactionIssuePayment) {
			transactionIssuePayment.add(clientTransactionIssuePayment.clone());
		}
		vatReturn.transactionIssuePayment = transactionIssuePayment;

		// transactionPaySalestax list
		List<ClientTransactionPaySalesTax> transactionPaySalesTax = new ArrayList<ClientTransactionPaySalesTax>();
		for (ClientTransactionPaySalesTax clientTransactionPaySalesTax : this.transactionPaySalesTax) {
			transactionPaySalesTax.add(clientTransactionPaySalesTax.clone());
		}
		vatReturn.transactionPaySalesTax = transactionPaySalesTax;

//		vatReturn.creditsAndPayments = this.creditsAndPayments.clone();

		return vatReturn;
	}
}
