package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Turkmenistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Ahal", "Asgabat", "Balkan", "Dasoguz", "Lebap",
				"Mari" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TMT";
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
		return "UTC+5:00 Asia/Ashgabat";
	}

}
