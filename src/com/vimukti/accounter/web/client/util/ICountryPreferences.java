package com.vimukti.accounter.web.client.util;

public interface ICountryPreferences {
	String[] getStates();
	
	String getPreferredCurrency();
	
	//String getDefaultTimeZone(String state);
	
	OrganizationType[] getOrganizationTypes();
	
	boolean allowFlexibleFiscalYear();
	
	String getDefaultFiscalYearStartingMonth();
	
	String getDefaultDateFormat();
	
	boolean isVatAvailable();
	
	boolean isVatIdAndCompanyTaxIdSame();
	
	boolean isSalesTaxAvailable();
	
	boolean isSalesTaxIdAndCompanyTaxIdSame();
	
	String getVATName();
	
	boolean isServiceTaxAvailable();
	
	boolean isServiceTaxIdAndCompanyTaxIdSame();
	
	boolean isTDSAvailable();
	
	boolean isServiceTaxAndSalesTaxSame();
	
	boolean isServiceTaxDeductable();
	
}
