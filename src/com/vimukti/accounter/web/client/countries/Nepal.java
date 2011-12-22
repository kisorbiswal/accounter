package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Nepal extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		return null;
	}

	@Override
	public String getPreferredCurrency() {
		return "NPR";
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+5:45 Asia/Kathmandu";
	}
}
