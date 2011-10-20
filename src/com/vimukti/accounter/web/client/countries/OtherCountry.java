package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class OtherCountry extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public String[] getStates() {
		return new String[] {};
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().april();
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

}
