package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientReceiveVAT extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PayFrom
	 */
	long depositIn;

	/**
	 * Bills Due On or Before.
	 */
	long returnsDueOnOrBefore;

	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	long vatAgency;

	double endingBalance;

	boolean isEdited = false;

	List<ClientTransactionReceiveVAT> transactionReceiveVAT;

	@Override
	public String getClientClassSimpleName() {
		// its not using any where
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.RECEIVEVAT;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	/**
	 * @return the payFrom
	 */
	public long getDepositIn() {
		return depositIn;
	}

	/**
	 * @param DepositIn
	 *            the DepositIn to set
	 */

	public void setDepositIn(long depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the returnsDueOnOrBefore
	 */
	public long getReturnsDueOnOrBefore() {
		return returnsDueOnOrBefore;
	}

	/**
	 * @param returnsDueOnOrBefore
	 *            the returnsDueOnOrBefore to set
	 */
	public void setReturnsDueOnOrBefore(long returnsDueOnOrBefore) {
		this.returnsDueOnOrBefore = returnsDueOnOrBefore;
	}

	/**
	 * @return the vatAgency
	 */
	public long getVatAgency() {
		return vatAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setVatAgency(long vatAgency) {
		this.vatAgency = vatAgency;
	}

	/**
	 * @return the endingBalance
	 */
	public double getEndingBalance() {
		return endingBalance;
	}

	/**
	 * @param endingBalance
	 *            the endingBalance to set
	 */
	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}

	/**
	 * @return the isVoid
	 */
	public boolean isVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
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
	 * @return the clientTransactionPayVAT
	 */
	public List<ClientTransactionReceiveVAT> getClientTransactionReceiveVAT() {
		return transactionReceiveVAT;
	}

	/**
	 * @param clientTransactionReceiveVAT
	 *            the clientTransactionReceiveVAT to set
	 */
	public void setClientTransactionReceiveVAT(
			List<ClientTransactionReceiveVAT> transactionReceiveVATList) {
		this.transactionReceiveVAT = transactionReceiveVATList;
	}

}