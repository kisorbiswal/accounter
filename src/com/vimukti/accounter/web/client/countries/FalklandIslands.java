package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class FalklandIslands extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "FKP";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Falkland Islands", "South Georgia" };
	}

}
