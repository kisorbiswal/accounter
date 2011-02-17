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
	String depositIn;

	/**
	 * Bills Due On or Before.
	 */
	long returnsDueOnOrBefore;

	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	String vatAgency;

	double endingBalance;

	boolean isEdited = false;

	List<ClientTransactionReceiveVAT> transactionReceiveVAT;

	transient boolean isImported;

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.RECEIVEVAT;
	}

	@Override
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	/**
	 * @return the payFrom
	 */
	public String getDepositIn() {
		return depositIn;
	}

	/**
	 * @param DepositIn
	 *            the DepositIn to set
	 */

	public void setDepositIn(String depositIn) {
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
	public String getVatAgency() {
		return vatAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setVatAgency(String vatAgency) {
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

	/**
	 * @return the isImported
	 */
	public boolean isImported() {
		return isImported;
	}

	/**
	 * @param isImported
	 *            the isImported to set
	 */
	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}

}