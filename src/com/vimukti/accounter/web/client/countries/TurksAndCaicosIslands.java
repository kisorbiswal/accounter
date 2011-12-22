package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class TurksAndCaicosIslands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Grand Turk", "Middle Caicos",
				"North Caicos", "Providenciales and West Caicos", "Salt Cay",
				"South Caicos and East Caicos" };
		return states;
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
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-5:00 America/Grand_Turk";
	}

}
