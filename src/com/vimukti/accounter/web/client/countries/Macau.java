package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Macau extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "MOP";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Macau" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+8:00 Asia/Macau";
	}

}
