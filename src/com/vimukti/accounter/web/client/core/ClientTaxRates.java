package com.vimukti.accounter.web.client.core;

public class ClientTaxRates implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	int version;

	long id;

	double rate;

	long asOf;

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
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * @return the asOf
	 */
	public long getAsOf() {
		return asOf;
	}

	/**
	 * @param asOf
	 *            the asOf to set
	 */
	public void setAsOf(long asOf) {
		this.asOf = asOf;
	}

	public void setAsOf(ClientFinanceDate date) {
		setAsOf(date.getDate());
	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TAXRATES;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientTaxRates";
	}

	public ClientTaxRates clone() {
		ClientTaxRates clientTaxRatesClone = (ClientTaxRates) this.clone();
		return clientTaxRatesClone;
	}
}
