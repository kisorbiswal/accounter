package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Mozambique extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Gaza", "Inhambane", "Manica",
				"Maputo", "Nampula", "Sofala", "Tete", "Zambézia" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MZN";
	}

}
