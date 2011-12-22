package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class HolySee extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public String[] getStates() {
		return new String[] {};
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Vatican";
	}

}
