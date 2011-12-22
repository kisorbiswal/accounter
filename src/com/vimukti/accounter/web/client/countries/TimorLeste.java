package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class TimorLeste extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		return null;
	}

	@Override
	public String getPreferredCurrency() {
		return "USD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return null;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+9:00 Asia/Dili";
	}

}
