package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Togo extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Centre", "Kara", "Maritime", "Plateaux", "Savanes" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XOF";
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
		return "UTC+0:00 Africa/Lome";
	}

}
