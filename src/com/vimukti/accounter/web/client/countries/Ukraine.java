package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Ukraine extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "UAH";
		// UAK also..
	}

	@Override
	public String[] getStates() {
		return new String[] { "Central", "Eastern", "Northern", "Western" };
	}

}
