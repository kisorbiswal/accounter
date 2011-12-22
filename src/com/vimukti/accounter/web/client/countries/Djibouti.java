package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Djibouti extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String states[] = new String[] { "Ali Sabieh", "Dikhil", "Djibouti",
				"Obock", "Tadjoura" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "DJF";
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
		return "UTC+3:00 Africa/Djibouti";
	}

}
