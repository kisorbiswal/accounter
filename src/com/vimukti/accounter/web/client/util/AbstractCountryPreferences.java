package com.vimukti.accounter.web.client.util;

public abstract class AbstractCountryPreferences implements ICountryPreferences {

	@Override
	public OrganizationType[] getOrganizationTypes() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isServiceTaxIdAndCompanyTaxIdSame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultDateFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isVatAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVatIdAndCompanyTaxIdSame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSalesTaxAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSalesTaxIdAndCompanyTaxIdSame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getVATName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isServiceTaxAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTDSAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isServiceTaxAndSalesTaxSame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isServiceTaxDeductable() {
		// TODO Auto-generated method stub
		return false;
	}

}
