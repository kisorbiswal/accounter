package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Ghana extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Ashanti", "Brong-Ahafo", "Central",
				"Eastern", "Greater Accra", "Northern", "Upper East",
				"Upper West", "Volta", "Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "GHC";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.constants().january();
	}

}
