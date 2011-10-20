package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CookIslands extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "NZD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Aitutaki", "Atiu", "Mangaia", "Manihiki",
				"Mauke", "Mitiaro", "Nassau", "Pukapuka", "Rakahanga",
				"Rarotonga", "Tongareva" };
	}

}
