package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class PuertoRico extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		return null;
	}

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Puerto_Rico" ;
	}

}
