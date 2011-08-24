/**
 * 
 */
package com.vimukti.accounter.web.client.core;

public class ClientCurrency implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int version;

	long id;

	String name;
	String symbol;
	String formalName;

	/*
	 * String countryName;
	 * 
	 * public String getCountryName() { return countryName; }
	 * 
	 * public void setCountryName(String countryName) { this.countryName =
	 * countryName; }
	 */

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the formalName
	 */
	public String getFormalName() {
		return formalName;
	}

	/**
	 * @param formalName
	 *            the formalName to set
	 */
	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.CURRENCY;
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

		return "ClientCurrency";
	}

	public ClientCurrency clone() {
		ClientCurrency currency = (ClientCurrency) this.clone();
		return currency;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientCurrency) {
			ClientCurrency currency = (ClientCurrency) obj;
			return this.getID() == currency.getID() ? true : false;
		}
		return false;
	}
}
