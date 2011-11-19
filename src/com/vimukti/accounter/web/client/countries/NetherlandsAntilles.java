package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class NetherlandsAntilles extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Bonaire", "Curaçao", "Saba", "Sint Eustatius",
				"Sint Maarten" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ANG";
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
