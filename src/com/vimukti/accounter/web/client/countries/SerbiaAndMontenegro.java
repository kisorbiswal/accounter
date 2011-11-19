package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class SerbiaAndMontenegro extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Central Serbia", "Vojvodina" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "RSD";
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
