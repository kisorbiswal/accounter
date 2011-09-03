package com.vimukti.accounter.web.client.util;

public interface ICountryPreferences {
	String[] getStates();
	
	String getPreferredCurrency();
	
	String getDefaultTimeZone(String state);
	
	IOrganizationTypes[] getOrganizationTypes();
	
	boolean allowFlexibleFiscalYear();
	
	String getDefaultFiscalYearStartingMonth();
	
	
}
