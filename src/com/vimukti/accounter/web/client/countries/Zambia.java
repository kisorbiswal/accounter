package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Zambia extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "ZMK";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Central", "Copperbelt", "Eastern", "Luapala",
				"Lusaka", "Northern", "North-Western", "Southern", "Western" };
	}

}
