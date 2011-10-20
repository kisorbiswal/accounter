package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Macao extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "MOP";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Macau" };
	}

}
