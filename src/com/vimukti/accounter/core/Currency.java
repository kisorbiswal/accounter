package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti16 Not used Yet
 */
public class Currency extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setName(String name) {
		this.name = name;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}

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
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

}
