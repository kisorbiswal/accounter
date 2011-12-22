package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class SolomonIslands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Central", "Choiseul", "Guadalcanal", "Isabel",
				"Makira and Ulawa", "Malaita", "Rennell and Bellona", "Temotu",
				"Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SBD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+11:00 Pacific/Guadalcanal";
	}

}
