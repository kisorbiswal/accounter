package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Aruba extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "AWG";
	}

	@Override
	public String[] getStates() {
		return new String[]{""};
	}

}
