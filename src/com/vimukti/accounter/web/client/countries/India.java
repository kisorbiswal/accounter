package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class India extends AbstractCountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Andaman and Nicobar Islands",
				"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bangla",
				"Bihar", "Chandigarh", "Chhattisgarh",
				"Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa",
				"Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir",
				"Jharkhand", "Karnataka", "Kerala", "Lakshadweep",
				"Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
				"Mizoram", "Nagaland", "Orissa", "Pondicherry", "Punjab",
				"Rajasthan", "Sikkim", "Tamil Nadu", "Tripura", "Uttaranchal",
				"Uttar Pradesh" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "INR";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().april();
	}

	@Override
	public boolean isServiceTaxIdAndCompanyTaxIdSame() {
		return false;
	}

	@Override
	public String getDefaultDateFormat() {
		return super.getDefaultDateFormat();
	}

	@Override
	public boolean isVatAvailable() {
		return true;
	}

	@Override
	public boolean isVatIdAndCompanyTaxIdSame() {
		return false;
	}

	@Override
	public boolean isSalesTaxAvailable() {
		return true;
	}

	@Override
	public boolean isSalesTaxIdAndCompanyTaxIdSame() {
		return false;
	}

	@Override
	public String getVATName() {
		return "VAT";
	}

	@Override
	public boolean isServiceTaxAvailable() {
		return true;
	}

	@Override
	public boolean isTDSAvailable() {
		// THIS IS TEMPARORY
		return false;
	}

	@Override
	public boolean isServiceTaxDeductable() {
		return true;
	}
}
