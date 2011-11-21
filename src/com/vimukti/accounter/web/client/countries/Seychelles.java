package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Seychelles extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Mahé" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SCR";
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
