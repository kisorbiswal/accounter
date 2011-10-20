package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Liechtenstein extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "CHF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Balzers", "Eschen", "Gamprin", "Mauren",
				"Planken", "Ruggell", "Schaan", "Schellenberg", "Triesen",
				"Triesenberg", "Vaduz", };
	}

}
