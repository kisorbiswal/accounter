package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Uganda extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Central", "Eastern", "Northern",
				"Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "UGX";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.july();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+3:00 Africa/Kampala";
	}

}
