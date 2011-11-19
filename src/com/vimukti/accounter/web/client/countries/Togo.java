package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Togo extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Centre", "Kara", "Maritime", "Plateaux", "Savanes" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XOF";
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
