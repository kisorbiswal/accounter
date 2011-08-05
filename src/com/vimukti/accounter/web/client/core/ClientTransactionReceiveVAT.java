package com.vimukti.accounter.web.client.core;

public class ClientTransactionReceiveVAT implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6293589247354234626L;

	long id;

	/**
	 * The TaxAgency that we have selected for what we are making the
	 * PaySalesTax.
	 */
	long taxAgency;

	/**
	 * The amount of Tax which we still have to pay.
	 */
	double taxDue;

	/**
	 * The amount of Tax what we are paying presently.
	 */
	double amountToReceive;

	long vatReturn;

	ClientReceiveVAT receiveVAT;

	int version;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return the vatAgency
	 */
	public long getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(long taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the taxDue
	 */
	public double getTaxDue() {
		return taxDue;
	}

	/**
	 * @param taxDue
	 *            the taxDue to set
	 */
	public void setTaxDue(double taxDue) {
		this.taxDue = taxDue;
	}

	/**
	 * @param amountToPay
	 *            the amountToReceive to set
	 */
	public void setAmountToReceive(double amountToReceive) {
		this.amountToReceive = amountToReceive;
	}

	/**
	 * @return the amountToReceive
	 */
	public double getAmountToReceive() {
		return amountToReceive;
	}

	/**
	 * @return the vatReturn
	 */
	public long getVatReturn() {
		return vatReturn;
	}

	/**
	 * @param vatReturn
	 *            the vatReturn to set
	 */
	public void setVatReturn(long vatReturn) {
		this.vatReturn = vatReturn;
	}

	/**
	 * @return the payVAT
	 */
	public ClientReceiveVAT getReceiveVAT() {
		return receiveVAT;
	}

	/**
	 * @param receiveVAT
	 *            the receiveVAT to set
	 */
	public void setPayVAT(ClientReceiveVAT receiveVAT) {
		this.receiveVAT = receiveVAT;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientTransactionReceiveVAT";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClientTransactionReceiveVAT clone() {
		return null;
	}
}