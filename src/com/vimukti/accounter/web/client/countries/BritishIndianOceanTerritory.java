package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class BritishIndianOceanTerritory extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GBR";
		// SCR...
	}

	@Override
	public String[] getStates() {
		return new String[] { "Tortola" };
	}

}
