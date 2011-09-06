package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class TurksAndCaicosIslands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Grand Turk", "Middle Caicos",
				"North Caicos", "Providenciales and West Caicos", "Salt Cay",
				"South Caicos and East Caicos" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "USD";
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
