package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Tokelau extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Atafu", "Fakaofo", "Nukunonu" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "NZD";
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
