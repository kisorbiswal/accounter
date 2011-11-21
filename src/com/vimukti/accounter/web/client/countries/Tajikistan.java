package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Tajikistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Dushanbe", "Gorno-Badakhshan", "Karotegin",
				"Khatlon", "Sughd" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TJS";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.messages().january();
	}
	
}
