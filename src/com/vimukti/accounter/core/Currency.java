package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti16 Not used Yet
 */
public class Currency implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}

	long id;
	/**
	 * Name of the Currency
	 */
	String name;
	/**
	 * Symbol of the Currency.
	 */
	String symbol;
	/**
	 * Formal Name of the Currency.
	 */
	String formalName;

	/*
	 * String countryName;
	 * 
	 * public Currency() { }
	 * 
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return the formalName
	 */
	public String getFormalName() {
		return formalName;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

}
