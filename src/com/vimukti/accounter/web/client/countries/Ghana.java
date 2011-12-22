package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Ghana extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Ashanti", "Brong-Ahafo", "Central",
				"Eastern", "Greater Accra", "Northern", "Upper East",
				"Upper West", "Volta", "Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "GHC";
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
		return "UTC+0:00 Africa/Accra";
	}

}
