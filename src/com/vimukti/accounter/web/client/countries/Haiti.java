package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Haiti extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "HTG";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Artibonite", "Centre", "Grand'Anse", "Nord",
				"Nord-Est", "Nord-Ouest", "Ouest", "Sud", "Sud-Est" };
	}

}
