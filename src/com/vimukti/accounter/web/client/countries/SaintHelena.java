package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SaintHelena extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Ascension", "Saint Helena", "Tristan da Cunha" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SHP";
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
