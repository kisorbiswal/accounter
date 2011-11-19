package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class NewCaledonia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Îles", "Nord", "Sud" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XPF";
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
