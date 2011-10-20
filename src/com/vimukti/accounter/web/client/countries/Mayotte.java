package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Mayotte extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Mayotte", "Pamanzi" };
	}

}
