package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class PapuaNewGuinea extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Eastern Highlands", "East New Britain",
				"East Sepik", "Enga", "Fly River", "Gulf", "Madang", "Manus",
				"Milne Bay", "Morobe", "National Capital District",
				"New Ireland", "North Solomons", "Oro", "Sandaun", "Simbu",
				"Southern Highlands", "Western Highlands", "West New Britain" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PGK";
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
