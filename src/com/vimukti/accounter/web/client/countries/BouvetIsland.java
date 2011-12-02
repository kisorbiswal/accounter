package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class BouvetIsland extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "NOK";
	}

	@Override
	public String[] getStates() {
		return new String[]{""};
	}

}
