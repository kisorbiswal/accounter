package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class CaymanIslands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Grand Cayman" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KYD";
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
		return "UTC-5:00 America/Cayman";
	}

}
