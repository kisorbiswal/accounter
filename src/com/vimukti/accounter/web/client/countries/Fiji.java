package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Fiji extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "FJD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Central", "Eastern", "Northern", "Western" };
	}

}
