package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class ChristmasIsland extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "AUD";
	}

	@Override
	public String[] getStates() {
		return new String[] {};
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+7:00 Indian/Christmas";
	}

}
