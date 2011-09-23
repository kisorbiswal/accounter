package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Vietnam extends AbstractCountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getStates() {
		String[] states = new String[] { "B?c Trung B?",
				"Ð?ng b?ng sông C?u Long", "Ð?ng b?ng sông H?ng",
				"Ðông B?c B?", "Ðông Nam B?", "Duyên h?i Nam Trung B?",
				"Tây B?c B?", "Tây Nguyên" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "VND";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
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
		return super.isSalesTaxAvailable();
	}

	@Override
	public boolean isSalesTaxIdAndCompanyTaxIdSame() {
		return false;
	}


}
