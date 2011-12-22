package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Nauru extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Aiwo" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "AUD";
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
		return "UTC+12:00 Pacific/Nauru";
	}
}
