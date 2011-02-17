package com.vimukti.accounter.web.client.core;

public class ClientTransactionReceiveVAT implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6293589247354234626L;

	long id;

	public String stringID;

	/**
	 * The TaxAgency that we have selected for what we are making the
	 * PaySalesTax.
	 */
	String taxAgency;

	/**
	 * The amount of Tax which we still have to pay.
	 */
	double taxDue;

	/**
	 * The amount of Tax what we are paying presently.
	 */
	double amountToReceive;

	String vatReturn;

	ClientReceiveVAT receiveVAT;

	int version;

	transient boolean isImported;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the vatAgency
	 */
	public String getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(String taxAgency) {
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
	public String getVatReturn() {
		return vatReturn;
	}

	/**
	 * @param vatReturn
	 *            the vatReturn to set
	 */
	public void setVatReturn(String vatReturn) {
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

	@Override
	public String getStringID() {

		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

}