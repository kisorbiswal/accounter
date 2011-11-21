package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Turkmenistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Ahal", "Asgabat", "Balkan", "Dasoguz", "Lebap",
				"Mari" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TMT";
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
