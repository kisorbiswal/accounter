package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Philippines extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bicol", "Cagayan Valley", "Caraga",
				"Central Luzon", "Central Mindanao", "Central Visayas",
				"Cordillera", "Eastern Visayas", "Ilocos", "Muslim Mindanao",
				"National Capital Region", "Northern Mindanao",
				"Southern Mindanao", "Southern Tagalog", "Western Mindanao",
				"Western Visayas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PHP";
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
