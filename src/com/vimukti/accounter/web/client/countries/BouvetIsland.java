package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class BouvetIsland extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "NOK";
	}

	@Override
	public String[] getStates() {
		return new String[] { "" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Andorra";
	}

}
