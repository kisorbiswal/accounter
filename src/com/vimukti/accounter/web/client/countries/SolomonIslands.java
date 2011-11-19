package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SolomonIslands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Central", "Choiseul", "Guadalcanal", "Isabel",
				"Makira and Ulawa", "Malaita", "Rennell and Bellona", "Temotu",
				"Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SBD";
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
