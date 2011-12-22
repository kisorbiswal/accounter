package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class JanMayen extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "NOK";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Länsimaa " };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Arctic/Longyearbyen";
	}

}
