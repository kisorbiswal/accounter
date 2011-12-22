package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Tokelau extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Atafu", "Fakaofo", "Nukunonu" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "NZD";
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
		return "UTC-10:00 Pacific/Fakaofo";
	}

}
