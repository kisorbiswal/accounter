package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Comoros extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "KMF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Mwali", "Ndzouani", "Ngazidja" };
	}

}
