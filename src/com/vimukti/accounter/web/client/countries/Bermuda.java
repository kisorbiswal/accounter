package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Bermuda extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Hamilton", "Saint George" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BMD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 Atlantic/Bermuda";
	}

}
