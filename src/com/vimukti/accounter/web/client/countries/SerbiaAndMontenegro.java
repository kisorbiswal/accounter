package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class SerbiaAndMontenegro extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Central Serbia", "Vojvodina" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "RSD";
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
		return "UTC+1:00 Europe/Belgrade";
	}
}
