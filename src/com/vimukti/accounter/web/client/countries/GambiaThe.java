package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class GambiaThe extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GMD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Banjul", "Basse", "Brikama", "Janjanbureh",
				"Kanifing", "Kerewan", "Kuntaur", "Mansakonko" };
	}

}
