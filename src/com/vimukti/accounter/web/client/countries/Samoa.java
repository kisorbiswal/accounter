package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Samoa extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Apia Urban Area", "North West Upolu",
				"Rest of Upolu", "Savaii" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "WST";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().january();
	}
}
