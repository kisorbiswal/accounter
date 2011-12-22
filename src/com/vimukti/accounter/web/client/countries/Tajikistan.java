package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Tajikistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Dushanbe", "Gorno-Badakhshan", "Karotegin",
				"Khatlon", "Sughd" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TJS";
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
		return "UTC+5:00 Asia/Dushanbe";
	}

}
